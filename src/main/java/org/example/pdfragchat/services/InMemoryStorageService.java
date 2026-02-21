package org.example.pdfragchat.services;

import org.example.pdfragchat.dto.DocumentChunk;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryStorageService {

    private final Map<String, List<DocumentChunk>> storage = new ConcurrentHashMap<>();


    public void save(String id, List<DocumentChunk> documentChunks) {
        storage.put(id, documentChunks);
    }

    public List<DocumentChunk> get(String id) {
        return storage.get(id);
    }

    public boolean alreadyExist(String id){
        return storage.containsKey(id);
    }

    public List<DocumentChunk> findMostSimilar(String id, float[] queryEmbedding, int topK) {
        return get(id).stream()
                .sorted(Comparator.comparingDouble(
                        chunk -> -cosineSimilarity(chunk.embedding(), queryEmbedding)
                ))
                .limit(topK)
                .toList();
    }

    private double cosineSimilarity(float[] v1, float[] v2) {

        double dot = 0.0;
        double norm1 = 0.0;
        double norm2 = 0.0;

        if (v1.length != v2.length) {
            throw new IllegalArgumentException("Embedding dimensions do not match");
        }

        for (int i = 0; i < v1.length; i++) {
            dot += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }

        return dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
    }
}


