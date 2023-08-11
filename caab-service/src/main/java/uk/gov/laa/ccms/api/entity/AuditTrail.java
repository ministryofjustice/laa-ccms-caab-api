package uk.gov.laa.ccms.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

  public AuditTrail(String modifiedBy, String createdBy) {
    this.modifiedBy = modifiedBy;
    this.createdBy = createdBy;
  }
}
