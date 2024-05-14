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
 * Entity to represent a land award entry in the XXCCMS_LAND_AWARD TDS table.
 */
@Entity
@Table(name = "XXCCMS_LAND_AWARD")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_LAND_AWARD_S")
@Getter
@Setter
@RequiredArgsConstructor
public class LandAward extends BaseAward {

  @Id
  @GeneratedValue(generator = "XXCCMS_LAND_AWARD_S")
  private Long id;

  /**
   * The title number.
   */
  @Column(name = "TITLE_NO", length = 50)
  private String titleNumber;

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
   * The disputed percentage.
   */
  @Column(name = "DISPUTED_PERCENTAGE", precision = 10, scale = 2)
  private BigDecimal disputedPercentage;

  /**
   * The awarded percentage.
   */
  @Column(name = "AWARDED_PERCENTAGE", precision = 10, scale = 2)
  private BigDecimal awardedPercentage;

  /**
   * The mortgage amount due.
   */
  @Column(name = "MORTGAGE_AMOUNT_DUE", precision = 10, scale = 2)
  private BigDecimal mortgageAmountDue;

  /**
   * The equity amount.
   */
  @Column(name = "EQUITY", precision = 10, scale = 2)
  private BigDecimal equity;

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
   * The statutory charge exemption reason.
   */
  @Column(name = "STAT_CHARGE_EXEMPT_REASON", length = 1000)
  private String statutoryChargeExemptReason;

  /**
   * The land charge registration.
   */
  @Column(name = "LAND_CHARGE_EXEMPT_REASON", length = 1000)
  private String landChargeRegistration;

  /**
   * The registration reference.
   */
  @Column(name = "REGISTRATION_REFERENCE", length = 50)
  private String registrationReference;

  /**
   * Flag to indicate the recovery of the award is time related.
   */
  @Convert(converter = BooleanStringConverter.class)
  @Column(name = "RECOVERY_TIME_RELATED_FLAG", length = 50)
  private Boolean recoveryOfAwardTimeRelated;

  /**
   * The address Line 1 details.
   */
  @Column(name = "ADDRESS_LINE1", length = 100)
  private String addressLine1;

  /**
   * The address Line 2 details.
   */
  @Column(name = "ADDRESS_LINE2", length = 100)
  private String addressLine2;

  /**
   * The address Line 3 details.
   */
  @Column(name = "ADDRESS_LINE3", length = 100)
  private String addressLine3;

  /**
   * The related case outcome.
   */
  @ManyToOne
  @JoinColumn(name = "FK_LAND_CASE_OUTCOME", nullable = false)
  private CaseOutcome caseOutcome;

  /**
   * The related time recovery.
   */
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_LAND_RECOVERY")
  private TimeRecovery timeRecovery;

  /**
   * The liable parties tied to the Award.
   */
  @OneToMany(mappedBy = "landAward", cascade = CascadeType.ALL)
  protected List<LiableParty> liableParties;
}
