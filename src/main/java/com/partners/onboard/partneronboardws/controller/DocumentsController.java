package com.partners.onboard.partneronboardws.controller;

import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.model.documents.Document;
import com.partners.onboard.partneronboardws.records.ApiResponse;
import com.partners.onboard.partneronboardws.records.RequiredDocumentResponse;
import com.partners.onboard.partneronboardws.service.DriverDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/driver-documents")
@Tag(name = "Document API", description = "These APIs are going to be used" +
        "for all kinds of CRUD operations for document upload, verification")
public class DocumentsController {

    @Autowired
    private DriverDocumentService driverDocumentService;

    @GetMapping("/required")
    @Operation(summary = "Get all the required documents for a driver")
    public ResponseEntity<RequiredDocumentResponse> getRequiredDocuments(@Param("email") @NotEmpty @NotNull String email) throws DriverNotFoundException {

        return ResponseEntity.ok(driverDocumentService.getRequiredDocuments(email));
    }

    @PostMapping(value = "/upload")
    @Operation(summary = "Upload a document")
    public ResponseEntity<ApiResponse> uploadDocuments(@RequestParam("id") @NotEmpty @NotNull String id,
                                                       @RequestPart("file") MultipartFile file,
                                                       @RequestPart("document") @Valid Document document) throws DriverNotFoundException {

        driverDocumentService.saveDocument(id, file, document);
        return ResponseEntity.ok(new ApiResponse("documents uploaded successfully"));
    }

    @PutMapping("/update")
    @Operation(summary = "Update an already uploaded document")
    public ResponseEntity<ApiResponse> updateDocument(@RequestParam("id") @NotEmpty @NotNull String id,
                                                      @RequestPart("file") MultipartFile file,
                                                      @RequestPart("document") @Valid Document document) throws DriverNotFoundException {

        driverDocumentService.updateDocument(id, file, document);
        return ResponseEntity.ok(new ApiResponse("document was updated successfully"));
    }

    @PostMapping("/verify")
    @Operation(summary = "Trigger document verification for all documents")
    public ResponseEntity<ApiResponse> triggerDocumentVerification(@Param("id") @NotEmpty @NotNull String id) throws DriverNotFoundException {

        driverDocumentService.triggerDocumentVerification(id);
        return ResponseEntity.ok(new ApiResponse("document verification is in progress"));
    }

}
