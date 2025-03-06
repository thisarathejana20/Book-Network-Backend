package edu.icet.book.book_network.util.email;

import lombok.Getter;

@Getter
public enum EmailTemplateName {
    ACTIVATION_ACCOUNT("activation-account");

    private final String name;

    EmailTemplateName(String name) {
        this.name = name;
    }
}
