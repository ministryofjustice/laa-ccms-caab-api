package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

/**
 * Represents an audit trail for various entities within the CCMS system.
 *
 * <p>This embeddable class provides fields to capture the audit details associated
 * with the creation and modification of entities. These details include timestamps
 * for when an entity is created or last modified and user identifiers for who
 * created or last modified the entity.</p>
 *
 * <p>The class is embedded in other entities to ensure they carry the
 * necessary audit information.</p>
 */
@Embeddable
@Data
public class AuditTrail {

  /**
   * created date.
   */
  @Column(name = "CREATED", updatable = false)
  @CreationTimestamp
  private Date created;

  /**
   * modified date.
   */
  @Column(name = "MODIFIED")
  @UpdateTimestamp
  private Date lastSaved;

  /**
   * modified by.
   */
  @LastModifiedBy
  @Column(name = "MODIFIED_BY")
  private String lastSavedBy;

  /**
   * created by.
   */
  @CreatedBy
  @Column(name = "CREATED_BY", updatable = false)
  private String createdBy;

}
