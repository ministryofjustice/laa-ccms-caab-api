package uk.gov.laa.ccms.caab.api.controller;


import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.laa.ccms.caab.api.EvidenceApi;
import uk.gov.laa.ccms.caab.api.service.EvidenceService;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetails;

/**
 * Controller handling evidence-related requests.
 * Implements the {@code EvidenceApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve evidence data.
 */
@RestController
@RequiredArgsConstructor
public class EvidenceController implements EvidenceApi {

  private final EvidenceService evidenceService;

  @Override
  public ResponseEntity<EvidenceDocumentDetails> getEvidenceDocuments(
      final String applicationOrOutcomeId,
      final String caseReferenceNumber,
      final String providerId,
      final String documentType,
      final String ccmsModule,
      final Boolean transferPending,
      final Pageable pageable) {

    EvidenceDocumentDetails evidenceDocumentDetails = evidenceService.getEvidenceDocuments(
        applicationOrOutcomeId,
        caseReferenceNumber,
        providerId,
        documentType,
        ccmsModule,
        transferPending,
        pageable);

    return new ResponseEntity<>(evidenceDocumentDetails, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<EvidenceDocumentDetail> getEvidenceDocument(final Long evidenceDocumentId) {

    EvidenceDocumentDetail evidenceDocumentDetail =
        evidenceService.getEvidenceDocument(evidenceDocumentId);

    return new ResponseEntity<>(evidenceDocumentDetail, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> createEvidenceDocument(
      final String caabUserLoginId,
      final EvidenceDocumentDetail evidenceDocumentDetail) {
    Long evidenceDocumentId = evidenceService.createEvidenceDocument(evidenceDocumentDetail);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(evidenceDocumentId)
        .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(uri);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> removeEvidenceDocument(
      final Long evidenceDocumentId, final String caabUserLoginId) {
    evidenceService.removeEvidenceDocument(evidenceDocumentId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> removeEvidenceDocuments(
      final String caabUserLoginId,
      final String applicationOrOutcomeId,
      final String caseReferenceNumber,
      final String providerId,
      final String documentType,
      final String ccmsModule,
      final Boolean transferPending) {

    evidenceService.removeEvidenceDocuments(
        applicationOrOutcomeId,
        caseReferenceNumber,
        providerId,
        documentType,
        ccmsModule,
        transferPending);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
