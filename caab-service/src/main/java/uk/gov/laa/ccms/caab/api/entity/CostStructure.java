package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Represents a cost structure entity associated with the "XXCCMS_COST_STRUCTURE" table.
 *
 * <p>This entity is utilized to manage and persist cost structure data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_COST_STRUCTURE")
@Getter
@Setter
@SequenceGenerator(
        allocationSize = 1,
        sequenceName = "XXCCMS_GENERATED_ID_S",
        name = "XXCCMS_COST_STRUCTURE_S")
@RequiredArgsConstructor
public class CostStructure implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_COST_STRUCTURE_S")
  private Long id;

  @Column(name = "DEFAULT_COST_LIMITATION", precision = 10, scale = 2)
  private BigDecimal defaultCostLimitation = new BigDecimal(0);

  @Column(name = "REQUESTED_COST_LIMITATION", precision = 10, scale = 2)
  private BigDecimal requestedCostLimitation = new BigDecimal(0);

  @Column(name = "GRANTED_COST_LIMITATION", precision = 10, scale = 2)
  private BigDecimal grantedCostLimitation = new BigDecimal(0);

  @OneToMany(mappedBy = "costStructure", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("id ASC ")
  private List<CostEntry> costEntries;

  @Embedded
  private AuditTrail auditTrail = new AuditTrail();


}
