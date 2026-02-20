package org.example.pdfragchat.exceptions;

public class PdfAlreadyExistsException extends RuntimeException {
    public PdfAlreadyExistsException(String message) {
        super(message);
    }
}