package org.example.pdfragchat.services;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class PdfTextExtractor {

    public String extractText(MultipartFile file) {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("The File is Empty");
        }

        if (!file.getOriginalFilename().toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed");
        }

        try (InputStream inputStream = file.getInputStream();
             PDDocument document = PDDocument.load(inputStream)) {

            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);

        } catch (Exception e) {
            throw new RuntimeException("Error extracting text from PDF", e);
        }
    }
}
