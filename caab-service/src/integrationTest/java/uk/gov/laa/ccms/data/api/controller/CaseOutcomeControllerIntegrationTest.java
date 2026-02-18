package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import uk.gov.laa.ccms.caab.api.controller.CaseOutcomeController;
import uk.gov.laa.ccms.caab.api.service.CaseOutcomeService;
import uk.gov.laa.ccms.caab.model.BaseAwardDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetails;
import uk.gov.laa.ccms.caab.model.CostAwardDetail;
import uk.gov.laa.ccms.caab.model.FinancialAwardDetail;
import uk.gov.laa.ccms.caab.model.LandAwardDetail;
import uk.gov.laa.ccms.caab.model.OtherAssetAwardDetail;

public class CaseOutcomeControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private CaseOutcomeController caseOutcomeController;

  @Autowired
  private CaseOutcomeService caseOutcomeService;

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/opponent_insert.sql","/sql/case_outcome_insert.sql"})
  public void testGetCaseOutcomes_byCaseReferenceNumber() {

    ResponseEntity<CaseOutcomeDetails> responseEntity =
        caseOutcomeController.getCaseOutcomes(
            "caseRef",
            null);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(2, responseEntity.getBody().getContent().size());

    CaseOutcomeDetail retrievedCaseOutcomeDetail =
        caseOutcomeService.getCaseOutcome(1L);

    assertEquals(retrievedCaseOutcomeDetail, responseEntity.getBody().getContent().get(0));
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/opponent_insert.sql","/sql/case_outcome_insert.sql"})
  public void testGetCaseOutcomes_byCaseReferenceNumberAndProviderId_queriesCorrectly() {

    ResponseEntity<CaseOutcomeDetails> responseEntity =
        caseOutcomeController.getCaseOutcomes(
            "caseRef",
            "providerId2");

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(1, responseEntity.getBody().getContent().size());

    CaseOutcomeDetail retrievedCaseOutcomeDetail =
        caseOutcomeService.getCaseOutcome(2L);

    assertEquals(retrievedCaseOutcomeDetail, responseEntity.getBody().getContent().get(0));
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/opponent_insert.sql","/sql/case_outcome_insert.sql"})
  public void testGetCaseOutcomes_unknownCaseReferenceNumber_queriesCorrectly() {

    ResponseEntity<CaseOutcomeDetails> responseEntity =
        caseOutcomeController.getCaseOutcomes(
            "nonsense",
            null);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertTrue(responseEntity.getBody().getContent().isEmpty());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/opponent_insert.sql"})
  public void testCreateCaseOutcome() throws Exception {

    CaseOutcomeDetail caseOutcomeDetail = loadObjectFromJson(
        "/json/case_outcome_new.json", CaseOutcomeDetail.class);

    EntityExchangeResult<Void> responseEntity = restClient.post()
        .uri("/case-outcomes").body(caseOutcomeDetail)
        .exchange()
        .expectStatus()
        .isCreated()
        .returnResult(Void.class);

    URI locationUri = responseEntity.getResponseHeaders().getLocation();

    String path = locationUri.getPath();
    String id = path.substring(path.lastIndexOf('/') + 1);

    CaseOutcomeDetail savedCaseOutcomeDetail =
        caseOutcomeService.getCaseOutcome(Long.valueOf(id));

    assertAuditTrail(savedCaseOutcomeDetail.getAuditTrail(), caabUserLoginId);

    // Clear the generated id and audit trail for comparison purposes.
    savedCaseOutcomeDetail.setId(null);
    savedCaseOutcomeDetail.setAuditTrail(null);
    savedCaseOutcomeDetail.getCostAwards().forEach(this::clearIdAndAudit);
    savedCaseOutcomeDetail.getFinancialAwards().forEach(this::clearIdAndAudit);
    savedCaseOutcomeDetail.getLandAwards().forEach(this::clearIdAndAudit);
    savedCaseOutcomeDetail.getOtherAssetAwards().forEach(this::clearIdAndAudit);
    savedCaseOutcomeDetail.getProceedingOutcomes().forEach(proceedingOutcomeDetail -> {
      proceedingOutcomeDetail.setId(null);
      proceedingOutcomeDetail.setAuditTrail(null);
    });

    // null/ignore the ids as theses are generated by the database

    assertEquals(caseOutcomeDetail, savedCaseOutcomeDetail);
  }

  private void clearIdAndAudit(BaseAwardDetail awardDetail) {
    awardDetail.setId(null);
    awardDetail.setAuditTrail(null);
    awardDetail.getLiableParties().forEach(liablePartyDetail -> {
      liablePartyDetail.setId(null);
      liablePartyDetail.setAuditTrail(null);
    });

    if (awardDetail instanceof CostAwardDetail costAwardDetail) {
      costAwardDetail.getRecovery().setId(null);
      costAwardDetail.getRecovery().setAuditTrail(null);
    }
    else if (awardDetail instanceof FinancialAwardDetail financialAwardDetail) {
      financialAwardDetail.getRecovery().setId(null);
      financialAwardDetail.getRecovery().setAuditTrail(null);
    } else if (awardDetail instanceof LandAwardDetail landAwardDetail) {
      landAwardDetail.getTimeRecovery().setId(null);
      landAwardDetail.getTimeRecovery().setAuditTrail(null);
    } else if (awardDetail instanceof OtherAssetAwardDetail otherAssetAwardDetail) {
      otherAssetAwardDetail.getTimeRecovery().setId(null);
      otherAssetAwardDetail.getTimeRecovery().setAuditTrail(null);
    }
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/opponent_insert.sql","/sql/case_outcome_insert.sql"})
  public void testRemoveCaseOutcome() {

    String auditUser = "audit@user.com";

    ResponseEntity<Void> responseEntity =
        caseOutcomeController.removeCaseOutcome(1L, auditUser);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    CaseOutcomeDetails queriedOutcomes =
        caseOutcomeService.getCaseOutcomes(
            "caseRef",
            null);

    // Check that only 1 record remains for the test caseReference
    assertEquals(1, queriedOutcomes.getContent().size());

  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/opponent_insert.sql","/sql/case_outcome_insert.sql"})
  public void testRemoveCaseOutcomes() {

    String auditUser = "audit@user.com";

    ResponseEntity<Void> responseEntity =
        caseOutcomeController.removeCaseOutcomes(auditUser, "caseRef", null);

    assertNotNull(responseEntity);
    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    CaseOutcomeDetails queriedOutcomes =
        caseOutcomeService.getCaseOutcomes(
            "caseRef",
            null);

    // Check that all records were removed for the test case reference.
    assertTrue(queriedOutcomes.getContent().isEmpty());

  }

}
