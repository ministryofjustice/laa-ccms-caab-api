package uk.gov.laa.ccms.caab.api.entity.converter;

import jakarta.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.Optional;
import org.hibernate.type.descriptor.converter.spi.BasicValueConverter;
import org.hibernate.type.descriptor.java.BooleanJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.springframework.beans.factory.annotation.Value;

/**
 * Configurable AttributeConverter to handle conversion to and from Boolean and DB
 * representation of Boolean values.
 */
public class BooleanStringConverter implements
    AttributeConverter<Boolean, String>, BasicValueConverter<Boolean, String> {

  public static final BooleanStringConverter INSTANCE = new BooleanStringConverter();

  /**
   * Array of values which should resolve to a Boolean.TRUE.
   */
  @Value("${laa.ccms.caab.converters.boolean.true_values}")
  protected String[] trueValues;

  /**
   * Array of values which should resolve to a Boolean.FALSE.
   */
  @Value("${laa.ccms.caab.converters.boolean.false_values}")
  protected String[] falseValues;

  public BooleanStringConverter() {
    super();
  }

  /**
   * Constructor which takes an array of true and false values.
   * Added for testing purposes only.
   *
   * @param trueValues - the true values.
   * @param falseValues - the false values.
   */
  protected BooleanStringConverter(final String[] trueValues, final String[] falseValues) {
    this.trueValues = trueValues;
    this.falseValues = falseValues;
  }

  /**
   * Convert the relational form to the domain value.
   *
   * @param relationalForm - the relational form.
   *
   * @return Boolean representation of relational form, or null.
   */
  public Boolean toDomainValue(String relationalForm) {
    if (relationalForm == null) {
      return null;
    }

    return containsRelationalForm(trueValues, relationalForm, Boolean.TRUE)
        .orElseGet(() -> containsRelationalForm(falseValues, relationalForm, Boolean.FALSE)
            .orElseThrow(() -> new RuntimeException(
                String.format("Unknown boolean relational value %s", relationalForm))));
  }

  private Optional<Boolean> containsRelationalForm(final String[] values,
      final String relationalForm, final Boolean response) {
    return Arrays.stream(values)
        .filter(s -> s.equals(relationalForm))
        .findFirst()
        .map(s -> response);
  }

  /**
   * Convert the Boolean domain form into the equivalent relational form.
   * Where multiple true or false values are configured for this converter, the first entry
   * in the appropriate array will be selected.
   *
   * @param domainForm - the domain form.
   *
   * @return relational form of Boolean value, or null.
   */
  public String toRelationalValue(Boolean domainForm) {
    return domainForm == null ? null : (domainForm ? trueValues[0] : falseValues[0]);
  }

  public String convertToDatabaseColumn(Boolean attribute) {
    return this.toRelationalValue(attribute);
  }

  public Boolean convertToEntityAttribute(String dbData) {
    return this.toDomainValue(dbData);
  }

  public JavaType<Boolean> getDomainJavaType() {
    return BooleanJavaType.INSTANCE;
  }

  public JavaType<String> getRelationalJavaType() {
    return StringJavaType.INSTANCE;
  }
}
