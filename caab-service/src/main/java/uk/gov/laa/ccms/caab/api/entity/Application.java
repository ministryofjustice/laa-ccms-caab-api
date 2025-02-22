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
import jakarta.persistence.OrderBy;
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
 * Represents an application entity from the "XXCCMS_APPLICATION" table.
 * This entity is primarily used to manage and persist application data
 * within the CCMS system. It utilizes the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_APPLICATION", schema = "XXCCMS_PUI")
@SequenceGenerator(
        allocationSize = 1,
        sequenceName = "XXCCMS_GENERATED_ID_S",
        name = "XXCCMS_APPLICATION_S",
        schema = "XXCCMS_PUI")
@Getter
@Setter
@RequiredArgsConstructor
public class Application implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_APPLICATION_S")
  private Long id;

  @Column(name = "LSC_CASE_REFERENCE", length = 35, nullable = false)
  private String lscCaseReference;

  //Provider
  @Column(name = "PROVIDER_ID", length = 19, nullable = false)
  private String providerId;
  @Column(name = "PROVIDER_DISPLAY_VALUE", length = 300)
  private String providerDisplayValue;
  @Column(name = "PROVIDER_CASE_REFERENCE", length = 35)
  private String providerCaseReference;

  //Office
  @Column(name = "OFFICE_ID")
  private Integer officeId;
  @Column(name = "OFFICE_DISPLAY_VALUE", length = 360)
  private String officeDisplayValue;

  //Supervisor
  @Column(name = "SUPERVISOR", length = 19)
  private String supervisor;
  @Column(name = "SUPERVISOR_DISPLAY_VALUE", length = 300)
  private String supervisorDisplayValue;

  //Fee Earner
  @Column(name = "FEE_EARNER", length = 19)
  private String feeEarner;
  @Column(name = "FEE_EARNER_DISPLAY_VALUE", length = 300)
  private String feeEarnerDisplayValue;

  //Provider Contact
  @Column(name = "PROVIDER_CONTACT", length = 19)
  private String providerContact;
  @Column(name = "PROVIDER_CONTACT_DISPLAY_VALUE", length = 300)
  private String providerContactDisplayValue;

  //Category of law
  @Column(name = "CATEGORY_OF_LAW", length = 50, nullable = false)
  private String categoryOfLaw;
  @Column(name = "CATEGORY_OF_LAW_DISPLAY_VALUE", length = 50)
  private String categoryOfLawDisplayValue;

  @Column(name = "RELATION_TO_LINKED_CASE", length = 50)
  private String relationToLinkedCase;

  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "OPPONENT_APPLIED_FOR_FUNDING")
  private Boolean opponentAppliedForFunding;

  //status
  @Column(name = "DISPLAY_STATUS", length = 50)
  private String displayStatus;
  @Column(name = "ACTUAL_STATUS", length = 50)
  private String actualStatus;

  //client
  @Column(name = "CLIENT_FIRSTNAME", length = 50)
  private String clientFirstName;
  @Column(name = "CLIENT_SURNAME", length = 50)
  private String clientSurname;
  @Column(name = "CLIENT_REFERENCE", length = 30, nullable = false)
  private String clientReference;

  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "AMENDMENT")
  private Boolean amendment;

  //Application Type
  @Column(name = "APPLICATION_TYPE", length = 30)
  private String applicationType;
  @Column(name = "APPLICATION_TYPE_DISPLAY_VALUE", length = 35)
  private String applicationTypeDisplayValue;

  @Convert(converter = YesNoConverter.class)
  @Column(name = "DEVOLVED_POWERS_USED", length = 5)
  private Boolean devolvedPowersUsed;

  @Column(name = "DATE_DEVOLVED_POWERS_USED", length = 5)
  private Date dateDevolvedPowersUsed;
  @Column(name = "DEVOLVED_POWERS_CONTRACT_FLAG", length = 30)
  private String devolvedPowersContractFlag;

  @Convert(converter = YesNoConverter.class)
  @Column(name = "LAR_SCOPE_FLAG", length = 5)
  private Boolean larScopeFlag;

  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "MEANS_ASSESSMENT_AMENDED")
  private Boolean meansAssessmentAmended;

  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "MERITS_ASSESSMENT_AMENDED")
  private Boolean meritsAssessmentAmended;

  //Cost Limit
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "COST_LIMIT_CHANGED")
  private Boolean costLimitChanged;
  @Column(name = "COST_LIMIT_AT_TIME_OF_MERITS", precision = 10, scale = 2)
  private BigDecimal costLimitAtTimeOfMerits;

  //submission status
  //poll_transaction_id
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "MERITS_REASSESSMENT_REQD_IND")
  private Boolean meritsReassessmentReqdInd;

  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "LEAD_PROCEEDING_CHANGED")
  private Boolean leadProceedingChangedOpaInput;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "FK_COST_STRUCTURE")
  private CostStructure costs;

  @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name = "FK_CORRESPONDENCE_ADDRESS")
  private Address correspondenceAddress;

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("leadProceedingInd desc, id asc")
  private List<Proceeding> proceedings;

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<PriorAuthority> priorAuthorities;

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<Opponent> opponents;

  @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id asc")
  private List<LinkedCase> linkedCases;


  @Embedded
  private AuditTrail auditTrail = new AuditTrail();
  

}
