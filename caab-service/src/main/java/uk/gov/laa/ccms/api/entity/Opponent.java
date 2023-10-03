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
 * Represents an opponent entity associated with the "XXCCMS_OPPONENT" table.
 *
 * <p>This entity is utilized to manage and persist opponent data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@Table(name = "XXCCMS_OPPONENT")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_OPPONENT_S")
@Getter
@Setter
@RequiredArgsConstructor
public class Opponent implements Serializable {

  @Id
  @GeneratedValue(generator = "XXCCMS_OPPONENT_S")
  private Long id;

  @Embedded
  private AuditTrail auditTrail;

  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;

  @Column(name = "RELATIONSHIP_TO_CASE", length = 50)
  private String relationshipToCase;

  @Column(name = "TYPE", length = 50)
  private String type;

  //TO BE CONTINUED IN FURTHER STORY

}
