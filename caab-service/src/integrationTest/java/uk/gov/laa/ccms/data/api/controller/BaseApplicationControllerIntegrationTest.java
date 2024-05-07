package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.laa.ccms.caab.api.controller.ApplicationController;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.BaseApplicationDetail;
import uk.gov.laa.ccms.caab.model.BaseClientDetail;
import uk.gov.laa.ccms.caab.model.LinkedCaseDetail;
import uk.gov.laa.ccms.caab.model.OpponentDetail;
import uk.gov.laa.ccms.caab.model.PriorAuthorityDetail;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;
import uk.gov.laa.ccms.caab.model.ReferenceDataItemDetail;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;

public abstract class BaseApplicationControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private ApplicationController applicationController;

  @Autowired
  private ApplicationService applicationService;


  private static Stream<Arguments> createApplicationUpdateArguments() {
    return Stream.of(
        Arguments.of("/json/applicationUpdate_caseReferenceNumber.json", "getCaseReferenceNumber", "caseReferenceNumber"),
        Arguments.of("/json/applicationUpdate_clientFirstName.json", "getClient.getFirstName", "client"),
        Arguments.of("/json/applicationUpdate_costLimit.json", "getCostLimit.getLimitAtTimeOfMerits", "costLimit"),
        Arguments.of("/json/applicationUpdate_applicationType.json", "getApplicationType.getId", "applicationType"),
        Arguments.of("/json/applicationUpdate_categoryOfLaw.json", "getCategoryOfLaw.getId", "categoryOfLaw"),
        Arguments.of("/json/applicationUpdate_correspondenceAddress.json", "getCorrespondenceAddress.getPostcode", "correspondenceAddress"),
        Arguments.of("/json/applicationUpdate_providerContact.json", "getProviderDetails.getProviderContact.getId", "providerDetails"),
        Arguments.of("/json/applicationUpdate_status.json", "getStatus.getId", "status")
    );
  }


  @ParameterizedTest
  @MethodSource("createApplicationUpdateArguments")
  @Sql(scripts = "/sql/application_insert.sql")
  public void testUpdateApplication(String fileInput, String methodCall, String fieldToIgnore)
      throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    ApplicationDetail patchedApplicationDetails = loadObjectFromJson(
        fileInput, ApplicationDetail.class);

    final Long id = 21L;

    //get the application before doing the update
    ApplicationDetail beforeUpdateApplication = applicationService.getApplication(id);

    String auditUser = "audit@user.com";
    ResponseEntity<Void> responseEntity =
        applicationController.updateApplication(id, auditUser, patchedApplicationDetails);

    assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());

    //get the application after doing the update
    ApplicationDetail afterUpdateApplication = applicationService.getApplication(id);

    // Split the methodCall string and iterate over it to get the final value
    String[] methodCalls = methodCall.split("\\.");
    Object expectedVariable = getObjectToCheckFromMethod(patchedApplicationDetails, methodCalls);
    Object actualVariable = getObjectToCheckFromMethod(afterUpdateApplication, methodCalls);
    assertEquals(expectedVariable, actualVariable);

    //check other variables are not changed
    assertTrue(areAllFieldsEqual(
        beforeUpdateApplication,
        afterUpdateApplication,
        ApplicationDetail.class, List.of(fieldToIgnore, "auditTrail")));
  }


  /**
   * Arguments for the testCreateApplication test method.
   */
  private static Stream<Arguments> createApplicationArguments() {
    return Stream.of(
        Arguments.of("/json/applicationDetail_base.json",
            "getAuditTrail",
            List.of("auditTrail")),
        Arguments.of("/json/applicationDetail_costStructure.json",
            "getCosts.getAuditTrail",
            List.of("costs.auditTrail", "auditTrail")),
        Arguments.of("/json/applicationDetail_costEntries.json",
            "getCosts.getCostEntries.getAuditTrail",
            List.of("costs.costEntries.auditTrail","costs.auditTrail", "auditTrail")),
        Arguments.of("/json/applicationDetail_correspondenceAddress.json",
            "getCorrespondenceAddress.getAuditTrail",
            List.of("correspondenceAddress.auditTrail", "auditTrail")),
        Arguments.of("/json/applicationDetail_proceedings.json",
            "getProceedings.getAuditTrail",
            List.of("proceedings.auditTrail", "auditTrail")),
        Arguments.of("/json/applicationDetail_scopeLimitation.json",
            "getProceedings.getScopeLimitations.getAuditTrail",
            List.of("proceedings.scopeLimitations.auditTrail","proceedings.auditTrail", "auditTrail")),
        Arguments.of("/json/applicationDetail_linkedCase.json",
            "getLinkedCases.getAuditTrail",
            List.of("linkedCases.auditTrail", "auditTrail")),
        Arguments.of("/json/applicationDetail_priorAuthority.json",
            "getPriorAuthorities.getAuditTrail",
            List.of("priorAuthorities.auditTrail", "auditTrail")),

        //No Audit data for reference data items
        Arguments.of("/json/applicationDetail_referenceDataItem.json",
            "getPriorAuthorities.getAuditTrail",
            List.of("priorAuthorities.auditTrail", "auditTrail")),
        Arguments.of("/json/applicationDetail_opponent.json",
            "getOpponents.getAuditTrail",
            List.of("opponents.auditTrail", "auditTrail")),
        Arguments.of("/json/applicationDetail_opponentAddress.json",
            "getOpponents.getAddress.getAuditTrail",
            List.of("opponents.address.auditTrail","opponents.auditTrail", "auditTrail"))
    );
  }

  /**
   * Parameterized test to create an application and verify that the audit trail and non-transient
   * variables are set correctly.
   *
   * @param fileInput The path to the JSON file to load
   * @param auditTrailMethod The method to call to get the audit trail object
   *                         (e.g. "getAuditTrail" or "getCosts.getAuditTrail")
   * @param auditTrailsToNull A list of audit trails to set to null
   *                          (e.g. "auditTrail" or "costs.auditTrail")
   */
  @ParameterizedTest
  @MethodSource("createApplicationArguments")
  public void testCreateApplication(String fileInput, String auditTrailMethod, List<String> auditTrailsToNull)
      throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException,
      NoSuchFieldException {
    // Load and parse the JSON file into yourApplicationDetailObject
    ApplicationDetail applicationDetail = loadObjectFromJson(
        fileInput, ApplicationDetail.class);

    String caseReference = generateTestCaseRef();
    applicationDetail.setCaseReferenceNumber(caseReference);

    String auditUser = "audit@user.com";
    // Call the createApplication method directly
    ResponseEntity<Void> responseEntity =
        applicationController.createApplication(auditUser, applicationDetail);

    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    URI locationUri = responseEntity.getHeaders().getLocation();

    String path = locationUri.getPath();
    String id = path.substring(path.lastIndexOf('/') + 1);

    ApplicationDetail savedApplicationDetails = applicationService.getApplication(Long.valueOf(id));

    assertAuditTrail(savedApplicationDetails, auditTrailMethod, auditUser);
    setAuditPropertiesToNull(savedApplicationDetails, auditTrailsToNull);

    // null/ignore the ids as theses are generated by the database
    nullModelIds(savedApplicationDetails);

    updateExpectedBooleans(applicationDetail);

    assertEquals(applicationDetail, savedApplicationDetails);
  }

  private void updateExpectedBooleans(ApplicationDetail applicationDetail) {
    applicationDetail.setAmendment(
        applicationDetail.getAmendment() == null
            ? Boolean.FALSE : applicationDetail.getAmendment());
    applicationDetail.setAppMode(
        applicationDetail.getAppMode() == null
            ? Boolean.FALSE : applicationDetail.getAppMode());
    applicationDetail.setAward(
        applicationDetail.getAward() == null
            ? Boolean.FALSE : applicationDetail.getAward());
    applicationDetail.setEditProceedingsAndCostsAllowed(
        applicationDetail.getEditProceedingsAndCostsAllowed() == null
        ? Boolean.FALSE : applicationDetail.getEditProceedingsAndCostsAllowed());
    applicationDetail.setLarScopeFlag(applicationDetail.getLarScopeFlag() == null
        ? Boolean.FALSE : applicationDetail.getLarScopeFlag());
    applicationDetail.setLeadProceedingChanged(applicationDetail.getLeadProceedingChanged() == null
        ? Boolean.FALSE : applicationDetail.getLeadProceedingChanged());
    applicationDetail.setMeansAssessmentAmended(applicationDetail.getMeansAssessmentAmended() == null
        ? Boolean.FALSE : applicationDetail.getMeansAssessmentAmended());
    applicationDetail.setMeritsAssessmentAmended(applicationDetail.getMeritsAssessmentAmended() == null
        ? Boolean.FALSE : applicationDetail.getMeritsAssessmentAmended());
    applicationDetail.setMeritsReassessmentRequired(
        applicationDetail.getMeritsReassessmentRequired() == null
        ? Boolean.FALSE : applicationDetail.getMeritsReassessmentRequired());
    applicationDetail.setOpponentAppliedForFunding(
        applicationDetail.getOpponentAppliedForFunding() == null
        ? Boolean.FALSE : applicationDetail.getOpponentAppliedForFunding());
  }

  private void nullModelIds(ApplicationDetail applicationDetail) {
    applicationDetail.setId(null);

    if (applicationDetail.getCorrespondenceAddress() != null) {
      applicationDetail.getCorrespondenceAddress().setId(null);
    }

    if (applicationDetail.getLinkedCases() != null) {
      for (LinkedCaseDetail linkedCase : applicationDetail.getLinkedCases()) {
        linkedCase.setId(null);
      }
    }

    if (applicationDetail.getProceedings() != null) {
      for (ProceedingDetail proceeding : applicationDetail.getProceedings()) {
        proceeding.setId(null);
        if (proceeding.getScopeLimitations() != null) {
          for (ScopeLimitationDetail scopeLimitation : proceeding.getScopeLimitations()) {
            scopeLimitation.setId(null);
          }
        }
      }
    }

    if (applicationDetail.getPriorAuthorities() != null) {
      for (PriorAuthorityDetail priorAuthority : applicationDetail.getPriorAuthorities()) {
        priorAuthority.setId(null);
        if (priorAuthority.getItems() != null) {
          for (ReferenceDataItemDetail referenceDataItem : priorAuthority.getItems()) {
            referenceDataItem.setId(null);
          }
        }
      }
    }

    if (applicationDetail.getOpponents() != null) {
      for (OpponentDetail opponent : applicationDetail.getOpponents()) {
        opponent.setId(null);

        if (opponent.getAddress() != null) {
          opponent.getAddress().setId(null);
        }
      }
    }
  }

  /**
   * Test to query for applications by casereference only
   *
   */
  @Test
  @Sql(scripts = "/sql/application_insert.sql")
  public void testGetApplications_caseref_returnsdata() {
    String caseRef = "300001644516";
    String providerId = "26517";
    String provCaseRef = "329635";
    String clientSurname = "Payne";
    String clientRef = "PhilTest";
    Integer officeId = 145512;
    String feeEarner = "2027148";
    String status = "UNSUBMITTED";

    // Call the getApplications method directly
    ResponseEntity<ApplicationDetails> responseEntity = applicationController.getApplications(
        caseRef,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        Pageable.unpaged());

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(1, responseEntity.getBody().getSize());
    BaseApplicationDetail result = responseEntity.getBody().getContent().get(0);
    assertEquals(caseRef, result.getCaseReferenceNumber());
    assertEquals(Integer.parseInt(providerId), result.getProviderDetails().getProvider().getId());
    assertEquals(provCaseRef, result.getProviderDetails().getProviderCaseReference());
    assertEquals(clientSurname, result.getClient().getSurname());
    assertEquals(clientRef, result.getClient().getReference());
    assertEquals(feeEarner, result.getProviderDetails().getFeeEarner().getId());
    assertEquals(officeId, result.getProviderDetails().getOffice().getId());
    assertEquals(status, result.getStatus().getId());
  }

  /**
   * Test to query for applications using all criteria
   *
   */
  @Test
  @Sql(scripts = "/sql/application_insert.sql")
  public void testGetApplications_allfields_returnsdata() {
    String caseRef = "300001644516";
    String providerId = "26517";
    String provCaseRef = "329635";
    String clientSurname = "Payne";
    String clientRef = "PhilTest";
    Integer officeId = 145512;
    String feeEarner = "2027148";
    String status = "UNSUBMITTED";

    // Call the getApplications method directly
    ResponseEntity<ApplicationDetails> responseEntity = applicationController.getApplications(
        caseRef,
        providerId,
        provCaseRef,
        clientSurname,
        clientRef,
        feeEarner,
        officeId,
        status,
        Pageable.unpaged());

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(1, responseEntity.getBody().getSize());
    BaseApplicationDetail result = responseEntity.getBody().getContent().get(0);
    assertEquals(caseRef, result.getCaseReferenceNumber());
    assertEquals(Integer.parseInt(providerId), result.getProviderDetails().getProvider().getId());
    assertEquals(provCaseRef, result.getProviderDetails().getProviderCaseReference());
    assertEquals(clientSurname, result.getClient().getSurname());
    assertEquals(clientRef, result.getClient().getReference());
    assertEquals(feeEarner, result.getProviderDetails().getFeeEarner().getId());
    assertEquals(officeId, result.getProviderDetails().getOffice().getId());
    assertEquals(status, result.getStatus().getId());
  }

  /**
   * Test to query for applications and verify no results are returned
   *
   */
  @Test
  @Sql(scripts = "/sql/application_insert.sql")
  public void testGetApplications_allfields_nomatch() {
    String caseRef = "unknown";
    String providerId = "26517";
    String provCaseRef = "329635";
    String clientSurname = "Payne";
    String clientRef = "PhilTest";
    Integer officeId = 145512;
    String feeEarner = "2027148";
    String status = "UNSUBMITTED";

    // Call the getApplications method directly
    ResponseEntity<ApplicationDetails> responseEntity = applicationController.getApplications(
        caseRef,
        providerId,
        provCaseRef,
        clientSurname,
        clientRef,
        feeEarner,
        officeId,
        status,
        Pageable.unpaged());

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(0, responseEntity.getBody().getSize());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql"})
  public void addApplicationProceedings() throws IOException {
    Long caseRef = 41L;

    ProceedingDetail proceeding = loadObjectFromJson("/json/proceeding_new.json", ProceedingDetail.class);
    ResponseEntity<Void> response = applicationController.addApplicationProceeding(
        caseRef, caabUserLoginId, proceeding);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    ResponseEntity<List<ProceedingDetail>> responseEntity =
        applicationController.getApplicationProceedings(caseRef);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<ProceedingDetail> proceedings = responseEntity.getBody();
    assertEquals(1, proceedings.size());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/proceeding_insert.sql"})
  public void getApplicationProceedings() {
    Long caseRef = 41L;

    ResponseEntity<List<ProceedingDetail>> responseEntity = applicationController.getApplicationProceedings(caseRef);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<ProceedingDetail> proceedings = responseEntity.getBody();
    assertEquals(1, proceedings.size());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql"})
  public void addLinkedCase() throws IOException {
    Long caseRef = 41L; // Assuming this is the ID of an application

    LinkedCaseDetail newLinkedCase = loadObjectFromJson("/json/linked_cases_new.json", LinkedCaseDetail.class);

    ResponseEntity<Void> response =
        applicationController.addApplicationLinkedCase(caseRef, caabUserLoginId,
            newLinkedCase);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    ResponseEntity<List<LinkedCaseDetail>> responseEntity =
        applicationController.getApplicationLinkedCases(caseRef);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<LinkedCaseDetail> linkedCases = responseEntity.getBody();
    assertEquals(1, linkedCases.size());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/linked_cases_insert.sql"})
  public void getLinkedCase() {
    Long caseRef = 41L;

    ResponseEntity<List<LinkedCaseDetail>> responseEntity = applicationController.getApplicationLinkedCases(caseRef);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<LinkedCaseDetail> linkedCases = responseEntity.getBody();
    assertEquals(1, linkedCases.size());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql"})
  public void addPriorAuthority() throws IOException {
    Long caseRef = 41L;

    PriorAuthorityDetail
        priorAuthority = loadObjectFromJson("/json/prior_authority_new.json", PriorAuthorityDetail.class);

    ResponseEntity<Void> response = applicationController.addApplicationPriorAuthority(caseRef, caabUserLoginId, priorAuthority);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/prior_authority_insert.sql"})
  public void getPriorAuthority() {
    Long caseRef = 41L;

    ResponseEntity<List<PriorAuthorityDetail>> responseEntity = applicationController.getApplicationPriorAuthorities(caseRef);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<PriorAuthorityDetail> priorAuthorities = responseEntity.getBody();
    assertEquals(1, priorAuthorities.size());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql"})
  public void updateClient() throws IOException {
    Long caseRef = 41L;
    String clientReferenceId = "62595640";

    BaseClientDetail baseClient = loadObjectFromJson("/json/base_client_new.json", BaseClientDetail.class);

    ResponseEntity<Void> response = applicationController.updateApplicationClient(clientReferenceId,caabUserLoginId, baseClient);
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    ResponseEntity<ApplicationDetail> responseEntity = applicationController.getApplication(caseRef);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    ApplicationDetail applicationDetail = responseEntity.getBody();
    assertEquals(clientReferenceId, applicationDetail.getClient().getReference());
    assertEquals(baseClient.getFirstName(), applicationDetail.getClient().getFirstName());
    assertEquals(baseClient.getSurname(), applicationDetail.getClient().getSurname());
  }




  @Test
  @Sql(scripts = {"/sql/application_insert.sql",})
  public void addOpponentToApplication() throws IOException {
    Long applicationId = 41L;

    OpponentDetail opponent = loadObjectFromJson("/json/opponent_new.json", OpponentDetail.class);

    ResponseEntity<Void> response = applicationController.addApplicationOpponent(applicationId, caabUserLoginId, opponent);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Test
  @Sql(scripts = {
      "/sql/application_insert.sql",
      "/sql/opponent_insert.sql"})
  public void getOpponentsForApplication() {
    Long applicationId = 41L;

    ResponseEntity<List<OpponentDetail>> responseEntity = applicationController.getApplicationOpponents(applicationId);

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    assertNotNull(responseEntity.getBody());

    List<OpponentDetail> opponents = responseEntity.getBody();
    assertFalse(opponents.isEmpty());
  }

}
