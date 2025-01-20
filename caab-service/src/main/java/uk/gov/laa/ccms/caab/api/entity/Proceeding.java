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
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.type.NumericBooleanConverter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a proceeding entity associated with the "XXCCMS_PROCEEDING" table.
 *
 * <p>This entity is utilized to manage and persist proceeding data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_PROCEEDING", schema = "XXCCMS_PUI")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_PROCEEDING_S",
    schema = "XXCCMS_PUI")
@Getter
@Setter
@RequiredArgsConstructor
public class Proceeding implements Serializable {


  /**
   * Unique identifier for the proceeding.
   */
  @Id
  @GeneratedValue(generator = "XXCCMS_PROCEEDING_S")
  private Long id;

  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();

  /**
   * EBS id.
   */
  @Column(name = "EBS_ID", length = 50)
  private String ebsId;

  /**
   * Matter type.
   */
  @Column(name = "MATTER_TYPE", length = 50)
  private String matterType;

  /**
   * Matter type display value.
   */
  @Column(name = "MATTER_TYPE_DISPLAY_VALUE", length = 50)
  private String matterTypeDisplayValue;

  /**
   * Proceeding type.
   */
  @Column(name = "PROCEEDING_TYPE", length = 50)
  private String proceedingType;

  /**
   * Proceeding type display value.
   */
  @Column(name = "PROCEEDING_TYPE_DISPLAY_VALUE", length = 542)
  private String proceedingTypeDisplayValue;

  /**
   *  Level of service.
   */
  @Column(name = "LEVEL_OF_SERVICE", length = 50)
  private String levelOfService;

  /**
   * Level of service display value.
   */
  @Column(name = "LEVEL_OF_SERVICE_DISPLAY_VALUE", length = 50)
  private String levelOfServiceDisplayValue;

  /**
   * Client involvement.
   */
  @Column(name = "CLIENT_INVOLVEMENT", length = 50)
  private String clientInvolvement;

  /**
   * Client involvement display value.
   */
  @Column(name = "CLIENT_DISPLAY_VALUE", length = 50)
  private String clientInvolvementDisplayValue;

  /**
   * Type of order.
   */
  @Column(name = "TYPE_OF_ORDER", length = 50)
  private String typeOfOrder;

  /**
   * Cost limitation.
   */
  @Column(name = "COST_LIMITATION", precision = 10, scale = 2)
  private BigDecimal costLimitation;

  /**
   * Proceeding status.
   */
  @Column(name = "STATUS", length = 50)
  private String status;

  /**
   * Proceeding status display value.
   */
  @Column(name = "DISPLAY_STATUS", length = 50)
  private String displayStatus;

  /**
   * Proceeding description value.
   */
  @Column(name = "DESCRIPTION", length = 542)
  private String description;

  /**
   * Date granted.
   */
  @Column(name = "DATE_GRANTED")
  private Date dateGranted;

  /**
   * Date costs were validated.
   */
  @Column(name = "DATE_COSTS_VALID")
  private Date dateCostsValid;

  /**
   * Indicates if a proceeding has been edited in TDS, will be set to true if saved through the
   * updater. Will be set to false when coming from EBS.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "EDITED")
  private Boolean edited;

  /**
   * Scope limitations.
   */
  @OneToMany(mappedBy = "proceeding", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id ASC ")
  private List<ScopeLimitation> scopeLimitations;

  /**
   * Default scope limitation.
   */
  @Column(name = "DEFAULT_SCOPE_LIMITATION")
  private String defaultScopeLimitation;

  /**
   * Stage.
   */
  @Column(name = "STAGE", length = 10)
  private String stage;

  /**
   * Lead proceeding indicator.
   */
  @Convert(converter = NumericBooleanConverter.class)
  @Column(name = "LEAD_PROCEEDING_IND")
  private Boolean leadProceedingInd;

  /**
   * LAR Scope.
   */
  @Column(name = "LAR_SCOPE", length = 50)
  private String larScope;

  /**
   * Application, the proceeding is associated with.
   */
  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;


}
