package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.laa.ccms.caab.api.controller.PriorAuthorityController;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.PriorAuthorityDetail;

public class PriorAuthorityControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private PriorAuthorityController priorAuthorityController;

  @Autowired
  private ApplicationService applicationService;


  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/prior_authority_insert.sql"})
  public void updatePriorAuthority() throws IOException {
    Long priorAuthorityId = 2L;

    PriorAuthorityDetail updatedPriorAuthority = loadObjectFromJson("/json/prior_authority_new.json", PriorAuthorityDetail.class);

    ResponseEntity<Void> response = priorAuthorityController.updatePriorAuthority(priorAuthorityId, caabUserLoginId, updatedPriorAuthority);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/prior_authority_insert.sql"})
  public void deletePriorAuthority() {
    Long caseRef = 41L;
    Long priorAuthorityRef = 2L;

    priorAuthorityController.removePriorAuthority(priorAuthorityRef, caabUserLoginId);
    List<PriorAuthorityDetail> priorAuthorities = applicationService.getPriorAuthoritiesForApplication(caseRef);
    assertEquals(0, priorAuthorities.size());
  }
}
