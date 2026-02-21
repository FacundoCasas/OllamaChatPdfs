package org.example.pdfragchat.services;

import org.example.pdfragchat.dto.DocumentChunk;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final InMemoryStorageService storageService;
    private final EmbeddingModel embeddingModel;
    private final ChatClient chatClient;

    public RagService(
            InMemoryStorageService storageService,
            EmbeddingModel embeddingModel,
            ChatClient chatClient
    ) {
        this.storageService = storageService;
        this.embeddingModel = embeddingModel;
        this.chatClient = chatClient;
    }

    public String ask(String documentId, String question) {

        float[] questionEmbedding = embeddingModel.embed(question);

        List<DocumentChunk> relevantChunks =
                storageService.findMostSimilar(documentId, questionEmbedding, 3);

        String context = relevantChunks.stream()
                .map(DocumentChunk::content)
                .collect(Collectors.joining("\n\n"));

        String prompt = """
                Use the following context to answer the question.
                If the answer is not in the context, say you don't know.

                Context:
                %s

                Question:
                %s
                """.formatted(context, question);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}

