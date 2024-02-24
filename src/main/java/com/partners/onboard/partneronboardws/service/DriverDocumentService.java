package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.constant.DocumentConstants;
import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.model.documents.Document;
import com.partners.onboard.partneronboardws.records.RequiredDocumentResponse;
import com.partners.onboard.partneronboardws.repository.DocumentFileRepository;
import com.partners.onboard.partneronboardws.repository.DocumentRepository;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.BackgroundVerificationState;
import com.partners.onboard.partneronboardws.service.state.impl.DocumentsCollectionState;
import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;
import com.partners.onboard.partneronboardws.service.verification.impl.VerificationRules;
import com.partners.onboard.partneronboardws.utils.DriverUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

@Service
public class DriverDocumentService {
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DocumentFileRepository documentFileRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private VerificationRules verificationRules;

    @Autowired
    private BackgroundVerificationState backgroundVerificationState;

    @Autowired
    private DocumentsCollectionState documentsCollectionState;

    @Autowired
    private DriverUtils driverUtils;

    public RequiredDocumentResponse getRequiredDocuments(String email) throws DriverNotFoundException {

        var driver = driverUtils.getDriverDetailsByEmail(email);
        int cityPin = driver.getCityPin();
        var verificationStrategies = verificationRules.getRules(cityPin);

        if (verificationStrategies.isEmpty()) {
            return new RequiredDocumentResponse("no documents found for the city pin", new ArrayList<>());
        }

        return new RequiredDocumentResponse("success", verificationStrategies.stream().map(VerificationStrategy::requiredDocument)
                .collect(Collectors.toList()));
    }

    public void saveDocument(String id, MultipartFile file, Document document) throws DriverNotFoundException {


        var d = driverUtils.getDriverDetails(id);

        // check if the prev state from document collection state is already completed
        d.getDriverState().isValidRequest(d);

        d.setAndGetDriverState(documentsCollectionState);
        if (DocumentConstants.DOCUMENT_DRIVER.equals(document.getDocumentType())) {
            document.setDocumentType(DocumentConstants.DOCUMENT_DRIVER);

        } else if (DocumentConstants.DOCUMENT_VEHICLE.equals(document.getDocumentType())) {
            document.setDocumentType(DocumentConstants.DOCUMENT_VEHICLE);
        }

        document.setCreatedDate(new Date());
        var documentLocationId = documentFileRepository.save(file);
        document.setDocumentLocationId(documentLocationId);
        documentRepository.save(document);

        d.getDocuments().add(document);

    }

    public void updateDocument(String id, MultipartFile file, Document document) throws DriverNotFoundException {

        var driver = driverUtils.getDriverDetails(id);

        // check if the prev state from document collection state is already completed
        driver.getDriverState().isValidRequest(driver);

        var documentLocationId = documentFileRepository.save(file);

        var documentOptional = driver.getDocuments().stream()
                .filter(d -> d.getDocumentId().equals(document.getDocumentId()))
                .findFirst();

        if (documentOptional.isPresent()) {
            var alreadySavedDocument = documentOptional.get();
            alreadySavedDocument.setDocumentLocationId(documentLocationId);
            alreadySavedDocument.setUpdatedDate(new Date());
            alreadySavedDocument.setAttributes(document.getAttributes());
            documentRepository.save(alreadySavedDocument);

        } else {
            //if no such document was present, just delete the multipart file from document storage
            documentFileRepository.delete(documentLocationId);
        }
    }

    public void triggerDocumentVerification(String id) throws DriverNotFoundException {

        var driver = driverUtils.getDriverDetails(id);

        // when trigger document verification is called
        // then set the currentState as document verification state

        DriverState driverState = driver.setAndGetDriverState(backgroundVerificationState);
        driverState.processApplication(driver);
    }
}
