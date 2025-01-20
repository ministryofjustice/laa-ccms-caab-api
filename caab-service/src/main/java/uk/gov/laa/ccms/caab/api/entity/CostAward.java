package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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

/**
 * Entity to represent a cost award entry in the XXCCMS_COST_AWARD TDS table.
 */
@Entity
@Table(name = "XXCCMS_COST_AWARD", schema = "XXCCMS_PUI")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_COST_AWARD_S",
    schema = "XXCCMS_PUI")
@Getter
@Setter
@RequiredArgsConstructor
public class CostAward extends BaseAward {


  @Id
  @GeneratedValue(generator = "XXCCMS_COST_AWARD_S")
  private Long id;

  /**
   * Court Assessment Status.
   */
  @Column(name = "COURT_ASSESSMENT_STATUS", length = 50)
  private String courtAssessmentStatus;

  /**
   * Pre certificate award LSC cost.
   */
  @Column(name = "PRECERTIFICATE_LSC_COST", precision = 10, scale = 2)
  private BigDecimal preCertificateLscCost;

  /**
   * Pre certificate award other cost.
   */
  @Column(name = "PRECERTIFICATE_OTHER_COST", precision = 10, scale = 2)
  private BigDecimal preCertificateOtherCost;

  /**
   * Certificate cost rate LSC.
   */
  @Column(name = "CERTIFICATE_COST_LSC", precision = 10, scale = 2)
  private BigDecimal certificateCostLsc;

  /**
   * Certificate cost rate market.
   */
  @Column(name = "CERTIFICATE_COST_MARKET", precision = 10, scale = 2)
  private BigDecimal certificateCostMarket;

  /**
   * Interest awarded rate.
   */
  @Column(name = "INTEREST_AWARDED_RATE", precision = 10, scale = 2)
  private BigDecimal interestAwardedRate;

  /**
   * The date the order was served.
   */
  @Column(name = "ORDER_DATE_SERVED")
  private Date orderServedDate;

  /**
   * Interest start date.
   */
  @Column(name = "INTEREST_START_DATE")
  private Date interestStartDate;

  /**
   * Other details.
   */
  @Column(name = "OTHER_DETAILS", length = 1000)
  private String otherDetails;

  /**
   * Address line 1.
   */
  @Column(name = "ADDRESS_LINE1", length = 100)
  private String addressLine1;

  /**
   * Address line 2.
   */
  @Column(name = "ADDRESS_LINE2", length = 100)
  private String addressLine2;

  /**
   * Address Line 3.
   */
  @Column(name = "ADDRESS_LINE3", length = 100)
  private String addressLine3;

  /**
   * The related case outcome.
   */
  @ManyToOne
  @JoinColumn(name = "FK_COST_CASE_OUTCOME", nullable = false)
  private CaseOutcome caseOutcome;

  /**
   * The related recovery.
   */
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_COST_RECOVERY")
  private Recovery recovery;

  /**
   * liable parties tied to the Award.
   */
  @OneToMany(mappedBy = "costAward", cascade = CascadeType.ALL)
  protected List<LiableParty> liableParties;
}
