package uk.gov.laa.ccms.api.entity;

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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents a proceeding entity associated with the "XXCCMS_PROCEEDING" table.
 *
 * <p>This entity is utilized to manage and persist proceeding data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
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

  @Column(name = "STAGE", length = 10)
  private String stage;

  @Embedded
  private AuditTrail auditTrail;

  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;

  //TO BE CONTINUED IN FURTHER STORY

}
