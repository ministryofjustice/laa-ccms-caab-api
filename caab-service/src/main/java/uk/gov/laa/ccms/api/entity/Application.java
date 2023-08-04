package uk.gov.laa.ccms.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "XXCCMS_APPLICATION")
@SequenceGenerator(allocationSize = 1, sequenceName = "XXCCMS_GENERATED_ID_S", name = "XXCCMS_APPLICATION_S")
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
    private Long officeId;
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

    @Column(name = "AMENDMENT")
    private boolean amendment = false;

    //Application Type
    @Column(name = "APPLICATION_TYPE", length = 30)
    private String applicationType;
    @Column(name = "APPLICATION_TYPE_DISPLAY_VALUE", length = 35)
    private String applicationTypeDisplayValue;

    @Convert(converter = StringCharacterConverter.class)
    @Column(name = "DEVOLVED_POWERS_USED", length = 5)
    private String devolvedPowersUsed;

    @Column(name = "DATE_DEVOLVED_POWERS_USED", length = 5)
    private Date dateDevolvedPowersUsed;
    @Column(name = "DEVOLVED_POWERS_CONTRACT_FLAG", length = 30)
    private String devolvedPowersContractFlag;

    @Column(name = "LAR_SCOPE_FLAG", length = 5)
    private String larScopeFlag;

    @Column(name = "MEANS_ASSESSMENT_AMENDED")
    private boolean meansAssessmentAmended = false;

    @Column(name = "MERITS_ASSESSMENT_AMENDED")
    private boolean meritsAssessmentAmended = false;

    //Cost Limit
    @Column(name = "COST_LIMIT_CHANGED")
    private boolean costLimitChanged;
    @Column(name = "COST_LIMIT_AT_TIME_OF_MERITS", precision = 10, scale = 2)
    private BigDecimal costLimitAtTimeOfMerits;

    //submission status
    //poll_transaction_id

    @Column(name = "MERITS_REASSESSMENT_REQD_IND")
    private boolean meritsReassessmentReqdInd = false;

    @Column(name = "LEAD_PROCEEDING_CHANGED")
    private boolean leadProceedingChangedOpaInput = false;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_COST_STRUCTURE")
    @Access(AccessType.PROPERTY)
    private CostStructure costs;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "FK_CORRESPONDENCE_ADDRESS")
    @Access(AccessType.PROPERTY)
    private Address correspondenceAddress;

    @Embedded
    private AuditTrail auditTrail = new AuditTrail();

}
