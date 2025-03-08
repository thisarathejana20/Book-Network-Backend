package edu.icet.book.book_network.util;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum BusinessErrorCodes {
    NO_CODE(0, "No code", HttpStatus.NOT_IMPLEMENTED),
    INCORRECT_CURRENT_PASSWORD(300, "Incorrect current password", HttpStatus.FORBIDDEN),
    INCORRECT_NEW_PASSWORD(301, "Incorrect new password", HttpStatus.FORBIDDEN),
    ACCOUNT_LOCKED(302, "Account locked", HttpStatus.FORBIDDEN),
    ACCOUNT_DISABLED(303, "Account disabled", HttpStatus.FORBIDDEN),
    BAD_CREDENTIALS(304, "Bad credentials", HttpStatus.FORBIDDEN),
    ;
    @Getter
    private final Integer code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus status;

    BusinessErrorCodes(Integer code, String description, HttpStatus status) {
        this.code = code;
        this.description = description;
        this.status = status;
    }
}
