package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.laa.ccms.caab.api.controller.EvidenceController;
import uk.gov.laa.ccms.caab.api.service.EvidenceService;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetails;

public abstract class BaseEvidenceControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private EvidenceController evidenceController;

  @Autowired
  private EvidenceService evidenceService;

  @Test
  public void testCreateEvidenceDocument() throws Exception {

    EvidenceDocumentDetail evidenceDocumentDetail = loadObjectFromJson(
        "/json/evidence_document_new.json", EvidenceDocumentDetail.class);

    String auditUser = "audit@user.com";

    // Call the createEvidenceDocument method directly
    ResponseEntity<Void> responseEntity =
        evidenceController.createEvidenceDocument(auditUser, evidenceDocumentDetail);

    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    URI locationUri = responseEntity.getHeaders().getLocation();

    String path = locationUri.getPath();
    String id = path.substring(path.lastIndexOf('/') + 1);

    EvidenceDocumentDetail savedEvidenceDocumentDetails =
        evidenceService.getEvidenceDocument(Long.valueOf(id));

    assertAuditTrail(savedEvidenceDocumentDetails.getAuditTrail(), auditUser);

    // Clear the audit trail for comparison purposes.
    savedEvidenceDocumentDetails.setAuditTrail(null);

    // null/ignore the ids as theses are generated by the database
    savedEvidenceDocumentDetails.setId(null);

    assertEquals(evidenceDocumentDetail, savedEvidenceDocumentDetails);
  }

  @Test
  @Sql(scripts = "/sql/evidence_document_insert.sql")
  public void testGetEvidenceDocuments_byApplicationOrOutcomeId() {

    // Call the getEvidenceDocuments method directly
    ResponseEntity<EvidenceDocumentDetails> responseEntity =
        evidenceController.getEvidenceDocuments(
            "appId",
            null,
            null,
            null,
            null,
            null,
            Pageable.unpaged()
        );

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(2, responseEntity.getBody().getContent().size());

    EvidenceDocumentDetail retrievedEvidenceDocumentDetails =
        evidenceService.getEvidenceDocument(3L);

    assertEquals(retrievedEvidenceDocumentDetails, responseEntity.getBody().getContent().get(0));
  }

  @Test
  @Sql(scripts = "/sql/evidence_document_insert.sql")
  public void testGetEvidenceDocuments_usingAllFields() {

    // Call the getEvidenceDocuments method directly
    ResponseEntity<EvidenceDocumentDetails> responseEntity =
        evidenceController.getEvidenceDocuments(
            "appId",
            "caseref",
            "provid",
            "doctype",
            "B",
            null,
            Pageable.unpaged()
        );

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(1, responseEntity.getBody().getContent().size());

    EvidenceDocumentDetail retrievedEvidenceDocumentDetails =
        evidenceService.getEvidenceDocument(3L);

    assertEquals(retrievedEvidenceDocumentDetails, responseEntity.getBody().getContent().get(0));
  }

  @Test
  @Sql(scripts = "/sql/evidence_document_insert.sql")
  public void testGetEvidenceDocuments_transferPending_filtersCorrectly() {

    // Call the getEvidenceDocuments method directly
    ResponseEntity<EvidenceDocumentDetails> responseEntity =
        evidenceController.getEvidenceDocuments(
            "appId",
            null,
            null,
            null,
            null,
            Boolean.TRUE,
            Pageable.unpaged()
        );

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(1, responseEntity.getBody().getContent().size());

    EvidenceDocumentDetail retrievedEvidenceDocumentDetails =
        evidenceService.getEvidenceDocument(4L);

    assertEquals(retrievedEvidenceDocumentDetails, responseEntity.getBody().getContent().get(0));
  }

  @Test
  @Sql(scripts = "/sql/evidence_document_insert.sql")
  public void testGetEvidenceDocument() {

    // Call the getEvidenceDocument method directly
    ResponseEntity<EvidenceDocumentDetail> responseEntity =
        evidenceController.getEvidenceDocument(3L);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertEquals(3, responseEntity.getBody().getId());
  }

  @Test
  @Sql(scripts = "/sql/evidence_document_insert.sql")
  public void testRemoveEvidenceDocument() {

    // Call the removeEvidenceDocuments method directly
    ResponseEntity<Void> responseEntity =
        evidenceController.removeEvidenceDocument(3L, caabUserLoginId);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    EvidenceDocumentDetails queriedDocuments =
        evidenceController.getEvidenceDocuments(
            "appId",
            "caseref",
            "provid",
            "doctype",
            "B",
            null,
            Pageable.unpaged()
        ).getBody();

    assertEquals(0, queriedDocuments.getContent().size());
  }

  @Test
  @Sql(scripts = "/sql/evidence_document_insert.sql")
  public void testRemoveEvidenceDocuments_usingAllFields() {

    // Call the removeEvidenceDocuments method directly
    ResponseEntity<Void> responseEntity =
        evidenceController.removeEvidenceDocuments(
            "Caab-user-login-id",
            "appId",
            "caseref",
            "provid",
            "doctype",
            "B",
            null
        );

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    // Check that only 1 of the EvidenceDocuments (with ccms module B) has been deleted.
    EvidenceDocumentDetails queriedDocuments =
        evidenceController.getEvidenceDocuments(
            "appId",
            "caseref",
            "provid",
            "doctype",
            null,
            null,
            Pageable.unpaged()
        ).getBody();

    assertEquals(1, queriedDocuments.getContent().size());
  }

  @Test
  @Sql(scripts = "/sql/evidence_document_insert.sql")
  public void testRemoveEvidenceDocuments_byCaseRef() {

    // Call the removeEvidenceDocuments method directly
    ResponseEntity<Void> responseEntity =
        evidenceController.removeEvidenceDocuments(
            "Caab-user-login-id",
            null,
            "caseref",
            null,
            null,
            null,
            null
        );

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    EvidenceDocumentDetails queriedDocuments =
        evidenceController.getEvidenceDocuments(
            null,
            "caseref",
            null,
            null,
            null,
            null,
            Pageable.unpaged()
        ).getBody();

    assertEquals(0, queriedDocuments.getContent().size());
  }

  @Test
  @Sql(scripts = "/sql/evidence_document_insert.sql")
  public void testRemoveEvidenceDocuments_byCaseRef_transferPending_filtersCorrectly() {

    // Call the removeEvidenceDocuments method directly
    ResponseEntity<Void> responseEntity =
        evidenceController.removeEvidenceDocuments(
            "Caab-user-login-id",
            null,
            "caseref",
            null,
            null,
            null,
            Boolean.TRUE
        );

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    EvidenceDocumentDetails queriedDocuments =
        evidenceController.getEvidenceDocuments(
            null,
            "caseref",
            null,
            null,
            null,
            null,
            Pageable.unpaged()
        ).getBody();

    assertEquals(1, queriedDocuments.getContent().size());
  }
}
