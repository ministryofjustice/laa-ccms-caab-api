package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.laa.ccms.caab.api.controller.LinkedCaseController;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.LinkedCase;

public abstract class BaseLinkedCaseControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private LinkedCaseController linkedCaseController;

  @Autowired
  private ApplicationService applicationService;

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/linked_cases_insert.sql"})
  public void updateLinkedCase() throws Exception {
    Long caseRef = 41L;
    Long linkedCaseRef = 2L;

    LinkedCase linkedCase = loadObjectFromJson("/json/linked_cases_new.json", LinkedCase.class);
    linkedCase.setId(linkedCaseRef.intValue());

    linkedCaseController.updateLinkedCase(linkedCaseRef, caabUserLoginId, linkedCase);

    List<LinkedCase> linkedCases = applicationService.getLinkedCasesForApplication(caseRef);

    assertEquals(1, linkedCases.size());

    // Remove the audittrail for comparison purposes.
    LinkedCase retrievedLinkedCase = linkedCases.get(0);
    retrievedLinkedCase.setAuditTrail(null);

    assertEquals(linkedCase, retrievedLinkedCase);
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/linked_cases_insert.sql"})
  public void deleteLinkedCase() {
    Long caseRef = 41L;
    Long linkedCaseRef = 2L;

    linkedCaseController.removeLinkedCase(linkedCaseRef, caabUserLoginId);
    List<LinkedCase> linkedCases = applicationService.getLinkedCasesForApplication(caseRef);
    assertEquals(0, linkedCases.size());
  }
}
