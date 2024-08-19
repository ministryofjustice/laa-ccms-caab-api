package uk.gov.laa.ccms.caab.api.controller;

import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.laa.ccms.caab.api.NotificationAttachmentsApi;
import uk.gov.laa.ccms.caab.api.service.NotificationAttachmentService;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetail;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetails;

/**
 * Controller handling notification attachment related requests. Implements the {@code
 * NotificationAttachmentsApi} interface for standardized endpoint definitions and provides the
 * necessary endpoints to manage and retrieve notification attachment data.
 */
@RestController
@RequiredArgsConstructor
public class NotificationAttachmentController implements NotificationAttachmentsApi {

  private final NotificationAttachmentService notificationAttachmentService;

  @Override
  public ResponseEntity<NotificationAttachmentDetails> getNotificationAttachments(
      final String notificationReference,
      final String providerId,
      final String documentType,
      final String sendBy,
      final Pageable pageable) {

    NotificationAttachmentDetails notificationAttachmentDetails =
        notificationAttachmentService.getNotificationAttachments(
            notificationReference, providerId, documentType, sendBy, pageable);

    return new ResponseEntity<>(notificationAttachmentDetails, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<NotificationAttachmentDetail> getNotificationAttachment(
      final Long notificationAttachmentId) {

    NotificationAttachmentDetail notificationAttachmentDetail =
        notificationAttachmentService.getNotificationAttachment(notificationAttachmentId);

    return new ResponseEntity<>(notificationAttachmentDetail, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> createNotificationAttachment(
      final String caabUserLoginId,
      final NotificationAttachmentDetail notificationAttachmentDetail) {

    Long notificationAttachmentId =
        notificationAttachmentService.createNotificationAttachment(notificationAttachmentDetail);

    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(notificationAttachmentId)
            .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(uri);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> updateNotificationAttachment(
      final Long notificationAttachmentId,
      final String caabUserLoginId,
      final NotificationAttachmentDetail notificationAttachmentDetail) {

    notificationAttachmentService.updateNotificationAttachment(notificationAttachmentDetail);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> removeNotificationAttachment(
      final Long notificationAttachmentId,
      final String caabUserLoginId) {

    notificationAttachmentService.removeNotificationAttachment(notificationAttachmentId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> removeNotificationAttachments(
      final String caabUserLoginId,
      final String notificationReference,
      final String providerId,
      final String documentType,
      final String sendBy) {

    notificationAttachmentService.removeNotificationAttachments(
        notificationReference,
        providerId,
        documentType,
        sendBy);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

}
