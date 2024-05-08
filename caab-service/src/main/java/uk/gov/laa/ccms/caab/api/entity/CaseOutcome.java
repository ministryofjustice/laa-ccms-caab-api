package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.gov.laa.ccms.caab.api.entity.converter.BooleanStringConverter;

/**
 * Represents a case outcome entity associated with the "XXCCMS_CASE_OUTCOME" table.
 *
 * <p>This entity is utilized to manage and persist case outcome data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_CASE_OUTCOME")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_CASE_OUTCOME_S")
@Getter
@Setter
@RequiredArgsConstructor
public class CaseOutcome {

  @Id
  @GeneratedValue(generator = "XXCCMS_CASE_OUTCOME_S")
  private Long id;

  /**
   * Case Reference Number.
   */
  @Column(name = "LSC_CASE_REFERENCE", length = 50, nullable = false)
  private String lscCaseReference;

  /**
   * Pre-certificate costs.
   */
  @Column(name = "PRE_CERTIFICATE_COSTS", precision = 10, scale = 2)
  private BigDecimal preCertificateCosts;

  /**
   * Legal costs.
   */
  @Column(name = "LEGAL_COSTS", precision = 10, scale = 2)
  private BigDecimal legalCosts;

  /**
   * Discharge reason.
   */
  @Column(name = "DISCHARGE_REASON", length = 50)
  private String dischargeReason;

  /**
   * Discharge case indicator.
   */
  @Convert(converter = BooleanStringConverter.class)
  @Column(name = "DISCHARGE_CASE_IND", length = 50)
  private Boolean dischargeCaseInd;

  /**
   * Client continue indicator.
   */
  @Convert(converter = BooleanStringConverter.class)
  @Column(name = "CLIENT_CONTINUE_IND", length = 50)
  private Boolean clientContinueInd;

  /**
   * Other details for the outcome.
   */
  @Column(name = "OTHER_DETAILS", length = 1000)
  private String otherDetails;

  /**
   * The related office code.
   */
  @Column(name = "OFFICE_CODE", length = 30)
  private String officeCode;

  /**
   * The unique file number.
   */
  @Column(name = "UNIQUE_FILE_NO", length = 30)
  private String uniqueFileNo;

  /**
   * The related provider id.
   */
  @Column(name = "PROVIDER_ID", length = 19, nullable = false)
  private String providerId;

  /**
   * The cost awards for this outcome.
   */
  @OneToMany(mappedBy = "caseOutcome", cascade = CascadeType.ALL)
  private List<CostAward> costAwards;

  /**
   * The land awards for this outcome.
   */
  @OneToMany(mappedBy = "caseOutcome", cascade = CascadeType.ALL)
  private List<LandAward> landAwards;

  /**
   * The financial awards for this outcome.
   */
  @OneToMany(mappedBy = "caseOutcome", cascade = CascadeType.ALL)
  private List<FinancialAward> financialAwards;

  /**
   * The other asset awards for this outcome.
   */
  @OneToMany(mappedBy = "caseOutcome", cascade = CascadeType.ALL)
  private List<OtherAssetAward> otherAssetAwards;

  /**
   * The related opponents for this outcome.
   */
  @OneToMany(mappedBy = "caseOutcome", cascade = CascadeType.MERGE)
  private List<Opponent> opponents;

  /**
   * The related proceeding outcomes for this case outcome.
   */
  @OneToMany(mappedBy = "caseOutcome", cascade = CascadeType.ALL)
  private List<ProceedingOutcome> proceedingOutcomes;

  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();
}
