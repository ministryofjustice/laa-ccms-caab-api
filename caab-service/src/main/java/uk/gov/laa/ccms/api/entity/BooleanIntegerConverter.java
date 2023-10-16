package uk.gov.laa.ccms.api.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.Optional;

/**
 * AttributeConverter class to handle the conversion to/from a Boolean value and 1 or 0.
 */
@Converter
public class BooleanIntegerConverter implements AttributeConverter<Boolean, Integer> {

  /**
   * Handle the conversion from a boolean value, to 1 or 0.
   *
   * @param attribute  the entity attribute value to be converted
   * @return 1 or 0 based on attribute value, or null if the attribute is null
   */
  @Override
  public Integer convertToDatabaseColumn(Boolean attribute) {
    return Optional.ofNullable(attribute)
            .map(a -> a ? 1 : 0)
            .orElse(null);
  }

  /**
   * Handle the conversion from 1 or 0 to a Boolean value.
   *
   * @param dbData  the data from the database column to be
   *                converted
   * @return equivalent Boolean value, or null if the dbData is null
   */
  @Override
  public Boolean convertToEntityAttribute(Integer dbData) {
    return Optional.ofNullable(dbData)
            .map(d -> d.equals(1))
            .orElse(null);
  }

}
