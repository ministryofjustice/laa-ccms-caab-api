package uk.gov.laa.ccms.caab.api.entity;

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
 * Represents a notification attachment entity associated with the "XXCCMS_NOTIFICAITON_ATTACHMENT"
 * table.
 *
 * <p>This entity is utilized to manage and persist notification attachment data
 * within the CCMS system. It makes use of the "XXCCMS_GENERATED_ID_S"
 * sequence for generating unique identifiers.</p>
 */
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "XXCCMS_NOTIFICATION_ATTACHMENT")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_NOTIFICATION_ATTACHMENT_S")
@Getter
@Setter
@RequiredArgsConstructor
public class NotificationAttachment implements Serializable {

  /**
   * The unique identifier for the NotificationAttachment.
   */
  @Id
  @GeneratedValue(generator = "XXCCMS_NOTIFICATION_ATTACHMENT_S")
  private Long id;

  /**
   * ID for the notification this attachment belongs to.
   */
  @Column(name = "NOTIFICATION_REFERENCE")
  private String notificationReference;

  /**
   * The related provider ID.
   */
  @Column(name = "PROVIDER_ID", length = 19)
  private String providerId;

  /**
   * The number for this attachment.
   */
  @Column(name = "NOTIFICATION_NUMBER")
  private Long number;

  /**
   * The type of document.
   */
  @Column(name = "TYPE", length = 50)
  private String documentType;

  /**
   * The display value for the document type.
   */
  @Column(name = "TYPE_DISPLAY_VALUE", length = 50)
  private String documentTypeDisplayValue;

  /**
   * Whether the document is postal or electronic.
   */
  @Column(name = "SEND_BY", length = 50)
  private String sendBy;

  /**
   * The description of the file.
   */
  @Column(name = "DESCRIPTION")
  private String description;

  /**
   * The status of the document.
   */
  @Column(name = "STATUS", length = 50)
  private String status;

  /**
   * The document file name.
   */
  @Column(name = "FILE_NAME")
  private String fileName;

  /**
   * The document file data.
   */
  @Column(name = "FILE_BYTES")
  @Lob
  private byte[] fileBytes;

  /**
   * Audit trail info.
   */
  @Embedded
  private AuditTrail auditTrail = new AuditTrail();

}
