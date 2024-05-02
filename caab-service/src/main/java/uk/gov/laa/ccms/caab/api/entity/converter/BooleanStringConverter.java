package uk.gov.laa.ccms.caab.api.entity.converter;

import jakarta.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.stream.Stream;
import org.hibernate.dialect.Dialect;
import org.hibernate.type.descriptor.converter.spi.BasicValueConverter;
import org.hibernate.type.descriptor.java.BooleanJavaType;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.StringJavaType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
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
   * Get all of the values supported by this converter.
   *
   * @return a concatenated list of true and false values.
   */
  protected String[] getValues() {
    return Stream.concat(
        Arrays.stream(trueValues),
        Arrays.stream(falseValues)).toArray(String[]::new);
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

    return Arrays.asList(trueValues).contains(relationalForm)
        ? Boolean.TRUE :
        (Arrays.asList(falseValues).contains(relationalForm) ? Boolean.FALSE : null);
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

  public String getCheckCondition(String columnName, JdbcType jdbcType, Dialect dialect) {
    return dialect.getCheckCondition(columnName, this.getValues());
  }

  public String getSpecializedTypeDeclaration(JdbcType jdbcType, Dialect dialect) {
    return dialect.getEnumTypeDeclaration(this.getValues());
  }
}
