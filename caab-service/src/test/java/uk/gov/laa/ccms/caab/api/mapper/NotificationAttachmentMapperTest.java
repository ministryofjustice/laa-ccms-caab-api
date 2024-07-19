package uk.gov.laa.ccms.caab.api.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildNotificationAttachment;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildNotificationAttachmentDetail;

import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uk.gov.laa.ccms.caab.api.entity.NotificationAttachment;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetail;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetails;

@ExtendWith(MockitoExtension.class)
public class NotificationAttachmentMapperTest {

  @InjectMocks
  private final NotificationAttachmentMapper mapper = new NotificationAttachmentMapperImpl();

  @BeforeEach
  public void setup() throws ParseException {
  }

  @Test
  public void testToNotificationAttachmentDetails_null() {
    NotificationAttachmentDetails notificationAttachmentDetails = mapper.toNotificationAttachmentDetails(null);
    assertNull(notificationAttachmentDetails);
  }

  @Test
  public void testToNotificationAttachmentDetails() {
    Page<NotificationAttachment> notificationAttachments = new PageImpl<>(List.of(new NotificationAttachment()));

    NotificationAttachmentDetails result = mapper.toNotificationAttachmentDetails(notificationAttachments);

    assertNotNull(result);
    assertNotNull(result.getContent());
    assertEquals(1, result.getContent().size());
  }

  @Test
  public void testToNotificationAttachmentDetail() {
    // Construct NotificationAttachment
    NotificationAttachment notificationAttachment = buildNotificationAttachment();

    // Convert NotificationAttachment to NotificationAttachmentDetail
    NotificationAttachmentDetail result = mapper.toNotificationAttachmentDetail(notificationAttachment);

    assertNotNull(result);
    assertNotNull(result.getAuditTrail());
    assertEquals(notificationAttachment.getDescription(), result.getDescription());
    assertEquals(notificationAttachment.getDocumentType(), result.getDocumentType().getId());
    assertEquals(notificationAttachment.getDocumentTypeDisplayValue(), result.getDocumentType().getDisplayValue());
    assertEquals(Base64.getEncoder().encodeToString(notificationAttachment.getFileBytes()), result.getFileData());
    assertEquals(notificationAttachment.getFileName(), result.getFileName());
    assertEquals(notificationAttachment.getId().intValue(), result.getId());
    assertEquals(notificationAttachment.getNotificationReference(), result.getNotificationReference());
    assertEquals(notificationAttachment.getProviderId(), result.getProviderId());
    assertEquals(notificationAttachment.getSendBy(), result.getSendBy());
    assertEquals(notificationAttachment.getStatus(), result.getStatus());
    assertEquals(notificationAttachment.getNumber(), result.getNumber());
  }

  @Test
  public void testToNotificationAttachment() {
    // Construct NotificationAttachmentDetail
    NotificationAttachmentDetail notificationAttachmentDetail = buildNotificationAttachmentDetail();

    // Convert NotificationAttachmentDetail to NotificationAttachment
    NotificationAttachment result = mapper.toNotificationAttachment(notificationAttachmentDetail);

    assertNotNull(result);
    assertNotNull(result.getAuditTrail());
    assertEquals(notificationAttachmentDetail.getDescription(), result.getDescription());
    assertEquals(notificationAttachmentDetail.getDocumentType().getId(), result.getDocumentType());
    assertEquals(notificationAttachmentDetail.getDocumentType().getDisplayValue(), result.getDocumentTypeDisplayValue());
    assertArrayEquals(Base64.getDecoder().decode(notificationAttachmentDetail.getFileData()), result.getFileBytes());
    assertEquals(notificationAttachmentDetail.getFileName(), result.getFileName());
    assertEquals(notificationAttachmentDetail.getId().intValue(), result.getId());
    assertEquals(notificationAttachmentDetail.getNotificationReference(), result.getNotificationReference());
    assertEquals(notificationAttachmentDetail.getProviderId(), result.getProviderId());
    assertEquals(notificationAttachmentDetail.getSendBy(), result.getSendBy());
    assertEquals(notificationAttachmentDetail.getStatus(), result.getStatus());
    assertEquals(notificationAttachmentDetail.getNumber(), result.getNumber());
  }
  
}
