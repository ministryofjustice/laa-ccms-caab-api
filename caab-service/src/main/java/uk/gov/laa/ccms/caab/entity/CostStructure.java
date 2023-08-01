package uk.gov.laa.ccms.caab.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "XXCCMS_COST_STRUCTURE")
@Getter
@Setter
@SequenceGenerator(allocationSize = 1, sequenceName = "XXCCMS_GENERATED_ID_S", name = "XXCCMS_COST_STRUCTURE_S")
public class CostStructure implements Serializable {

    @Id
    @GeneratedValue(generator = "XXCCMS_COST_STRUCTURE_S")
    private Long id;

    @Column(name = "DEFAULT_COST_LIMITATION", precision = 10, scale = 2)
    private BigDecimal defaultCostLimitation;

    @Column(name = "REQUESTED_COST_LIMITATION", precision = 10, scale = 2)
    private BigDecimal requestedCostLimitation;

    @Column(name = "GRANTED_COST_LIMITATION", precision = 10, scale = 2)
    private BigDecimal grantedCostLimitation;

    @Embedded
    private AuditTrail auditTrail = new AuditTrail();


}
