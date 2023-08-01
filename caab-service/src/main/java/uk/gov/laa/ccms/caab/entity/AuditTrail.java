package uk.gov.laa.ccms.caab.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Embeddable
@Getter
@Setter
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
}
