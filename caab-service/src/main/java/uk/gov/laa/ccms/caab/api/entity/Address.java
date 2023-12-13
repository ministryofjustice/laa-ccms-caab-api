package uk.gov.laa.ccms.caab.api.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents an address entity from the "XXCCMS_ADDRESS" table.
 * This entity is primarily used to manage and persist address data
 * associated with the CCMS system. It utilizes the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_ADDRESS")
@Setter
@Getter
@SequenceGenerator(
        allocationSize = 1,
        sequenceName = "XXCCMS_GENERATED_ID_S",
        name = "XXCCMS_ADDRESS_S")
@RequiredArgsConstructor
public class Address implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_ADDRESS_S")
  private Long id;

  @Convert(converter = BooleanIntegerConverter.class)
  @Column(name = "NO_FIXED_ABODE")
  private Boolean noFixedAbode = false;

  @Column(name = "POSTCODE", length = 15)
  private String postCode;

  @Column(name = "HOUSE_NAME_NUMBER", length = 50)
  private String houseNameNumber;

  @Column(name = "ADDRESS_LINE1", length = 35)
  private String addressLine1;

  @Column(name = "ADDRESS_LINE2", length = 35)
  private String addressLine2;

  @Column(name = "CITY", length = 35)
  private String city;

  @Column(name = "COUNTY", length = 35)
  private String county;

  @Column(name = "COUNTRY", length = 3)
  private String country;

  @Column(name = "CARE_OF", length = 35)
  private String careOf;

  @Column(name = "PREFERRED_ADDRESS", length = 50)
  private String preferredAddress;


  @Embedded
  private AuditTrail auditTrail = new AuditTrail();


}
