package org.example.pdfragchat.services;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TextChunker {

    public List<String> chunkText(String text, int chunkSize, int overlap) {

        List<String> chunks = new ArrayList<>();

        int start = 0;

        while (start < text.length()) {
            int end = Math.min(text.length(), start + chunkSize);

            chunks.add(text.substring(start, end));

            start += (chunkSize - overlap);
        }

        return chunks;
    }
}
