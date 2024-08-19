package uk.gov.laa.ccms.caab.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildNotificationAttachment;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildNotificationAttachmentDetail;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import uk.gov.laa.ccms.caab.api.entity.NotificationAttachment;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.NotificationAttachmentMapper;
import uk.gov.laa.ccms.caab.api.repository.NotificationAttachmentRepository;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetail;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetails;

@ExtendWith(MockitoExtension.class)
public class NotificationAttachmentServiceTest {

  @Mock
  private NotificationAttachmentRepository repository;

  @Mock
  private NotificationAttachmentMapper mapper;

  @InjectMocks
  private NotificationAttachmentService notificationAttachmentService;

  @Test
  void createNotificationAttachment_createsNotificationAttachment() {
    NotificationAttachmentDetail notificationAttachmentDetail = new NotificationAttachmentDetail();
    NotificationAttachment notificationAttachment = buildNotificationAttachment();

    when(mapper.toNotificationAttachment(notificationAttachmentDetail)).thenReturn(notificationAttachment);
    when(repository.save(notificationAttachment)).thenReturn(notificationAttachment);

    Long result = notificationAttachmentService.createNotificationAttachment(notificationAttachmentDetail);

    assertNotNull(result);
    assertEquals(notificationAttachment.getId(), result);

    verify(mapper).toNotificationAttachment(notificationAttachmentDetail);
    verify(repository).save(notificationAttachment);
  }

  @Test
  void updateNotificationAttachment_updatesNotificationAttachment() {
    NotificationAttachmentDetail notificationAttachmentDetail = new NotificationAttachmentDetail();
    NotificationAttachment notificationAttachment = buildNotificationAttachment();

    when(mapper.toNotificationAttachment(notificationAttachmentDetail)).thenReturn(notificationAttachment);
    when(repository.save(notificationAttachment)).thenReturn(notificationAttachment);

    notificationAttachmentService.updateNotificationAttachment(notificationAttachmentDetail);

    verify(mapper).toNotificationAttachment(notificationAttachmentDetail);
    verify(repository).save(notificationAttachment);
  }

  @Test
  void getNotificationAttachmentDetails_queriesByExample() {
    NotificationAttachment notificationAttachment = buildNotificationAttachment();

    NotificationAttachmentDetails expectedResponse = new NotificationAttachmentDetails();

    Page<NotificationAttachment> notificationAttachmentPage = new PageImpl<>(
        List.of(buildNotificationAttachment()));

    when(repository.findAll(any(Example.class),
        eq(Pageable.unpaged()))).thenReturn(notificationAttachmentPage);
    when(mapper.toNotificationAttachmentDetails(notificationAttachmentPage)).thenReturn(expectedResponse);

    NotificationAttachmentDetails result = notificationAttachmentService.getNotificationAttachments(
        notificationAttachment.getNotificationReference(),
        notificationAttachment.getProviderId(),
        notificationAttachment.getDocumentType(),
        notificationAttachment.getSendBy(),
        Pageable.unpaged());

    assertNotNull(result);
    assertEquals(expectedResponse, result);

    verify(repository).findAll(any(Example.class), eq(Pageable.unpaged()));
    verify(mapper).toNotificationAttachmentDetails(notificationAttachmentPage);
  }

  @Test
  void getNotificationAttachmentDetail_retrievesCorrectly() {
    NotificationAttachmentDetail notificationAttachmentDetail = buildNotificationAttachmentDetail();

    NotificationAttachment notificationAttachment = buildNotificationAttachment();

    when(repository.findById(notificationAttachmentDetail.getId().longValue())).thenReturn(Optional.of(notificationAttachment));
    when(mapper.toNotificationAttachmentDetail(notificationAttachment)).thenReturn(notificationAttachmentDetail);

    NotificationAttachmentDetail result = notificationAttachmentService.getNotificationAttachment(notificationAttachmentDetail.getId().longValue());

    assertNotNull(result);
    assertEquals(notificationAttachmentDetail, result);

    verify(repository).findById(notificationAttachmentDetail.getId().longValue());
    verify(mapper).toNotificationAttachmentDetail(notificationAttachment);
  }

  @Test
  void removeNotificationAttachment_whenExists_removesEntry() {
    Long notificationAttachmentId = 1L;
    when(repository.existsById(notificationAttachmentId)).thenReturn(true);
    doNothing().when(repository).deleteById(notificationAttachmentId);

    notificationAttachmentService.removeNotificationAttachment(notificationAttachmentId);

    verify(repository).existsById(notificationAttachmentId);
    verify(repository).deleteById(notificationAttachmentId);
  }

  @Test
  void removeNotificationAttachment_whenNotExists_throwsException() {
    Long notificationAttachmentId = 1L;
    when(repository.existsById(notificationAttachmentId)).thenReturn(false);

    CaabApiException exception = assertThrows(CaabApiException.class, () ->
        notificationAttachmentService.removeNotificationAttachment(notificationAttachmentId));

    assertEquals("NotificationAttachment with id: 1 not found", exception.getMessage());
    assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
  }

  @Test
  void removeNotificationAttachmentDetails_deletesBasedOnExample() {
    NotificationAttachment notificationAttachment = buildNotificationAttachment();

    List<NotificationAttachment> notificationAttachments =
        List.of(buildNotificationAttachment());

    when(repository.findAll(any(Example.class))).thenReturn(notificationAttachments);

    notificationAttachmentService.removeNotificationAttachments(
        notificationAttachment.getNotificationReference(),
        notificationAttachment.getProviderId(),
        notificationAttachment.getDocumentType(),
        notificationAttachment.getSendBy());

    verify(repository).findAll(any(Example.class));
    verify(repository).deleteAll(notificationAttachments);

  }
  
}
