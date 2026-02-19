package org.example.pdfragchat.controllers;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/test")
public class ChatTestController {

    private final ChatClient chatClient;

    public ChatTestController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping
    public String test(@RequestParam String prompt) {
        return chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
