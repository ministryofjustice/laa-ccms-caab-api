package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;
import static uk.gov.laa.ccms.caab.api.audit.AuditorAwareImpl.currentUserHolder;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import uk.gov.laa.ccms.caab.api.CaabApiApplication;
import uk.gov.laa.ccms.caab.api.controller.ApplicationController;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.BaseApplication;
import uk.gov.laa.ccms.data.api.AbstractIntegrationTest;

@SpringBootTest(classes = CaabApiApplication.class)
@SqlMergeMode(MERGE)
//@ActiveProfiles("local")
//public class ApplicationControllerIntegrationTest {
@Sql(executionPhase = BEFORE_TEST_METHOD, scripts = "/sql/application_tables_create_schema.sql")
@Sql(executionPhase = AFTER_TEST_METHOD, scripts = "/sql/application_tables_drop_schema.sql")
public class ApplicationControllerIntegrationTest extends AbstractIntegrationTest {

  @Autowired
  private ApplicationController applicationController;

  @Autowired
  private ApplicationService applicationService;

  private final String caabUserLoginId = "audit@user.com";

  @BeforeEach
  public void setup() {
    currentUserHolder.set(caabUserLoginId);
  }

  /**
   * Loads the JSON file from the classpath and parses it into an ApplicationDetail object.
   *
   * @param jsonFilePath The path to the JSON file to load
   */
  private ApplicationDetail loadApplicationDetailFromJsonFile(String jsonFilePath) throws
      IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    ClassPathResource resource = new ClassPathResource(jsonFilePath);
    return objectMapper.readValue(resource.getInputStream(), ApplicationDetail.class);
  }

  /**
   * Generates a random 12-digit number string to be used as a case reference number.
   */
  private String generateTestCaseRef(){
    Random random = new Random();
    StringBuilder stringBuilder = new StringBuilder(12);

    for (int i = 0; i < 12; i++) {
      int digit = random.nextInt(10); // Generate a random digit (0-9)
      stringBuilder.append(digit);
    }

    return stringBuilder.toString();
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
    ApplicationDetail applicationDetail = loadApplicationDetailFromJsonFile(
        fileInput);

    String caseReference = generateTestCaseRef();
    applicationDetail.setCaseReferenceNumber(caseReference);

    String auditUser = "audit@user.com";
    // Call the createApplication method directly
    ResponseEntity<Void> responseEntity = applicationController.createApplication(auditUser, applicationDetail);

    assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    URI locationUri = responseEntity.getHeaders().getLocation();

    String path = locationUri.getPath();
    String id = path.substring(path.lastIndexOf('/') + 1);

    ApplicationDetail savedApplicationDetails = applicationService.getApplication(Long.valueOf(id));

    assertAuditTrail(savedApplicationDetails, auditTrailMethod, auditUser);
    setAuditPropertiesToNull(savedApplicationDetails, auditTrailsToNull);
    assertEquals(applicationDetail, savedApplicationDetails);
  }

  /**
   * Test to query for applications by casereference only
   *
   */
  @Test
  @Sql(scripts = "/sql/application_insert.sql")
  public void testGetApplications_caseref_returnsdata() {
    String caseRef = "300001644516";
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
        Pageable.unpaged());

    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

    assertNotNull(responseEntity.getBody());
    assertNotNull(responseEntity.getBody().getContent());
    assertEquals(1, responseEntity.getBody().getSize());
    BaseApplication result = responseEntity.getBody().getContent().get(0);
    assertEquals(caseRef, result.getCaseReferenceNumber());
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
    String provCaseRef = "329635";
    String clientSurname = "Payne";
    String clientRef = "PhilTest";
    Integer officeId = 145512;
    String feeEarner = "2027148";
    String status = "UNSUBMITTED";

    // Call the getApplications method directly
    ResponseEntity<ApplicationDetails> responseEntity = applicationController.getApplications(
        caseRef,
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
    BaseApplication result = responseEntity.getBody().getContent().get(0);
    assertEquals(caseRef, result.getCaseReferenceNumber());
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
    String provCaseRef = "329635";
    String clientSurname = "Payne";
    String clientRef = "PhilTest";
    Integer officeId = 145512;
    String feeEarner = "2027148";
    String status = "UNSUBMITTED";

    // Call the getApplications method directly
    ResponseEntity<ApplicationDetails> responseEntity = applicationController.getApplications(
        caseRef,
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

  /**
   * Assert that the audit trail is set correctly in the object. Uses reflection to navigate the
   * auditTrailMethod path and assert that the audit trail is set correctly.
   */
  private void assertAuditTrail(Object object, String auditMethod, String expectedAuditUser)
      throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
    String[] methodCalls = auditMethod.split("\\.");
    assertAuditTrailRecursive(object, methodCalls, 0, expectedAuditUser);
  }

  /**
   * Recursively navigate the object using the methodCalls array and assert that the audit trail is
   * set correctly.
   */
  private void assertAuditTrailRecursive(Object object, String[] methodCalls, int index, String expectedAuditUser)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    if (index >= methodCalls.length || object == null) {
      return;
    }

    Method method = object.getClass().getMethod(methodCalls[index]);
    object = method.invoke(object);

    if (object instanceof List<?> list) {
      for (Object item : list) {
        assertAuditTrailRecursive(item, methodCalls, index + 1, expectedAuditUser);
      }
    } else {
      if (index == methodCalls.length - 1) {
        assertAuditDetailsInObject(object, expectedAuditUser);
      } else {
        assertAuditTrailRecursive(object, methodCalls, index + 1, expectedAuditUser);
      }
    }
  }

  /**
   * Used to assert that the audit trail is set correctly in the object.
   *
   * @param object The object to check
   * @param expectedAuditUser The expected audit user
   */
  private void assertAuditDetailsInObject(Object object, String expectedAuditUser) {
    if (object instanceof AuditTrail auditTrail) {
      assertNotNull(auditTrail);
      assertEquals(expectedAuditUser, auditTrail.getCreatedBy());
      assertEquals(expectedAuditUser, auditTrail.getLastSavedBy());
      assertNotNull(auditTrail.getCreated());
      assertNotNull(auditTrail.getLastSaved());
    }
  }

  /**
   * Sets the audit trail properties to null in the object. Uses reflection to navigate the
   * list of auditTrailsToNull and set the audit trail to null.
   */
  private void setAuditPropertiesToNull(Object object, List<String> auditTrailsToNull) throws NoSuchFieldException, IllegalAccessException {
    if (object == null || auditTrailsToNull == null) {
      return;
    }
    for (String trail : auditTrailsToNull) {
      String[] fields = trail.split("\\.");
      nullifyFieldRecursive(object, fields, 0);
    }
  }

  /**
   * Recursively navigate the object using the fields array and set the field to null.
   */
  private static void nullifyFieldRecursive(Object object, String[] fields, int index)
      throws NoSuchFieldException, IllegalAccessException {
    if (object == null || index >= fields.length) {
      return;
    }

    if (object instanceof List<?> list) {
      // Handle List objects
      for (Object item : list) {
        nullifyFieldRecursive(item, fields, index);
      }
    } else {
      Field field = object.getClass().getDeclaredField(fields[index]);
      field.setAccessible(true);

      if (index == fields.length - 1) {
        // Last field in the path, set to null
        field.set(object, null);
      } else {
        // Not the last field, navigate to the next object or list in the path
        Object nextObject = field.get(object);
        nullifyFieldRecursive(nextObject, fields, index + 1);
      }
    }
  }
}
