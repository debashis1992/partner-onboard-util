package com.partners.onboard.partneronboardws.repository;

import com.partners.onboard.partneronboardws.model.documents.Document;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DocumentRepository {
    private ConcurrentHashMap<String, Document> documentMap = new ConcurrentHashMap<>();

    public void save(Document document) {
        documentMap.put(document.getDocumentId(), document);
    }

    public void update(Document document) {
        documentMap.put(document.getDocumentId(), document);
    }

    public void delete(Document document) {
        if(documentMap.containsKey(document.getDocumentId())) {
            documentMap.remove(document.getDocumentId());
        }
    }
}
