package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.type.YesNoConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * Represents a prior authority entity associated with the "XXCCMS_PRIOR_AUTHORITY" table.
 *
 * <p>This entity is utilized to manage and persist prior authority data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_PRIOR_AUTHORITY")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_PRIOR_AUTHORITY_S")
@Getter
@Setter
@RequiredArgsConstructor
public class PriorAuthority implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_PRIOR_AUTHORITY_S")
  private Long id;

  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();

  /**
   * Application, the prior authority is associated with.
   */
  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;

  /**
   * EBS id.
   */
  @Column(name = "EBS_ID", length = 50)
  private String ebsId;

  /**
   * Status, also called 'DecisionStatus'.
   */
  @Column(name = "STATUS", length = 50)
  private String status;

  /**
   * Prior Authority type.
   */
  @Column(name = "TYPE", length = 50)
  private String type;

  /**
   * Prior Authority type display value.
   */
  @Column(name = "TYPE_DISPLAY_VALUE", length = 50)
  private String typeDisplayValue;

  /**
   * Prior Authority summary.
   */
  @Column(name = "SUMMARY", length = 50)
  private String summary;

  /**
   * Prior Authority justification.
   */
  @Column(name = "JUSTIFICATION", length = 200)
  private String justification;

  /**
   * Amount requested required or not.
   */
  @Convert(converter = YesNoConverter.class)
  @Column(name = "VALUE_REQUIRED_FLAG", length = 5)
  private Boolean valueRequired;

  /**
   * Prior Authority amount requested.
   */
  @Column(name = "AMOUNT_REQUESTED", precision = 10, scale = 2)
  private BigDecimal amountRequested;

  /**
   * Items from Reference data.
   */
  @OneToMany(mappedBy = "priorAuthority", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id ASC ")
  private List<ReferenceDataItem> items;

}
