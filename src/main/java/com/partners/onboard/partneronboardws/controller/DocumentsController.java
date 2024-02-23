package com.partners.onboard.partneronboardws.controller;

import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.model.ApiResponse;
import com.partners.onboard.partneronboardws.model.documents.Document;
import com.partners.onboard.partneronboardws.service.DriverDocumentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/driver-documents")
public class DocumentsController {

    @Autowired
    private DriverDocumentService driverDocumentService;

    @GetMapping("/required")
    public ResponseEntity<List<String>> getRequiredDocuments(@Param("email") @NotEmpty @NotNull String email) throws DriverNotFoundException {

        return ResponseEntity.ok(driverDocumentService.getRequiredDocuments(email));
    }

    @PostMapping(value = "/upload")
    public ResponseEntity<ApiResponse> uploadDocuments(@RequestParam("id") @NotEmpty @NotNull String id,
                                                       @RequestPart("file") MultipartFile file, @RequestPart("document") @Valid Document document) throws DriverNotFoundException {

        driverDocumentService.saveDocument(id, file, document);
        return ResponseEntity.ok(ApiResponse.builder().message("documents uploaded successfully").build());
    }

    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateDocument(@RequestParam("id") @NotEmpty @NotNull String id,
                                                      @RequestPart("file") MultipartFile file, @RequestPart("document") @Valid Document document) throws DriverNotFoundException {

        driverDocumentService.updateDocument(id, file, document);
        return ResponseEntity.ok(ApiResponse.builder().message("document was updated successfully").build());
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> triggerDocumentVerification(@Param("id") @NotEmpty @NotNull String id) throws DriverNotFoundException {

        driverDocumentService.triggerDocumentVerification(id);
        return ResponseEntity.ok(ApiResponse.builder().message("document verification is in progress").build());
    }

}
