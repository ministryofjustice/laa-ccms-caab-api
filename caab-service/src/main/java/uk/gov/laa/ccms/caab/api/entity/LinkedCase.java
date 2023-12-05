package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
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
 * Represents a linked case entity associated with the "XXCCMS_LINKED_CASE" table.
 *
 * <p>This entity is utilized to manage and persist linked case data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@Table(name = "XXCCMS_LINKED_CASE")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_LINKED_CASE_S")
@Getter
@Setter
@RequiredArgsConstructor
public class LinkedCase implements Serializable {

  /**
   * serialization number.
   */
  private static final long serialVersionUID = 3263016106758049766L;

  @Id
  @GeneratedValue(generator = "XXCCMS_LINKED_CASE_S")
  private Long id;

  @Embedded
  private AuditTrail auditTrail;

  @ManyToOne
  @JoinColumn(name = "FK_APPLICATION", nullable = false)
  private Application application;

  /**
   * Id of the referenced case.
   */
  @Column(name = "LSC_CASE_REFERENCE", length = 35)
  private String lscCaseReference;

  /**
   * Nature of the relation to the case/application.
   */
  @Column(name = "RELATION_TO_LINKED_CASE", length = 50)
  private String relationToCase;

  /**
   * providerCaseReference.
   */
  @Column(name = "PROVIDER_CASE_REFERENCE", length = 35)
  private String providerCaseReference;

  /**
   * feeEarner.
   */
  @Column(name = "FEE_EARNER", length = 300)
  private String feeEarner;

  /**
   * status.
   */
  @Column(name = "STATUS", length = 50)
  private String status;


  /**
   * client reference.
   */
  @Column(name = "CLIENT_REFERENCE", length = 30)
  @Access(AccessType.PROPERTY)
  private String clientReference;

  /**
   * client firstname.
   */
  @Column(name = "CLIENT_FIRST_NAME", length = 35)
  @Access(AccessType.PROPERTY)
  private String clientFirstName;

  /**
   * client surname.
   */
  @Column(name = "CLIENT_SURNAME", length = 35)
  @Access(AccessType.PROPERTY)
  private String clientSurname;


}
