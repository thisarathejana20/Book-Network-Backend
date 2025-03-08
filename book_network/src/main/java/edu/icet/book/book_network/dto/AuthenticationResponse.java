package edu.icet.book.book_network.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AuthenticationResponse {
    private String token;
}
