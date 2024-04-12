package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.laa.ccms.caab.api.controller.ScopeLimitationController;
import uk.gov.laa.ccms.caab.api.service.ProceedingService;
import uk.gov.laa.ccms.caab.model.ScopeLimitation;

public abstract class BaseScopeLimitiationControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private ScopeLimitationController scopeLimitationController;

  @Autowired
  private ProceedingService proceedingService;

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql", "/sql/scope_limitation_insert.sql"})
  public void updateScopeLimitation() throws IOException {
    Long scopeLimitationId = 3L;

    ScopeLimitation updatedScopeLimitation = loadObjectFromJson("/json/scope_limitation_new.json", ScopeLimitation.class);

    ResponseEntity<Void> response = scopeLimitationController.updateScopeLimitation(scopeLimitationId, caabUserLoginId, updatedScopeLimitation);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql", "/sql/scope_limitation_insert.sql"})
  public void deleteScopeLimitation() {
    Long proceedingId = 2L;
    Long scopeLimitationId = 3L;

    scopeLimitationController.removeScopeLimitation(scopeLimitationId, caabUserLoginId);
    List<ScopeLimitation> scopeLimitations = proceedingService.getScopeLimitationsForProceeding(proceedingId);
    assertEquals(0, scopeLimitations.size());
  }

}
