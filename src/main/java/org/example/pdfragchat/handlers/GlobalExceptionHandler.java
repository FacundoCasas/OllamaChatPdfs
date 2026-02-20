package org.example.pdfragchat.handlers;

import org.example.pdfragchat.dto.UploadResponse;
import org.example.pdfragchat.exceptions.FileNameInvalidException;
import org.example.pdfragchat.exceptions.PdfAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FileNameInvalidException.class)
    public ResponseEntity<UploadResponse> handleInvalidFile(FileNameInvalidException ex) {
        return ResponseEntity
                .unprocessableEntity()
                .body(new UploadResponse(null, ex.getMessage()));
    }

    @ExceptionHandler(PdfAlreadyExistsException.class)
    public ResponseEntity<UploadResponse> handleAlreadyExists(PdfAlreadyExistsException ex) {
        return ResponseEntity
                .unprocessableEntity()
                .body(new UploadResponse(null, ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<UploadResponse> handleGeneric(Exception ex) {
        return ResponseEntity
                .internalServerError()
                .body(new UploadResponse(null, "Unexpected error"));
    }
}
