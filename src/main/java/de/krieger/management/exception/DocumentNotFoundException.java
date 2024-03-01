package de.krieger.management.exception;

public class DocumentNotFoundException extends RuntimeException{
    public DocumentNotFoundException(String message) {
        super(message);
    }
}
