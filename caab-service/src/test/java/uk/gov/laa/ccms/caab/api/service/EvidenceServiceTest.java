package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildEvidenceDocument;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildEvidenceDocumentDetail;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
    void getEvidenceDocumentDetails_queriesBySpecification() {
        EvidenceDocument evidenceDocument = buildEvidenceDocument();

        EvidenceDocumentDetails expectedResponse = new EvidenceDocumentDetails();

        Page<EvidenceDocument> evidenceDocumentPage = new PageImpl<>(
            List.of(buildEvidenceDocument()));

        when(repository.findAll(any(Specification.class),
            eq(Pageable.unpaged()))).thenReturn(evidenceDocumentPage);
        when(mapper.toEvidenceDocumentDetails(evidenceDocumentPage)).thenReturn(expectedResponse);

        EvidenceDocumentDetails result = evidenceService.getEvidenceDocuments(
            evidenceDocument.getApplicationOrOutcomeId(),
            evidenceDocument.getCaseReferenceNumber(),
            evidenceDocument.getProviderId(),
            evidenceDocument.getDocumentType(),
            evidenceDocument.getCcmsModule(),
            Boolean.TRUE,
            Pageable.unpaged());

        assertNotNull(result);
        assertEquals(expectedResponse, result);

        verify(repository).findAll(any(Specification.class), eq(Pageable.unpaged()));
        verify(mapper).toEvidenceDocumentDetails(evidenceDocumentPage);
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
        EvidenceDocument evidenceDocument = buildEvidenceDocument();

        List<EvidenceDocument> evidenceDocuments =
            List.of(buildEvidenceDocument());

        when(repository.findAll(any(Specification.class))).thenReturn(evidenceDocuments);

        evidenceService.removeEvidenceDocuments(
            evidenceDocument.getApplicationOrOutcomeId(),
            evidenceDocument.getCaseReferenceNumber(),
            evidenceDocument.getProviderId(),
            evidenceDocument.getDocumentType(),
            evidenceDocument.getCcmsModule(),
            Boolean.TRUE);

        verify(repository).findAll(any(Specification.class));
        verify(repository).deleteAll(evidenceDocuments);

    }

    @Test
    @DisplayName("updateEvidence updates the evidence document when it exists")
    void updateEvidence_UpdatesWhenExists() {
        Long evidenceDocumentId = 1L;
        EvidenceDocumentDetail evidenceDocumentDetail = buildEvidenceDocumentDetail();
        EvidenceDocument existingEvidenceDocument = buildEvidenceDocument();

        when(repository.findById(evidenceDocumentId)).thenReturn(Optional.of(existingEvidenceDocument));
        doNothing().when(mapper).mapIntoEvidence(existingEvidenceDocument, evidenceDocumentDetail);
        when(repository.save(existingEvidenceDocument)).thenReturn(existingEvidenceDocument);

        evidenceService.updateEvidence(evidenceDocumentId, evidenceDocumentDetail);

        verify(repository).findById(evidenceDocumentId);
        verify(mapper).mapIntoEvidence(existingEvidenceDocument, evidenceDocumentDetail);
        verify(repository).save(existingEvidenceDocument);
    }

    @Test
    @DisplayName("updateEvidence throws exception when evidence document is not found")
    void updateEvidence_ThrowsExceptionWhenNotFound() {
        Long evidenceDocumentId = 1L;
        EvidenceDocumentDetail evidenceDocumentDetail = buildEvidenceDocumentDetail();

        when(repository.findById(evidenceDocumentId)).thenReturn(Optional.empty());

        CaabApiException exception = assertThrows(CaabApiException.class, () ->
            evidenceService.updateEvidence(evidenceDocumentId, evidenceDocumentDetail));

        assertEquals(String.format("Failed to find evidence with id: %s", evidenceDocumentId), exception.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());

        verify(repository).findById(evidenceDocumentId);
        verify(repository, never()).save(any(EvidenceDocument.class));
    }

}

