package org.example.pdfragchat.controllers;

import org.example.pdfragchat.dto.UploadResponse;
import org.example.pdfragchat.dto.DocumentChunk;
import org.example.pdfragchat.services.PdfProcessingService;
import org.example.pdfragchat.services.RagService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    private final PdfProcessingService pdfProcessingService;
    private final RagService ragService;

    public PdfController(PdfProcessingService pdfProcessingService, RagService ragService) {
        this.pdfProcessingService = pdfProcessingService;
        this.ragService = ragService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UploadResponse> uploadPdf(
            @RequestParam("file") MultipartFile file) {

        String id = pdfProcessingService.processAndStore(file);

        return ResponseEntity.ok(new UploadResponse(id, null));
    }

    @GetMapping("/{id}")
    public ResponseEntity<List<DocumentChunk>> getChunks(@PathVariable String id) {

        List<DocumentChunk> chunks = pdfProcessingService.getDocumentChunks(id);

        if (chunks == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(chunks);
    }

    @PostMapping("/{id}/ask")
    public ResponseEntity<String> ask(
            @PathVariable String id,
            @RequestParam String question) {

        String response = ragService.ask(id, question);

        return ResponseEntity.ok(response);
    }
}
