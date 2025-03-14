package edu.icet.book.book_network.util.exception.custom;

public class OperationNotPermittedException extends RuntimeException{
    private String message;

    public OperationNotPermittedException(String message) {
        super(message);
    }
}
