package com.partners.onboard.partneronboardws.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DocumentFileRepository {

    private ConcurrentHashMap<String, MultipartFile> documentsStorage = new ConcurrentHashMap<>();

    public String save(MultipartFile file) {
        String id = UUID.randomUUID().toString();
        documentsStorage.put(id, file);
        return id;
    }

    public Optional<MultipartFile> getDocument(String id) {
        return Optional.ofNullable(documentsStorage.get(id));
    }

    public void update(String id, MultipartFile file) {
        documentsStorage.put(id, file);
    }

    public void delete(String id) {
        documentsStorage.remove(id);
    }
}
