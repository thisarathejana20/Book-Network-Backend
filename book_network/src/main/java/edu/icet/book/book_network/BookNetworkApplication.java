package edu.icet.book.book_network;

import edu.icet.book.book_network.entity.Role;
import edu.icet.book.book_network.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableJpaAuditing
@EnableAsync
public class BookNetworkApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookNetworkApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(RoleRepository roleRepository) {
        return args -> {
            if (roleRepository.findByName("USER").isEmpty()) {
                roleRepository.save(
                        Role.builder().name("USER").build()
                );
            }
        };
    }
}
