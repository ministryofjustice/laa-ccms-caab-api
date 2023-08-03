package uk.gov.laa.ccms.caab.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "XXCCMS_ADDRESS")
@Setter
@Getter
@SequenceGenerator(allocationSize = 1, sequenceName = "XXCCMS_GENERATED_ID_S", name = "XXCCMS_ADDRESS_S")
@RequiredArgsConstructor
public class Address implements Serializable {

    @Id
    @GeneratedValue(generator = "XXCCMS_ADDRESS_S")
    private Long id;

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
    @Access(AccessType.PROPERTY)
    private String preferredAddress;


    @Embedded
    private AuditTrail auditTrail;

    public Address(AuditTrail auditTrail){
        this.auditTrail = auditTrail;
    }

}
