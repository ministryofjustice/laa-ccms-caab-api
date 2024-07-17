package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;
import org.hibernate.type.YesNoConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * Represents an opponent entity associated with the "XXCCMS_OPPONENT" table.
 *
 * <p>This entity is utilized to manage and persist opponent data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_OPPONENT")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_OPPONENT_S")
@Getter
@Setter
@RequiredArgsConstructor
public class Opponent implements Serializable {

  /**
   * The unique identifier for the Opponent.
   */
  @Id
  @GeneratedValue(generator = "XXCCMS_OPPONENT_S")
  private Long id;

  /**
   * The audit trail information for the Opponent.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();

  /**
   * The application associated with the Opponent.
   */
  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;

  /**
   * The EBS ID of the Opponent.
   */
  @Column(name = "EBS_ID", length = 50)
  private String ebsId;

  /**
   * The type of the Opponent.
   */
  @Column(name = "TYPE", length = 50)
  private String type;

  /**
   * The title of the Opponent.
   */
  @Column(name = "TITLE", length = 50)
  private String title;

  /**
   * The first name of the Opponent.
   */
  @Column(name = "FIRST_NAME", length = 35)
  private String firstName;

  /**
   * The middle names of the Opponent.
   */
  @Column(name = "MIDDLE_NAMES", length = 35)
  private String middleNames;

  /**
   * The surname of the Opponent.
   */
  @Column(name = "SURNAME", length = 35)
  private String surname;

  /**
   * The date of birth of the Opponent.
   */
  @Column(name = "DATE_OF_BIRTH")
  private Date dateOfBirth;

  /**
   * The national insurance number of the Opponent.
   */
  @Column(name = "NATIONAL_INSURANCE_NUMBER", length = 9)
  private String nationalInsuranceNumber;

  /**
   * The address of the Opponent.
   */
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_ADDRESS")
  private Address address;

  /**
   * The relationship to the case of the Opponent.
   */
  @Column(name = "RELATIONSHIP_TO_CASE", length = 50)
  private String relationshipToCase;

  /**
   * The relationship to the client of the Opponent.
   */
  @Column(name = "RELATIONSHIP_TO_CLIENT", length = 50)
  private String relationshipToClient;

  /**
   * The home telephone number of the Opponent.
   */
  @Column(name = "TELEPHONE_HOME", length = 15)
  private String telephoneHome;

  /**
   * The work telephone number of the Opponent.
   */
  @Column(name = "TELEPHONE_WORK", length = 15)
  private String telephoneWork;

  /**
   * The mobile telephone number of the Opponent.
   */
  @Column(name = "TELEPHONE_MOBILE", length = 15)
  private String telephoneMobile;

  /**
   * The fax number of the Opponent.
   */
  @Column(name = "FAX_NUMBER", length = 15)
  private String faxNumber;

  /**
   * The email address of the Opponent.
   */
  @Column(name = "EMAIL_ADDRESS", length = 254)
  private String emailAddress;

  /**
   * Additional information about the Opponent.
   */
  @Column(name = "OTHER_INFORMATION", length = 2000)
  private String otherInformation;

  /**
   * The employment status of the Opponent.
   */
  @Column(name = "EMPLOYMENT_STATUS", length = 25)
  private String employmentStatus;

  /**
   * The name of the employer of the Opponent.
   */
  @Column(name = "EMPLOYER_NAME", length = 35)
  private String employerName;

  /**
   * The address of the employer of the Opponent.
   */
  @Column(name = "EMPLOYER_ADDRESS", length = 200)
  private String employerAddress;

  /**
   * Indicates if the Opponent is eligible for legal aid.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "LEGAL_AIDED")
  private Boolean legalAided;

  /**
   * The certificate number for legal aid.
   */
  @Column(name = "CERTIFICATE_NUMBER", length = 35)
  private String certificateNumber;

  /**
   * Indicates if a court-ordered means assessment is required for the Opponent.
   */
  @Convert(converter = YesNoConverter.class)
  @Column(name = "COURT_ORDERDED_MEANS_ASSMT", length = 10)
  private Boolean courtOrderedMeansAssessment;

  /**
   * The assessed income of the Opponent.
   */
  @Column(name = "ASSESSED_INCOME", precision = 10, scale = 2)
  private BigDecimal assessedIncome;

  /**
   * The frequency of the assessed income.
   */
  @Column(name = "ASSESSED_INCOME_FREQUENCY", length = 25)
  private String assessedIncomeFrequency;

  /**
   * The assessed assets of the Opponent.
   */
  @Column(name = "ASSESSED_ASSETS", precision = 10, scale = 2)
  private BigDecimal assessedAssets;

  /**
   * The date of assessment for the Opponent.
   */
  @Column(name = "ASSESSMENT_DATE")
  private Date assessmentDate;

  /**
   * The name of the organization associated with the Opponent.
   */
  @Column(name = "ORGANISATION_NAME", length = 360)
  private String organisationName;

  /**
   * The type of the organization associated with the Opponent.
   */
  @Column(name = "ORGANISATION_TYPE", length = 50)
  private String organisationType;

  /**
   * Indicates if the organization is currently trading.
   */
  @Convert(converter = YesNoConverter.class)
  @Column(name = "CURRENTLY_TRADING", length = 10)
  private Boolean currentlyTrading;

  /**
   * The role of the contact name associated with the Opponent.
   */
  @Column(name = "CONTACT_NAME_ROLE", length = 35)
  private String contactNameRole;

  /**
   * Indicates if the Opponent is confirmed.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "CONFIRMED")
  private Boolean confirmed;

  /**
   * Indicates the application mode.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "APPMODE")
  private Boolean appMode;

  /**
   * Indicates if the Opponent represents an amendment.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "AMENDMENT")
  private Boolean amendment;

  /**
   * Indicates if the Opponent is awarded.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "AWARD")
  private Boolean award;

  /**
   * Indicates if public funding has been applied for.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "PUBLIC_FUNDING_APPLIED")
  private Boolean publicFundingApplied;

  /**
   * Indicates if the Opponent is shared.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "SHARED_IND")
  private Boolean sharedInd;

  /**
   * Indicates if the Opponent is marked for deletion.
   */
  @Column(name = "DELETE_IND")
  private Boolean deleteInd;

  /**
   * The outcome associated with the Opponent.
   */
  @ManyToOne
  @JoinColumn(name = "FK_OUTCOME")
  private CaseOutcome caseOutcome;

  /**
   * The liable parties associated with this opponent.
   */
  @OneToMany(fetch = FetchType.LAZY, mappedBy = "opponent",
      cascade = CascadeType.REMOVE, orphanRemoval = true)
  private List<LiableParty> liableParties;

}
