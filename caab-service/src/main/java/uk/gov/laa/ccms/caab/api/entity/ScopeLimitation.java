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
import org.hibernate.annotations.LazyToOne;
import org.hibernate.annotations.LazyToOneOption;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a scope limitation entity associated with the "XXCCMS_SCOPE_LIMITATION" table.
 *
 * <p>This entity is utilized to manage and persist scope limitation data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_SCOPE_LIMITATION")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_SCOPE_LIMITATION_S")
@Getter
@Setter
@RequiredArgsConstructor
public class ScopeLimitation {

  /**
   * serialization version.
   */
  private static final long serialVersionUID = 3810058408319016618L;

  @Id
  @GeneratedValue(generator = "XXCCMS_SCOPE_LIMITATION_S")
  private Long id;

  /**
   * Relation with Proceeding.
   */
  @ManyToOne
  @JoinColumn(name = "FK_PROCEEDING", nullable = false)
  private Proceeding proceeding;

  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();

  @Column(name = "SCOPE_LIMITATION", length = 50)
  private String scopeLimitation;
  @Column(name = "EBS_ID", length = 50)
  private String ebsId;

  @Column(name = "SCOPE_LIMITATION_DISPLAY_VALUE", length = 100)
  private String scopeLimitationDisplayValue;
  @Column(name = "SCOPE_LIMITATION_WORDING", length = 1000)
  private String scopeLimitationWording;

  @Column(name = "DEFAULT_IND")
  private boolean defaultInd;

  @Column(name = "DELEGATED_FUNC_APPLY_IND")
  private boolean delegatedFuncApplyInd = false;

}
