package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.entity.NotificationAttachment;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.NotificationAttachmentMapper;
import uk.gov.laa.ccms.caab.api.repository.NotificationAttachmentRepository;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetail;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetails;

/**
 * Service responsible for handling notification attachment operations.
 *
 * @see NotificationAttachmentRepository
 * @see NotificationAttachmentMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationAttachmentService {

  private final NotificationAttachmentRepository repository;
  private final NotificationAttachmentMapper mapper;

  /**
   * Get a Page of NotificationAttachments for the supplied search criteria.
   *
   * @param providerId - the provider id.
   * @param documentType - the document type.
   * @param pageable - the pageable settings.
   * @return NotificationAttachmentDetails wrapping a page of NotificationAttachments.
   */
  public NotificationAttachmentDetails getNotificationAttachments(
      final String notificationReference,
      final String providerId,
      final String documentType,
      final String sendBy,
      final Pageable pageable) {

    Example<NotificationAttachment> exampleDocument =
        buildExampleDocument(notificationReference, providerId, documentType, sendBy);

    return mapper.toNotificationAttachmentDetails(repository.findAll(exampleDocument, pageable));
  }

  /**
   * Get a single NotificationAttachmentDetail by id.
   *
   * @param id - the notification attachment id.
   * @return NotificationAttachmentDetail with the matching id, or else an error.
   */
  public NotificationAttachmentDetail getNotificationAttachment(final Long id) {

    return repository
        .findById(id)
        .map(mapper::toNotificationAttachmentDetail)
        .orElseThrow(
            () ->
                new CaabApiException(
                    String.format("Failed to find notification attachment with id: %s", id),
                    HttpStatus.NOT_FOUND));
  }

  /**
   * Create a new NotificationAttachment entry.
   *
   * @param notificationAttachmentDetail - the notification attachment to create.
   * @return the id of the newly created notification attachment.
   */
  public Long createNotificationAttachment(
      final NotificationAttachmentDetail notificationAttachmentDetail) {

    NotificationAttachment createdNotificationAttachment =
        repository.save(mapper.toNotificationAttachment(notificationAttachmentDetail));

    return createdNotificationAttachment.getId();
  }

  /**
   * Removes a notification attachment entry. If the notification attachment is not found, a
   * CaabApiException is thrown.
   *
   * @param notificationAttachmentId The unique identifier of the notification attachment to be
   *     removed.
   * @throws CaabApiException If the notification attachment with the specified ID is not found.
   */
  @Transactional
  public void removeNotificationAttachment(final Long notificationAttachmentId) {

    if (repository.existsById(notificationAttachmentId)) {
      repository.deleteById(notificationAttachmentId);
    } else {
      throw new CaabApiException(
          String.format("NotificationAttachment with id: %s not found", notificationAttachmentId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Remove all notification attachments which match the provided search criteria.
   *
   * @param providerId - the provider id.
   * @param documentType - the document type.
   */
  @Transactional
  public void removeNotificationAttachments(
      final String notificationReference,
      final String providerId,
      final String documentType,
      final String sendBy) {

    Example<NotificationAttachment> exampleDocument =
        buildExampleDocument(notificationReference, providerId, documentType, sendBy);

    repository.deleteAll(repository.findAll(exampleDocument));
  }

  private Example<NotificationAttachment> buildExampleDocument(
      final String notificationReference,
      final String providerId,
      final String documentType,
      final String sendBy) {

    NotificationAttachment exampleDocument = new NotificationAttachment();
    exampleDocument.setNotificationReference(notificationReference);
    exampleDocument.setProviderId(providerId);
    exampleDocument.setDocumentType(documentType);
    exampleDocument.setSendBy(sendBy);
    return Example.of(exampleDocument);
  }
}
