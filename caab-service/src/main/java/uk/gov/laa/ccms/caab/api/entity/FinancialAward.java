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
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Entity to represent a financial award entry in the XXCCMS_FINANCIAL_AWARD TDS table.
 */
@Entity
@Table(name = "XXCCMS_FINANCIAL_AWARD")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_FINANCIAL_AWARD_S")
@Getter
@Setter
@RequiredArgsConstructor
public class FinancialAward extends BaseAward {

  @Id
  @GeneratedValue(generator = "XXCCMS_COST_AWARD_S")
  private Long id;

  /**
   * The interim award details.
   */
  @Column(name = "INTERIM_AWARD", length = 50)
  private String interimAward;

  /**
   * The award justifications.
   */
  @Column(name = "AWARD_JUSTIFICATIONS", length = 1000)
  private String awardJustifications;

  /**
   * The date the order was served.
   */
  @Column(name = "DATE_ORDER_SERVED")
  private Date orderServedDate;

  /**
   * The statutory charge exemption reason.
   */
  @Column(name = "STATUTORY_CHARGE_EXMP_REASON", length = 1000)
  private String statutoryChargeExemptReason;

  /**
   * The other details for the award.
   */
  @Column(name = "OTHER_DETAILS", length = 50)
  private String otherDetails;

  /**
   * The address line 1 details.
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
  @JoinColumn(name = "FK_FINANCIAL_CASE_OUTCOME", nullable = false)
  private CaseOutcome caseOutcome;

  /**
   * The related recovery.
   */
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "FK_COST_RECOVERY")
  private Recovery recovery;

  /**
   * The liable parties tied to the Award.
   */
  @OneToMany(mappedBy = "financialAward", cascade = CascadeType.ALL)
  protected List<LiableParty> liableParties;

}
