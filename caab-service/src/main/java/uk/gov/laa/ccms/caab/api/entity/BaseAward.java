package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * A base class for common attributes of an Award.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@RequiredArgsConstructor
public abstract class BaseAward {
  /**
   * The ebs id.
   */
  @Column(name = "EBS_ID", length = 15)
  private String ebsId;

  /**
   * Type of the Award.
   */
  @Column(name = "AWARD_TYPE", length = 50)
  private String awardType;

  /**
   * Date of order.
   */
  @Column(name = "DATE_OF_ORDER")
  private Date dateOfOrder;

  /**
   * Awarded By.
   */
  @Column(name = "AWARDED_BY", length = 50)
  private String awardedBy;

  /**
   * Description.
   */
  @Column(name = "DESCRIPTION", length = 50)
  private String description;

  /**
   * Award Amount.
   */
  @Column(name = "AWARD_AMOUNT", precision = 10, scale = 2)
  private BigDecimal awardAmount;

  /**
   * updateAllowed.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "UPDATE_ALLOWED_IND")
  private Boolean updateAllowed;

  /**
   * deleteAllowed.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "DELETE_ALLOWED_IND")
  private Boolean deleteAllowed;

  /**
   * award Code.
   */
  @Column(name = "AWARD_CODE", length = 30)
  private String awardCode;

  /**
   * Audit info for the entity.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();
}
