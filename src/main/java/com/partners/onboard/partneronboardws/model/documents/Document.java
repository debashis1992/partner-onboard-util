package com.partners.onboard.partneronboardws.model.documents;

import com.partners.onboard.partneronboardws.enums.VerificationStatus;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Data @AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Document {
    String documentId;
    String documentName;
    String documentType;
    Object documentLocationId;
    Map<String, String> attributes;

    Date createdDate;
    Date updatedDate;
    String verificationStatus;
    Date verifiedDate;
    String verificationFailureReason;

    public Document() {
        documentId = UUID.randomUUID().toString();
        verificationStatus = VerificationStatus.NOT_VERIFIED.name();
    }

    @PrePersist
    public void setCreatedDate() {
        createdDate = new Date();
    }

    @PostUpdate
    public void setUpdatedDate() {
        updatedDate = new Date();
    }
}
