package edu.icet.book.book_network.controller;

import edu.icet.book.book_network.dto.AuthenticationRequest;
import edu.icet.book.book_network.dto.AuthenticationResponse;
import edu.icet.book.book_network.dto.RegistrationRequest;
import edu.icet.book.book_network.services.impl.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("auth")
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest registrationRequest) {
        try {
            authenticationService.register(registrationRequest);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody @Valid AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
    }

    @GetMapping("/activate-account")
    public void confirm(@RequestParam("token") String token) {
        try {
            authenticationService.activateAccount(token);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
