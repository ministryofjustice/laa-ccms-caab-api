package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.laa.ccms.caab.api.controller.ProceedingController;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;

public class ProceedingControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private ProceedingController proceedingController;

  @Autowired
  private ApplicationService applicationService;


  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql"})
  public void updateProceeding() throws IOException {
    Long proceedingId = 2L;

    ProceedingDetail updatedProceeding = loadObjectFromJson("/json/proceeding_new.json", ProceedingDetail.class);

    ResponseEntity<Void> response = proceedingController.updateProceeding(proceedingId, caabUserLoginId, updatedProceeding);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql"})
  public void deleteProceedings() {
    Long caseRef = 41L;
    Long proceedingRef = 2L;

    proceedingController.removeProceeding(proceedingRef, caabUserLoginId);
    List<ProceedingDetail> proceedings = applicationService.getProceedingsForApplication(caseRef);
    assertEquals(0, proceedings.size());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql"})
  public void addScopeLimitationToProceeding() throws IOException {
    Long proceedingId = 2L;

    ScopeLimitationDetail scopeLimitation = loadObjectFromJson("/json/scope_limitation_new.json", ScopeLimitationDetail.class);

    ResponseEntity<Void> response = proceedingController.addProceedingScopeLimitation(proceedingId, caabUserLoginId, scopeLimitation);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }


  @Test
  @Sql(scripts = {
      "/sql/application_insert.sql",
      "/sql/proceeding_insert.sql",
      "/sql/scope_limitation_insert.sql"})
  public void getScopeLimitationsForProceeding() {
    Long proceedingId = 2L;

    ResponseEntity<List<ScopeLimitationDetail>> responseEntity = proceedingController.getProceedingsScopeLimitations(proceedingId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<ScopeLimitationDetail> scopeLimitations = responseEntity.getBody();
    assertFalse(scopeLimitations.isEmpty());
  }
}
