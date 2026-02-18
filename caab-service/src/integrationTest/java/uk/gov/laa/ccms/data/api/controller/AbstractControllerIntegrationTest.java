package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static uk.gov.laa.ccms.caab.api.audit.AuditorAwareImpl.currentUserHolder;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.metric.ApplicationsMetricScheduler;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.data.api.OracleContainerIntegrationTest;

public class AbstractControllerIntegrationTest extends OracleContainerIntegrationTest {

  protected final String caabUserLoginId = "audit@user.com";

  @MockitoBean
  private ApplicationsMetricScheduler applicationsMetricScheduler;
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
        AbstractControllerIntegrationTest.nullifyFieldRecursive(item, fields, index);
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
        AbstractControllerIntegrationTest.nullifyFieldRecursive(nextObject, fields, index + 1);
      }
    }
  }

  @BeforeEach
  public void setup() {
    currentUserHolder.set(caabUserLoginId);
  }

  public static boolean areAllFieldsEqual(Object obj1, Object obj2, Class<?> clazz, List<String> fieldsToIgnore)
      throws IllegalArgumentException, IllegalAccessException {

    for (Field field : clazz.getDeclaredFields()) {
      field.setAccessible(true);

      // Continue to the next field if it should be ignored
      if (fieldsToIgnore.contains(field.getName())) {
        continue;
      }

      Object value1 = field.get(obj1);
      Object value2 = field.get(obj2);

      if (!Objects.equals(value1, value2)) {
        System.out.println("Field " + field.getName() + " is not equal: " + value1 + " != " + value2);
        return false;
      }
    }
    return true;
  }

  /**
   * Loads the JSON file from the classpath and parses it into a specified object.
   *
   * @param jsonFilePath The path to the JSON file to load
   */
  protected <T> T loadObjectFromJson(String jsonFilePath, Class<T> objectType) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    ClassPathResource resource = new ClassPathResource(jsonFilePath);
    return objectMapper.readValue(resource.getInputStream(), objectType);
  }

  /**
   * Generates a random 12-digit number string to be used as a case reference number.
   */
  protected String generateTestCaseRef() {
    Random random = new Random();
    StringBuilder stringBuilder = new StringBuilder(12);

    for (int i = 0; i < 12; i++) {
      int digit = random.nextInt(10); // Generate a random digit (0-9)
      stringBuilder.append(digit);
    }

    return stringBuilder.toString();
  }

  // Utility method to resolve the method chain and get the value
  protected Object getObjectToCheckFromMethod(Object initialObject, String[] methodCalls)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

    Object currentObject = initialObject;
    for (String methodName : methodCalls) {
      Method method = currentObject.getClass().getMethod(methodName);
      currentObject = method.invoke(currentObject);
    }
    return currentObject;
  }

  /**
   * Used to assert that the audit detail is set correctly.
   *
   * @param auditDetail The audit trail to check
   * @param expectedAuditUser The expected audit user
   */
  protected void assertAuditTrail(AuditDetail auditDetail, String expectedAuditUser) {
    assertNotNull(auditDetail);
    assertEquals(expectedAuditUser, auditDetail.getCreatedBy());
    assertEquals(expectedAuditUser, auditDetail.getLastSavedBy());
    assertNotNull(auditDetail.getCreated());
    assertNotNull(auditDetail.getLastSaved());
  }

  /**
   * Assert that the audit trail is set correctly in the object. Uses reflection to navigate the
   * auditTrailMethod path and assert that the audit trail is set correctly.
   */
  protected void assertAuditTrail(Object object, String auditMethod, String expectedAuditUser)
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
  protected void setAuditPropertiesToNull(Object object, List<String> auditTrailsToNull) throws NoSuchFieldException, IllegalAccessException {
    if (object == null || auditTrailsToNull == null) {
      return;
    }
    for (String trail : auditTrailsToNull) {
      String[] fields = trail.split("\\.");
      AbstractControllerIntegrationTest.nullifyFieldRecursive(object, fields, 0);
    }
  }
}
