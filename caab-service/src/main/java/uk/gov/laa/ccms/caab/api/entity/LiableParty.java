package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Entity to represent a liable party entry in the XXCCMS_LIABLE_PARTY TDS table.
 */
@Entity
@Table(name = "XXCCMS_LIABLE_PARTY", schema = "XXCCMS_PUI")
@EntityListeners(AuditingEntityListener.class)
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_LIABLE_PARTY_S",
    schema = "XXCCMS_PUI")
@Getter
@Setter
@RequiredArgsConstructor
public class LiableParty {

  @Id
  @GeneratedValue(generator = "XXCCMS_LIABLE_PARTY_S")
  private Long id;

  /**
   * Type of the Award.
   */
  @Column(name = "AWARD_TYPE", length = 50)
  private String awardType;

  /**
   * The related opponent.
   */
  @ManyToOne
  @JoinColumn(name = "FK_OPPONENT", nullable = false)
  private Opponent opponent;

  /**
   * Relation with CostAward.
   */
  @ManyToOne
  @JoinColumn(name = "FK_COST_AWARD")
  private CostAward costAward;

  /**
   * Relation with FinancialAward.
   */
  @ManyToOne
  @JoinColumn(name = "FK_FINANCIAL_AWARD")
  private FinancialAward financialAward;

  /**
   * Relation with LandAward.
   */
  @ManyToOne
  @JoinColumn(name = "FK_LAND_AWARD")
  private LandAward landAward;

  /**
   * Relation with OtherAssetAward.
   */
  @ManyToOne
  @JoinColumn(name = "FK_OTHER_ASSET_AWARD")
  private OtherAssetAward otherAssetAward;


  @Embedded
  private AuditTrail auditTrail = new AuditTrail();
}
