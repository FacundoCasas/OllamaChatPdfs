package org.example.pdfragchat.services;

import org.example.pdfragchat.exceptions.FileNameInvalidException;
import org.example.pdfragchat.exceptions.PdfAlreadyExistsException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class PdfProcessingService {

    private final PdfTextExtractor pdfTextExtractor;
    private final TextChunker textChunker;
    private final InMemoryStorageService storageService;

    private static final int CHUNK_SIZE = 500;
    private static final int OVERLAP = 50;

    public PdfProcessingService(
            PdfTextExtractor pdfTextExtractor,
            TextChunker textChunker,
            InMemoryStorageService storageService
    ) {
        this.pdfTextExtractor = pdfTextExtractor;
        this.textChunker = textChunker;
        this.storageService = storageService;
    }

    public String processAndStore(MultipartFile file) {

        validateFile(file);

        String originalName = file.getOriginalFilename();
        String id = generateIdFromFilename(originalName);

        String text = pdfTextExtractor.extractText(file);
        List<String> chunks = textChunker.chunkText(text, CHUNK_SIZE, OVERLAP);

        storageService.save(id, chunks);

        return id;
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FileNameInvalidException("File is empty");
        }

        String filename = file.getOriginalFilename();

        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new FileNameInvalidException("File name is invalid");
        }

        if (pdfAlreadyExist(file)) {
            throw new PdfAlreadyExistsException("File already exists");
        }
    }

    public List<String> getChunks(String filename) {
        return storageService.get(filename);
    }

    public boolean pdfAlreadyExist(MultipartFile file){
        return storageService.alreadyExist(generateIdFromFilename(file.getOriginalFilename()));
    }

    public boolean fileNameIsInvalid(MultipartFile file){
        String fileName = file.getOriginalFilename();
        return fileName == null || fileName.isBlank();
    }

    private String generateIdFromFilename(String filename) {

        if (filename == null || filename.isBlank()) {
            return "unknown-file";
        }

        // remove extension
        String nameWithoutExtension = filename.replaceAll("\\.pdf$", "");

        // replace spaces with hyphens
        return nameWithoutExtension
                .toLowerCase()
                .replaceAll("[^a-z0-9-]", "-");
    }
}
