package edu.icet.book.book_network.services;

import edu.icet.book.book_network.dto.AuthenticationRequest;
import edu.icet.book.book_network.dto.AuthenticationResponse;
import edu.icet.book.book_network.dto.RegistrationRequest;
import edu.icet.book.book_network.entity.Token;
import edu.icet.book.book_network.entity.User;
import edu.icet.book.book_network.repository.RoleRepository;
import edu.icet.book.book_network.repository.TokenRepository;
import edu.icet.book.book_network.repository.UserRepository;
import edu.icet.book.book_network.util.email.EmailTemplateName;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    @Value("${application.mailing.frontend.activation-url}")
    private String activationUrl;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public void register(@Valid RegistrationRequest registrationRequest) throws MessagingException {
        var userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("User role not found"));
        var user = User.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .email(registrationRequest.getEmail())
                .password(passwordEncoder.encode(registrationRequest.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();
        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generateAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.getFullName(),
                EmailTemplateName.ACTIVATION_ACCOUNT,
                activationUrl,
                newToken,
                "Account activation");
    }

    private String generateAndSaveActivationToken(User user) {
        String generatedToken = generateActivationCode(6);
        Token token = Token.builder()
                .token(generatedToken)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generatedToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        // cryptographically secure the random number generator
        SecureRandom secureRandom = new SecureRandom();
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthenticationResponse authenticate(
            AuthenticationRequest authenticationRequest) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );
        var claims = new HashMap<String, Object>();
        var user = ((User) auth.getPrincipal());
        claims.put("fullName", user.getFullName());
        var jwtToken = jwtService.generateToken(claims, user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

//    @Transactional
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token).
                orElseThrow(() -> new RuntimeException("Token not found"));
        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Token expired");
        }
        var user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        userRepository.save(user);
    }
}
