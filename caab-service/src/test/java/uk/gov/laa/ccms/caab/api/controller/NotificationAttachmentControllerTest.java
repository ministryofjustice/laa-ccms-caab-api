package uk.gov.laa.ccms.caab.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildNotificationAttachmentDetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.laa.ccms.caab.api.service.NotificationAttachmentService;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetail;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetails;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
public class NotificationAttachmentControllerTest {

  private final String caabUserLoginId = "userLoginId";
  @Mock
  private NotificationAttachmentService notificationAttachmentService;
  @InjectMocks
  private NotificationAttachmentController notificationAttachmentController;
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    mockMvc = standaloneSetup(notificationAttachmentController).setCustomArgumentResolvers(
        new PageableHandlerMethodArgumentResolver()).build();

    objectMapper = new ObjectMapper();
  }

  @Test
  public void removeNotificationAttachment_RemovesEntry() throws Exception {
    Long notificationAttachmentId = 2L;

    doNothing().when(notificationAttachmentService).removeNotificationAttachment(notificationAttachmentId);

    this.mockMvc.perform(
            delete("/notification-attachments/{notification-attachment-id}", notificationAttachmentId)
                .header("Caab-User-Login-Id", caabUserLoginId))
        .andExpect(status().isNoContent());

    verify(notificationAttachmentService).removeNotificationAttachment(notificationAttachmentId);
  }

  @Test
  public void getNotificationAttachments_returnsCorrectly() throws Exception {
    NotificationAttachmentDetail notificationAttachmentDetail = buildNotificationAttachmentDetail();

    when(
        notificationAttachmentService.getNotificationAttachments(
            eq(notificationAttachmentDetail.getNotificationReference()),
            eq(notificationAttachmentDetail.getProviderId()),
            eq(notificationAttachmentDetail.getDocumentType().getId()),
            eq(notificationAttachmentDetail.getSendBy()),
            any(Pageable.class))).thenReturn(
        new NotificationAttachmentDetails());

    this.mockMvc.perform(get("/notification-attachments")
            .param("notification-reference", notificationAttachmentDetail.getNotificationReference())
            .param("provider-id", notificationAttachmentDetail.getProviderId())
            .param("document-type", notificationAttachmentDetail.getDocumentType().getId())
            .param("send-by", notificationAttachmentDetail.getSendBy()))
        .andDo(print())
        .andExpect(status().isOk());

    verify(notificationAttachmentService).getNotificationAttachments(
        eq(notificationAttachmentDetail.getNotificationReference()),
        eq(notificationAttachmentDetail.getProviderId()),
        eq(notificationAttachmentDetail.getDocumentType().getId()),
        eq(notificationAttachmentDetail.getSendBy()),
        any(Pageable.class));
  }

  @Test
  public void getNotificationAttachment_returnsCorrectly() throws Exception {
    NotificationAttachmentDetail notificationAttachmentDetail = buildNotificationAttachmentDetail();

    when(
        notificationAttachmentService.getNotificationAttachment(notificationAttachmentDetail.getId().longValue())).thenReturn(
        new NotificationAttachmentDetail());

    this.mockMvc.perform(
            get("/notification-attachments/{notification-attachment-id}", notificationAttachmentDetail.getId().longValue()))
        .andDo(print()).andExpect(status().isOk());

    verify(notificationAttachmentService).getNotificationAttachment(notificationAttachmentDetail.getId().longValue());
  }

  @Test
  public void createNotificationAttachment_isCreated() throws Exception {
    NotificationAttachmentDetail notificationAttachmentDetail = buildNotificationAttachmentDetail();

    when(notificationAttachmentService.createNotificationAttachment(notificationAttachmentDetail)).thenReturn(
        notificationAttachmentDetail.getId().longValue());

    this.mockMvc.perform(post("/notification-attachments").header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(notificationAttachmentDetail)))
        .andExpect(status().isCreated()).andExpect(
            header().string("Location", "http://localhost/notification-attachments/" + notificationAttachmentDetail.getId()));
  }

  @Test
  public void updateNotificationAttachment_returnsCorrectly() throws Exception {
    NotificationAttachmentDetail notificationAttachmentDetail = buildNotificationAttachmentDetail();

    this.mockMvc.perform(put("/notification-attachments/{notification-attachment-id}", 123L).header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(notificationAttachmentDetail)))
        .andExpect(status().isNoContent());

    verify(notificationAttachmentService).updateNotificationAttachment(notificationAttachmentDetail);
  }


  @Test
  public void removeNotificationAttachments_callsServiceMethod() throws Exception {
    NotificationAttachmentDetail notificationAttachmentDetail = buildNotificationAttachmentDetail();

    this.mockMvc.perform(delete("/notification-attachments")
            .header("Caab-User-Login-Id", caabUserLoginId)
            .param("notification-reference", notificationAttachmentDetail.getNotificationReference())
            .param("provider-id", notificationAttachmentDetail.getProviderId())
            .param("document-type", notificationAttachmentDetail.getDocumentType().getId())
            .param("send-by", notificationAttachmentDetail.getSendBy()))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(notificationAttachmentService).removeNotificationAttachments(
        notificationAttachmentDetail.getNotificationReference(),
        notificationAttachmentDetail.getProviderId(),
        notificationAttachmentDetail.getDocumentType().getId(),
        notificationAttachmentDetail.getSendBy());
  }
  
}
