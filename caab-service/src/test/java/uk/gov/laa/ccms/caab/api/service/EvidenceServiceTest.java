package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildEvidenceDocument;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildEvidenceDocumentDetail;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.entity.EvidenceDocument;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.EvidenceMapper;
import uk.gov.laa.ccms.caab.api.repository.EvidenceDocumentRepository;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetails;

@ExtendWith(MockitoExtension.class)
class EvidenceServiceTest {
    @Mock
    private EvidenceDocumentRepository repository;

    @Mock
    private EvidenceMapper mapper;

    @InjectMocks
    private EvidenceService evidenceService;

    @Test
    void createEvidence_createsEvidenceDocument() {
        EvidenceDocumentDetail evidenceDocumentDetail = new EvidenceDocumentDetail();
        EvidenceDocument evidenceDocument = buildEvidenceDocument();

        when(mapper.toEvidenceDocument(evidenceDocumentDetail)).thenReturn(evidenceDocument);
        when(repository.save(evidenceDocument)).thenReturn(evidenceDocument);

        Long result = evidenceService.createEvidenceDocument(evidenceDocumentDetail);

        assertNotNull(result);
        assertEquals(evidenceDocument.getId(), result);

        verify(mapper).toEvidenceDocument(evidenceDocumentDetail);
        verify(repository).save(evidenceDocument);
    }

    @Test
    void getEvidenceDocumentDetails_queriesBasedOnExample() {
        EvidenceDocument evidenceDocument = new EvidenceDocument();
        evidenceDocument.setApplicationOrOutcomeId("appOutId");
        evidenceDocument.setCaseReferenceNumber("caseRef");
        evidenceDocument.setProviderId("provId");
        evidenceDocument.setDocumentType("docType");
        evidenceDocument.setTransferStatus("status");
        evidenceDocument.setCcmsModule("module");

        EvidenceDocumentDetails expectedResponse = new EvidenceDocumentDetails();

        Page<EvidenceDocument> evidenceDocumentPage = new PageImpl<>(
            List.of(buildEvidenceDocument()));

        final ArgumentCaptor<Example<EvidenceDocument>> exampleArgumentCaptor =
            ArgumentCaptor.forClass(Example.class);

        when(repository.findAll(exampleArgumentCaptor.capture(), eq(Pageable.unpaged()))).thenReturn(evidenceDocumentPage);
        when(mapper.toEvidenceDocumentDetails(evidenceDocumentPage)).thenReturn(expectedResponse);

        EvidenceDocumentDetails result = evidenceService.getEvidenceDocuments(
            evidenceDocument.getApplicationOrOutcomeId(),
            evidenceDocument.getCaseReferenceNumber(),
            evidenceDocument.getProviderId(),
            evidenceDocument.getDocumentType(),
            evidenceDocument.getTransferStatus(),
            evidenceDocument.getCcmsModule(),
            Pageable.unpaged());

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(repository).findAll(any(Example.class), eq(Pageable.unpaged()));
        verify(mapper).toEvidenceDocumentDetails(evidenceDocumentPage);

        // Check that the example EvidenceDocument was initialised based on the method params.
        checkExampleDocument(evidenceDocument, exampleArgumentCaptor.getValue().getProbe());
    }

    @Test
    void getEvidenceDocumentDetail_retrievesCorrectly() {
        EvidenceDocumentDetail evidenceDocumentDetail = buildEvidenceDocumentDetail();

        EvidenceDocument evidenceDocument = buildEvidenceDocument();

        when(repository.findById(evidenceDocumentDetail.getId().longValue())).thenReturn(Optional.of(evidenceDocument));
        when(mapper.toEvidenceDocumentDetail(evidenceDocument)).thenReturn(evidenceDocumentDetail);

        EvidenceDocumentDetail result = evidenceService.getEvidenceDocument(evidenceDocumentDetail.getId().longValue());

        assertNotNull(result);
        assertEquals(evidenceDocumentDetail, result);

        verify(repository).findById(evidenceDocumentDetail.getId().longValue());
        verify(mapper).toEvidenceDocumentDetail(evidenceDocument);
    }

    @Test
    void removeEvidenceDocument_whenExists_removesEntry() {
        Long evidenceDocumentId = 1L;
        when(repository.existsById(evidenceDocumentId)).thenReturn(true);
        doNothing().when(repository).deleteById(evidenceDocumentId);

        evidenceService.removeEvidenceDocument(evidenceDocumentId);

        verify(repository).existsById(evidenceDocumentId);
        verify(repository).deleteById(evidenceDocumentId);
    }

    @Test
    void removeEvidenceDocument_whenNotExists_throwsException() {
        Long evidenceDocumentId = 1L;
        when(repository.existsById(evidenceDocumentId)).thenReturn(false);

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            evidenceService.removeEvidenceDocument(evidenceDocumentId));

        assertEquals("EvidenceDocument with id: 1 not found", exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void removeEvidenceDocumentDetails_deletesBasedOnExample() {
        EvidenceDocument evidenceDocument = new EvidenceDocument();
        evidenceDocument.setApplicationOrOutcomeId("appOutId");
        evidenceDocument.setCaseReferenceNumber("caseRef");
        evidenceDocument.setProviderId("provId");
        evidenceDocument.setDocumentType("docType");
        evidenceDocument.setTransferStatus("status");
        evidenceDocument.setCcmsModule("module");

        List<EvidenceDocument> evidenceDocuments =
            List.of(buildEvidenceDocument());

        final ArgumentCaptor<Example<EvidenceDocument>> exampleArgumentCaptor =
            ArgumentCaptor.forClass(Example.class);

        when(repository.findAll(exampleArgumentCaptor.capture())).thenReturn(evidenceDocuments);

        evidenceService.removeEvidenceDocuments(
            evidenceDocument.getApplicationOrOutcomeId(),
            evidenceDocument.getCaseReferenceNumber(),
            evidenceDocument.getProviderId(),
            evidenceDocument.getDocumentType(),
            evidenceDocument.getTransferStatus(),
            evidenceDocument.getCcmsModule());

        verify(repository).findAll(any(Example.class));
        verify(repository).deleteAll(evidenceDocuments);

        // Check that the example EvidenceDocument was initialised based on the method params.
        checkExampleDocument(evidenceDocument, exampleArgumentCaptor.getValue().getProbe());
    }

    private static void checkExampleDocument(EvidenceDocument evidenceDocument,
        EvidenceDocument exampleDocument) {
        assertEquals(evidenceDocument.getApplicationOrOutcomeId(), exampleDocument.getApplicationOrOutcomeId());
        assertEquals(evidenceDocument.getCaseReferenceNumber(), exampleDocument.getCaseReferenceNumber());
        assertEquals(evidenceDocument.getProviderId(), exampleDocument.getProviderId());
        assertEquals(evidenceDocument.getDocumentType(), exampleDocument.getDocumentType());
        assertEquals(evidenceDocument.getTransferStatus(), exampleDocument.getTransferStatus());
        assertEquals(evidenceDocument.getCcmsModule(), exampleDocument.getCcmsModule());
    }

}

