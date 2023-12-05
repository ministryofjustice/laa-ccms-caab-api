package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a cost entry entity associated with the "XXCCMS_COST_ENTRY" table.
 *
 * <p>This entity is utilized to manage and persist cost entry data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@Table(name = "XXCCMS_COST_ENTRY")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_COST_ENTRY_S")
@Setter
@Getter
@RequiredArgsConstructor
public class CostEntry implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_COST_ENTRY_S")
  private Long id;

  /**
   * The requested costs.
   */
  @Column(name = "REQUESTED_COSTS", precision = 10, scale = 2)
  private BigDecimal requestedCosts;

  /**
   * The cost category.
   */
  @Column(name = "COST_CATEGORY", length = 50)
  private String costCategory;

  /**
   * The id for this cost entry in EBS.
   */
  @Column(name = "EBS_ID", length = 15)
  private String ebsId;

  /**
   * Has this Cost Entry been seen in EBS. This is to prevent more that one 'Counsel' (CostEntry)
   * being added to the case whilst in the PUI editor. To limit only one new 'Counsel' per PUI
   * amendment. true (default)= its a new Entry - added during a PUI  amendment false         = its
   * been created from an existing EBS case.
   */
  @Column(name = "NEW_ENTRY")
  private Boolean newEntry = Boolean.TRUE;

  /**
   * The LSC ID for the resource these costs belongs to.
   */
  @Column(name = "LSC_RESOURCE_ID", length = 15)
  private String lscResourceId;

  /**
   * The name of the resource these costs belongs to.
   */
  @Column(name = "RESOURCE_NAME", length = 300)
  private String resourceName;

  /**
   * The parent cost structure.
   */
  @ManyToOne
  @JoinColumn(name = "FK_COST_STRUCTURE", nullable = false)
  private CostStructure costStructure;

  @Embedded
  private AuditTrail auditTrail;
}
