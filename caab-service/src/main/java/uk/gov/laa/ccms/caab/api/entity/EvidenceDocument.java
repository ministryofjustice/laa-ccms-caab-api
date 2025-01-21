package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * Represents an evidence document entity associated with the "XXCCMS_EVIDENCE_DOCUMENTS" table.
 *
 * <p>This entity is utilized to manage and persist evidence document data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_EVIDENCE_DOCUMENTS", schema = "XXCCMS_PUI")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_EVIDENCE_DOCUMENTS_S",
    schema = "XXCCMS_PUI")
@Getter
@Setter
@RequiredArgsConstructor
public class EvidenceDocument implements Serializable {

  /**
   * The unique identifier for the EvidenceDocument.
   */
  @Id
  @GeneratedValue(generator = "XXCCMS_EVIDENCE_DOCUMENTS_S")
  private Long id;

  /**
   * The application or case outcome id to which this evidence relates.
   * Originally a sessionid used by an external document handler.
   */
  @Column(name = "DOCHANDLER_SESSION_ID")
  private String applicationOrOutcomeId;

  /**
   * The related case reference number.
   */
  @Column(name = "CASE_ID", length = 20)
  private String caseReferenceNumber;

  /**
   * The related provider id.
   */
  @Column(name = "PROVIDER_ID", length = 19)
  private String providerId;

  /**
   * The type of document.
   */
  @Column(name = "DOCUMENT_TYPE", length = 50)
  private String documentType;

  /**
   * The display value for the document type.
   */
  @Column(name = "TYPE_DISPLAY_VALUE", length = 50)
  private String documentTypeDisplayValue;

  /**
   * The description of the file.
   */
  @Column(name = "USER_FREE_TEXT")
  private String description;

  /**
   * The document sender.
   */
  @Column(name = "DOCUMENT_SENDER", length = 50)
  private String documentSender;

  /**
   * The document file name.
   */
  @Column(name = "FILE_NAME")
  private String fileName;

  /**
   * The extension of the file.
   * This is automatically populated via an INSERT trigger on XXCCMS_EVIDENCE_DOCUMENTS.
   */
  @Column(name = "FILE_EXTENSION", length = 20)
  private String fileExtension;

  /**
   * The document file data.
   */
  @Column(name = "DOCUMENT")
  @Lob
  private byte[] fileBytes;

  /**
   * The status of the file transfer.
   */
  @Column(name = "TRANSFER_STATUS")
  private String transferStatus;

  /**
   * The transfer response code.
   */
  @Column(name = "WEBSERVICE_RESPONSE_CODE")
  private String transferResponseCode;

  /**
   * The transfer response description.
   */
  @Column(name = "WEBSERVICE_RESPONSE_DESC")
  private String transferResponseDescription;

  /**
   * The number of attempts to transfer the file.
   * Possibly legacy attribute from dochandler process.
   */
  @Column(name = "NO_OF_RETRIES_UNDERTAKEN")
  private Integer transferRetryCount;

  /**
   * The CCMS Module to which this evidence document relates.
   */
  @Column(name = "CCMS_MODULE")
  private String ccmsModule;

  /**
   * A delimited list of descriptions of the evidence represented by this document.
   */
  @Column(name = "EVIDENCE_DOC_DESCS")
  private String evidenceDescriptions;

  /**
   * The pre-registered document id for this evidence.
   */
  @Column(name = "REGISTERED_DOCUMENT_ID")
  private String registeredDocumentId;

  /**
   * The reference of the related notification, if relevant.
   */
  @Column(name = "NOTIFICATION_REFERENCE")
  private String notificationReference;

  /**
   * The audit trail information.
   */
  @Embedded
  @AttributeOverrides({
      @AttributeOverride(name = "created",
          column = @Column(name = "DATE_CREATED")),
      @AttributeOverride(name = "createdBy",
          column = @Column(name = "USER_CREATED")),
      @AttributeOverride(name = "lastSaved",
          column = @Column(name = "DATE_MODIFIED")),
      @AttributeOverride(name = "lastSavedBy",
          column = @Column(name = "USER_MODIFIED"))
  })
  private AuditTrail auditTrail = new AuditTrail();
}
