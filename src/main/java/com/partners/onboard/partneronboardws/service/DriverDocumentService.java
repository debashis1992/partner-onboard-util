package com.partners.onboard.partneronboardws.service;

import com.partners.onboard.partneronboardws.constant.DocumentConstants;
import com.partners.onboard.partneronboardws.enums.VerificationStatus;
import com.partners.onboard.partneronboardws.exception.DriverNotFoundException;
import com.partners.onboard.partneronboardws.model.Driver;
import com.partners.onboard.partneronboardws.model.documents.Document;
import com.partners.onboard.partneronboardws.repository.DocumentRepository;
import com.partners.onboard.partneronboardws.repository.DriverRepository;
import com.partners.onboard.partneronboardws.service.state.DriverState;
import com.partners.onboard.partneronboardws.service.state.impl.BackgroundVerificationState;
import com.partners.onboard.partneronboardws.service.verification.VerificationStrategy;
import com.partners.onboard.partneronboardws.service.verification.impl.VerificationRules;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class DriverDocumentService {
    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private VerificationRules verificationRules;

    @Autowired
    private BackgroundVerificationState backgroundVerificationState;


    public List<String> getRequiredDocuments(String email) throws DriverNotFoundException {

        var driverOptional = driverRepository.getDriverByEmail(email);
        if(driverOptional.isPresent()) {

            int cityPin = driverOptional.get().getCityPin();
            var verificationStrategies = verificationRules.getRules(cityPin);

            return verificationStrategies.stream().map(VerificationStrategy::requiredDocument)
                    .collect(Collectors.toList());

        }

        throw new DriverNotFoundException("no driver found with email: "+email);
    }

    public void saveDocument(String id, MultipartFile file, Document document) {



        var driver = driverRepository.getDriver(id);
        if(driver.isPresent()) {
            Driver d = driver.get();

            // check if the prev state from document collection state is already completed
            d.getDriverState().isValidRequest(d);

            if(DocumentConstants.DOCUMENT_DRIVER.equals(document.getDocumentType())) {
                document.setDocumentType(DocumentConstants.DOCUMENT_DRIVER);

            }
            else if(DocumentConstants.DOCUMENT_VEHICLE.equals(document.getDocumentType())) {
                document.setDocumentType(DocumentConstants.DOCUMENT_VEHICLE);
            }

            String documentLocationId = documentRepository.save(file);
            document.setDocumentLocationId(documentLocationId);
            d.getDocuments().add(document);
        }
    }

    public void updateDocument(String id, MultipartFile file, Document document) {

        var driverOptional = driverRepository.getDriver(id);
        if(driverOptional.isPresent()) {
            var driver = driverOptional.get();

            // check if the prev state from document collection state is already completed
            driver.getDriverState().isValidRequest(driver);

            String documentLocationId = documentRepository.save(file);

            var documentOptional = driver.getDocuments().stream().filter(d -> d.getDocumentId().equals(document.getDocumentId()))
                    .findFirst();

            if(documentOptional.isPresent()) {
                Document alreadySavedDocument = documentOptional.get();
                alreadySavedDocument.setDocumentLocationId(documentLocationId);
//                alreadySavedDocument.setUpdatedDate(new Date());
            }
            else {
                //if no such document was present, just delete the multipart file from document storage
                documentRepository.delete(documentLocationId);
            }

        }

    }

    public void triggerDocumentVerification(String id) {
        var driverOptional = driverRepository.getDriver(id);
        if(driverOptional.isPresent()) {

            var driver = driverOptional.get();

            // when trigger document verification is called
            // then set the currentState as document verification state

            DriverState driverState = driver.setAndGetDriverState(backgroundVerificationState);
            driverState.processApplication(driver);
        }
    }
}
