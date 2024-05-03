package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import uk.gov.laa.ccms.caab.api.entity.converter.BooleanStringConverter;

/**
 * Entity to represent an other asset award entry in the XXCCMS_OTHER_ASSET_AWARD TDS table.
 */
@Entity
@Table(name = "XXCCMS_OTHER_ASSET_AWARD")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_OTHER_ASSET_AWARD_S")
@Getter
@Setter
@RequiredArgsConstructor
public class OtherAssetAward extends BaseAward {

  @Id
  @GeneratedValue(generator = "XXCCMS_OTHER_ASSET_AWARD_S")
  private Long id;

  /**
   * The valuation amount.
   */
  @Column(name = "VALUATION_AMT", precision = 10, scale = 2)
  private BigDecimal valuationAmount;

  /**
   * The valuation criteria.
   */
  @Column(name = "VALUATION_CRITERIA", length = 50)
  private String valuationCriteria;

  /**
   * The valuation date.
   */
  @Column(name = "VALUATION_DATE")
  private Date valuationDate;

  /**
   * The awarded percentage.
   */
  @Column(name = "AWARDED_PERCENTAGE", precision = 10, scale = 2)
  private BigDecimal awardedPercentage;

  /**
   * The recovered amount.
   */
  @Column(name = "RECOVERED_AMOUNT", precision = 10, scale = 2)
  private BigDecimal recoveredAmount;

  /**
   * The recovered percentage.
   */
  @Column(name = "RECOVERED_PERCENTAGE", precision = 10, scale = 2)
  private BigDecimal recoveredPercentage;

  /**
   * The disputed amount.
   */
  @Column(name = "DISPUTED_AMOUNT", precision = 10, scale = 2)
  private BigDecimal disputedAmount;

  /**
   * The awarded amount.
   */
  @Column(name = "AWARDED_AMOUNT", precision = 10, scale = 2)
  private BigDecimal awardedAmount;

  /**
   * The disputed percentage amount.
   */
  @Column(name = "DISPUTED_PERCENTAGE", precision = 10, scale = 2)
  private BigDecimal disputedPercentage;

  /**
   * The recovery details.
   */
  @Column(name = "RECOVERY", length = 200)
  private String recovery;

  /**
   * The no recovery details.
   */
  @Column(name = "NO_RECOVERY_DETAILS", length = 1000)
  private String noRecoveryDetails;

  /**
   * The statutory charge exempt reason.
   */
  @Column(name = "STAT_CHARGE_EXEMPT_REASON", length = 1000)
  private String statutoryChargeExemptReason;

  /**
   * Flag to indicate the recovery of award is time related.
   */
  @Convert(converter = BooleanStringConverter.class)
  @Column(name = "RECOVERY_TIME_RELATED_FLAG", length = 10)
  private Boolean recoveryOfAwardTimeRelated;

  /**
   * The related outcome for this award.
   */
  @ManyToOne
  @JoinColumn(name = "FK_OTHER_ASSET_CASE_OUTCOME", nullable = false)
  private CaseOutcome caseOutcome;

  /**
   * The time recovery for the award.
   */
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_OTHER_ASSET_RECOVERY")
  private TimeRecovery timeRecovery;

  /**
   * liable parties tied to the Award.
   */
  @OneToMany(mappedBy = "otherAssetAward", cascade = CascadeType.ALL)
  protected List<LiableParty> liableParties;
}
