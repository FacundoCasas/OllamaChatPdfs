package org.example.pdfragchat.controllers;

import org.example.pdfragchat.dto.UploadResponse;
import org.example.pdfragchat.services.PdfProcessingService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfProcessingService pdfProcessingService;

    public PdfController(PdfProcessingService pdfProcessingService) {
        this.pdfProcessingService = pdfProcessingService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadPdf(
            @RequestParam("file") MultipartFile file) {

        String id = pdfProcessingService.processAndStore(file);

        return ResponseEntity.ok(new UploadResponse(id, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<String>> getChunks(@PathVariable String id) {

        List<String> chunks = pdfProcessingService.getChunks(id);

        if (chunks == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(chunks);
    }
}
