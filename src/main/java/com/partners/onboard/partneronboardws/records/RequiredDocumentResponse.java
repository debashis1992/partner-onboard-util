package com.partners.onboard.partneronboardws.records;

import java.util.List;

public record RequiredDocumentResponse(String message, List<String> requiredDocuments) {
}
