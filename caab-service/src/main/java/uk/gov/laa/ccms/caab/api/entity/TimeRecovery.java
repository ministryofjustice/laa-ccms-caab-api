package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
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


/**
 * Entity to represent a time recovery entry in the XXCCMS_TIME_RECOVERY TDS table.
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_TIME_RECOVERY")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_TIME_RECOVERY_S")
@Getter
@Setter
@RequiredArgsConstructor
public class TimeRecovery {

  @Id
  @GeneratedValue(generator = "XXCCMS_TIME_RECOVERY_S")
  private Long id;

  /**
   * Type of the award.
   */
  @Column(name = "AWARD_TYPE", length = 50)
  private String awardType;

  /**
   * The time recovery description.
   */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  /**
   * The award Amount.
   */
  @Column(name = "AWARD_AMT", precision = 10, scale = 2)
  private BigDecimal awardAmount;

  /**
   * The triggering event.
   */
  @Column(name = "TRIGGERING_EVENT", length = 1000)
  private String triggeringEvent;

  /**
   * The effective date.
   */
  @Column(name = "EFFECTIVE_DATE")
  private Date effectiveDate;

  /**
   * The time-related recovery details.
   */
  @Column(name = "TIME_RELATED_RECOVERY_DETAILS", length = 1000)
  private String timeRelatedRecoveryDetails;


  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();
}
