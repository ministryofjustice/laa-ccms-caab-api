package uk.gov.laa.ccms.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Embeddable
@Data
@NoArgsConstructor
public class AuditTrail {

    /**
     * created date.
     */
    @Column(name = "CREATED")
    @CreationTimestamp
    private Date created;

    /**
     * modified date.
     */
    @Column(name = "MODIFIED")
    @UpdateTimestamp
    private Date modified;

    /**
     * modified by.
     */
    @Column(name = "MODIFIED_BY")
    private String modifiedBy;

    /**
     * created by.
     */
    @Column(name = "CREATED_BY")
    private String createdBy;

    public AuditTrail(String modifiedBy, String createdBy){
        this.modifiedBy = modifiedBy;
        this.createdBy = createdBy;
    }
}