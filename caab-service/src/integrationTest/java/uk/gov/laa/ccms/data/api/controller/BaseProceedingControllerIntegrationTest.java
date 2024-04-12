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
import uk.gov.laa.ccms.caab.model.Proceeding;
import uk.gov.laa.ccms.caab.model.ScopeLimitation;

public abstract class BaseProceedingControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private ProceedingController proceedingController;

  @Autowired
  private ApplicationService applicationService;


  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql"})
  public void updateProceeding() throws IOException {
    Long proceedingId = 2L;

    Proceeding updatedProceeding = loadObjectFromJson("/json/proceeding_new.json", Proceeding.class);

    ResponseEntity<Void> response = proceedingController.updateProceeding(proceedingId, caabUserLoginId, updatedProceeding);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql"})
  public void deleteProceedings() {
    Long caseRef = 41L;
    Long proceedingRef = 2L;

    proceedingController.removeProceeding(proceedingRef, caabUserLoginId);
    List<Proceeding> proceedings = applicationService.getProceedingsForApplication(caseRef);
    assertEquals(0, proceedings.size());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql"})
  public void addScopeLimitationToProceeding() throws IOException {
    Long proceedingId = 2L;

    ScopeLimitation scopeLimitation = loadObjectFromJson("/json/scope_limitation_new.json", ScopeLimitation.class);

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

    ResponseEntity<List<ScopeLimitation>> responseEntity = proceedingController.getProceedingsScopeLimitations(proceedingId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<ScopeLimitation> scopeLimitations = responseEntity.getBody();
    assertFalse(scopeLimitations.isEmpty());
  }
}
