package uk.gov.laa.ccms.caab.api.entity.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class BooleanStringConverterTest {

  @ParameterizedTest
  @CsvSource({
      "'certainly', 'not'",
      "'Y,Yes', 'N,No'"
  })
  void testToDomainValue_returnsCorrectResponse(String trueValues, String falseValues) {

    String[] trueVals = trueValues.split(",");
    String[] falseVals = falseValues.split(",");

    BooleanStringConverter booleanStringConverter = new BooleanStringConverter(
        trueVals, falseVals);

    Arrays.stream(trueVals).forEach(s -> assertTrue(booleanStringConverter.toDomainValue(s)));
    Arrays.stream(falseVals).forEach(s -> assertFalse(booleanStringConverter.toDomainValue(s)));
  }

  @Test
  void testToDomainValue_nullValue_returnsNull() {
    BooleanStringConverter booleanStringConverter = new BooleanStringConverter(
        new String[]{"Y"}, new String[]{"N"});

    assertThrows( RuntimeException.class, () -> booleanStringConverter.toDomainValue(
        "unknownval"));
  }

  @Test
  void testToDomainValue_unknownValue_throwsException() {
    BooleanStringConverter booleanStringConverter = new BooleanStringConverter(
        new String[]{"Y"}, new String[]{"N"});

    assertNull( booleanStringConverter.toDomainValue(null));
  }

  @ParameterizedTest
  @CsvSource(value = {
      "'certainly', 'not', true, certainly",
      "'certainly', 'not', false, not",
      "'Y,Yes', 'N,No', true, Y",
      "'Y,Yes', 'N,No', false, N",
      "'Y,Yes', 'N,No', null, null"
  }, nullValues = "null")
  void testToRelationalValueTrue_returnsFirstTrueValue(String trueValues, String falseValues, Boolean domainVal, String expectedResult) {

    String[] trueVals = trueValues.split(",");
    String[] falseVals = falseValues.split(",");

    BooleanStringConverter booleanStringConverter = new BooleanStringConverter(
        trueVals, falseVals);

    String result = booleanStringConverter.toRelationalValue(domainVal);
    assertEquals(expectedResult, result);
  }
}
