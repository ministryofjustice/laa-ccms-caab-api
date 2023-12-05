package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;


/**
 * Represents an opponent entity associated with the "XXCCMS_OPPONENT" table.
 *
 * <p>This entity is utilized to manage and persist opponent data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@Table(name = "XXCCMS_OPPONENT")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_OPPONENT_S")
@Getter
@Setter
@RequiredArgsConstructor
public class Opponent implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_OPPONENT_S")
  private Long id;

  @Embedded
  private AuditTrail auditTrail;

  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;

  @Column(name = "EBS_ID", length = 50)
  private String ebsId;

  @Column(name = "TYPE", length = 50)
  private String type;

  @Column(name = "TITLE", length = 50)
  private String title;

  @Column(name = "FIRST_NAME", length = 35)
  private String firstName;

  @Column(name = "MIDDLE_NAMES", length = 35)
  private String middleNames;

  @Column(name = "SURNAME", length = 35)
  private String surname;

  @Column(name = "DATE_OF_BIRTH")
  private Date dateOfBirth;

  @Column(name = "NATIONAL_INSURANCE_NUMBER", length = 9)
  private String nationalInsuranceNumber;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_ADDRESS")
  private Address address;

  @Column(name = "RELATIONSHIP_TO_CASE", length = 50)
  private String relationshipToCase;

  @Column(name = "RELATIONSHIP_TO_CLIENT", length = 50)
  private String relationshipToClient;
  @Column(name = "TELEPHONE_HOME", length = 15)
  private String telephoneHome;
  @Column(name = "TELEPHONE_WORK", length = 15)
  private String telephoneWork;
  @Column(name = "TELEPHONE_MOBILE", length = 15)
  private String telephoneMobile;
  @Column(name = "FAX_NUMBER", length = 15)
  private String faxNumber;
  @Column(name = "EMAIL_ADDRESS", length = 254)
  private String emailAddress;
  @Column(name = "OTHER_INFORMATION", length = 2000)
  private String otherInformation;
  @Column(name = "EMPLOYMENT_STATUS", length = 25)
  private String employmentStatus;
  @Column(name = "EMPLOYER_NAME", length = 35)
  private String employerName;
  @Column(name = "EMPLOYER_ADDRESS", length = 200)
  private String employerAddress;
  @Column(name = "LEGAL_AIDED")
  private Boolean legalAided;
  @Column(name = "CERTIFICATE_NUMBER", length = 35)
  private String certificateNumber;
  @Column(name = "COURT_ORDERDED_MEANS_ASSMT", length = 10)
  private Boolean courtOrderedMeansAssessment;
  @Column(name = "ASSESSED_INCOME", precision = 10, scale = 2)
  private BigDecimal assessedIncome;

  @Column(name = "ASSESSED_INCOME_FREQUENCY", length = 25)
  private String assessedIncomeFrequency;
  @Column(name = "ASSESSED_ASSETS", precision = 10, scale = 2)
  private BigDecimal assessedAssets;
  @Column(name = "ASSESSMENT_DATE")
  private Date assessmentDate;
  @Column(name = "ORGANISATION_NAME", length = 360)
  private String organisationName;
  @Column(name = "ORGANISATION_TYPE", length = 50)
  private String organisationType;
  @Column(name = "CURRENTLY_TRADING", length = 10)
  private Boolean currentlyTrading;
  @Column(name = "CONTACT_NAME_ROLE", length = 35)
  private String contactNameRole;

  @Column(name = "CONFIRMED")
  private boolean confirmed;
  @Column(name = "APPMODE")
  private boolean appMode = true;
  @Column(name = "AMENDMENT")
  private boolean amendment = false;
  @Column(name = "AWARD")
  private boolean award = false;

  @Column(name = "PUBLIC_FUNDING_APPLIED")
  private boolean publicFundingApplied;
  @Column(name = "SHARED_IND")
  private boolean sharedInd = false;

  @Column(name = "DELETE_IND")
  private boolean deleteInd = true;

  @ManyToOne
  @JoinColumn(name = "FK_OUTCOME")
  private CaseOutcome outcome;

}
