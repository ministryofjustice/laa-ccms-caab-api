package uk.gov.laa.ccms.api.entity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a prior authority entity associated with the "XXCCMS_PRIOR_AUTHORITY" table.
 *
 * <p>This entity is utilized to manage and persist prior authority data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@Table(name = "XXCCMS_PRIOR_AUTHORITY")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_PRIOR_AUTHORITY_S")
@Getter
@Setter
@RequiredArgsConstructor
public class PriorAuthority implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_PRIOR_AUTHORITY_S")
  private Long id;

  /**
   * audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail;

  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;

  //TO BE CONTINUED IN FURTHER STORY
}
