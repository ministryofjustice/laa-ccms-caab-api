package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import uk.gov.laa.ccms.caab.api.entity.converter.BooleanStringConverter;


/**
 * Entity to represent a recovery entry in the XXCCMS_RECOVERY TDS table.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_RECOVERY")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_RECOVERY_S")
@Getter
@Setter
@RequiredArgsConstructor
public class Recovery {
  
  @Id
  @GeneratedValue(generator = "XXCCMS_RECOVERY_S")
  private Long id;

  /**
   * The award amount.
   */
  @Column(name = "AWARD_AMT", precision = 10, scale = 2)
  private BigDecimal awardAmount;

  /**
   * The solicitor recovery amount.
   */
  @Column(name = "SOLICITOR_RECOVERY_AMT", precision = 10, scale = 2)
  private BigDecimal solicitorRecoveryAmount;

  /**
   * The solicitor recovery date.
   */
  @Column(name = "SOLICITOR_RECOVERY_DATE")
  private Date solicitorRecoveryDate;

  /**
   * The solicitor amount paid to LSC.
   */
  @Column(name = "SOLICITOR_AMT_PAID_TO_LSC", precision = 10, scale = 2)
  private BigDecimal solicitorAmountPaidToLsc;

  /**
   * The court recovery amount.
   */
  @Column(name = "COURT_RECOVERY_AMT", precision = 10, scale = 2)
  private BigDecimal courtRecoveryAmount;

  /**
   * The court recovery date.
   */
  @Column(name = "COURT_RECOVERY_DATE")
  private Date courtRecoveryDate;

  /**
   * The court amount paid to LSC.
   */
  @Column(name = "COURT_AMT_PAID_TO_LSC", precision = 10, scale = 2)
  private BigDecimal courtAmountPaidToLsc;

  /**
   * The recovered amount.
   */
  @Column(name = "RECOVERED_AMT", precision = 10, scale = 2)
  private BigDecimal recoveredAmount;

  /**
   * The client recovery date.
   */
  @Column(name = "CLIENT_RECOVERY_DATE")
  private Date clientRecoveryDate;

  /**
   * The client amount paid to LSC.
   */
  @Column(name = "CLIENT_AMT_PAID_TO_LSC", precision = 10, scale = 2)
  private BigDecimal clientAmountPaidToLsc;

  /**
   * The client recovery amount.
   */
  @Column(name = "CLIENT_RECOVERY_AMT", precision = 10, scale = 2)
  private BigDecimal clientRecoveryAmount;

  /**
   * The unrecovered amount.
   */
  @Column(name = "UNRECOVERED_AMT", precision = 10, scale = 2)
  private BigDecimal unrecoveredAmount;

  /**
   * Flag to indicate that leave of court is requested.
   */
  @Convert(converter = BooleanStringConverter.class)
  @Column(name = "LEAVE_OF_COURT_REQD_IND", length = 10)
  private Boolean leaveOfCourtRequiredInd;

  /**
   * The offered amount.
   */
  @Column(name = "OFFERED_AMT", precision = 10, scale = 2)
  private BigDecimal offeredAmount;

  /**
   * The offer details.
   */
  @Column(name = "OFFER_DETAILS", length = 1000)
  private String offerDetails;

  /**
   * The award type.
   */
  @Column(name = "AWARD_TYPE", length = 50)
  private String awardType;

  /**
   * The description of the recovery.
   */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();

}
