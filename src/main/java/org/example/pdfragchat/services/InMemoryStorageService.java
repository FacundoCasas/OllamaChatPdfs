package org.example.pdfragchat.services;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryStorageService {

    private final Map<String, List<String>> storage = new ConcurrentHashMap<>();

    public void save(String id, List<String> chunks) {
        storage.put(id, chunks);
    }

    public List<String> get(String id) {
        return storage.get(id);
    }

    public boolean alreadyExist(String id){
        return storage.containsKey(id);
    }
}


