package uk.gov.laa.ccms.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "XXCCMS_COST_STRUCTURE")
@Getter
@Setter
@SequenceGenerator(allocationSize = 1, sequenceName = "XXCCMS_GENERATED_ID_S", name = "XXCCMS_COST_STRUCTURE_S")
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

    @Embedded
    private AuditTrail auditTrail;

    public CostStructure(AuditTrail auditTrail){
        this.auditTrail = auditTrail;
    }


}
