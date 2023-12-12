package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
@Table(name = "XXCCMS_PROCEEDING")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_PROCEEDING_S")
@Getter
@Setter
@RequiredArgsConstructor
public class Proceeding implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_PROCEEDING_S")
  private Long id;

  @Embedded
  private AuditTrail auditTrail = new AuditTrail();

  @Column(name = "EBS_ID", length = 50)
  private String ebsId;

  @Column(name = "MATTER_TYPE", length = 50)
  private String matterType;

  @Column(name = "MATTER_TYPE_DISPLAY_VALUE", length = 50)
  private String matterTypeDisplayValue;

  @Column(name = "PROCEEDING_TYPE", length = 50)
  private String proceedingType;

  @Column(name = "PROCEEDING_TYPE_DISPLAY_VALUE", length = 542)
  private String proceedingTypeDisplayValue;

  @Column(name = "LEVEL_OF_SERVICE", length = 50)
  private String levelOfService;

  @Column(name = "LEVEL_OF_SERVICE_DISPLAY_VALUE", length = 50)
  private String levelOfServiceDisplayValue;

  @Column(name = "CLIENT_INVOLVEMENT", length = 50)
  private String clientInvolvement;

  @Column(name = "CLIENT_DISPLAY_VALUE", length = 50)
  private String clientInvolvementDisplayValue;

  @Column(name = "TYPE_OF_ORDER", length = 50)
  private String typeOfOrder;

  @Column(name = "COST_LIMITATION", precision = 10, scale = 2)
  private BigDecimal costLimitation;

  @Column(name = "STATUS", length = 50)
  private String status;

  @Column(name = "DISPLAY_STATUS", length = 50)
  private String displayStatus;

  @Column(name = "DESCRIPTION", length = 542)
  private String description;

  @Column(name = "DATE_GRANTED")
  private Date dateGranted;
  @Column(name = "DATE_COSTS_VALID")
  private Date dateCostsValid;

  /**
   * Indicates if a proceeding has been edited in TDS, will be set to true if saved through the
   * updater. Will be set to false when coming from EBS.
   */
  @Column(name = "EDITED")
  private boolean edited;

  @OneToMany(mappedBy = "proceeding", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id ASC ")
  private List<ScopeLimitation> scopeLimitations;

  @Column(name = "DEFAULT_SCOPE_LIMITATION")
  private String defaultScopeLimitation;

  @Column(name = "STAGE", length = 10)
  private String stage;

  @Column(name = "LEAD_PROCEEDING_IND")
  private boolean leadProceedingInd = false;
  @Column(name = "LAR_SCOPE", length = 50)
  private String larScope;

  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;


}
