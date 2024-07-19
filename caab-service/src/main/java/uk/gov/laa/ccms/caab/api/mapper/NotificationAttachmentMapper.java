package uk.gov.laa.ccms.caab.api.mapper;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import uk.gov.laa.ccms.caab.api.entity.NotificationAttachment;
import uk.gov.laa.ccms.caab.model.BaseNotificationAttachmentDetail;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetail;
import uk.gov.laa.ccms.caab.model.NotificationAttachmentDetails;

/**
 * Interface responsible for mapping and transforming objects related
 * to the notification attachment domain. It bridges the gap between the data model
 * and the service or API layers, ensuring consistent object translation.
 */

@Mapper(componentModel = "spring",
    uses = CommonMapper.class)
public interface NotificationAttachmentMapper extends FileDataMapper {
  
  NotificationAttachmentDetails toNotificationAttachmentDetails(
      final Page<NotificationAttachment> notificationAttachments);

  @Mapping(target = "documentType.id", source = "documentType")
  @Mapping(target = "documentType.displayValue", source = "documentTypeDisplayValue")
  BaseNotificationAttachmentDetail toBaseNotificationAttachmentDetail(
      final uk.gov.laa.ccms.caab.api.entity.NotificationAttachment notificationAttachment);

  @Mapping(target = "documentType.id", source = "documentType")
  @Mapping(target = "documentType.displayValue", source = "documentTypeDisplayValue")
  @Mapping(target = "fileData", source = "fileBytes")
  NotificationAttachmentDetail toNotificationAttachmentDetail(
      final uk.gov.laa.ccms.caab.api.entity.NotificationAttachment notificationAttachment);

  @InheritInverseConfiguration
  @Mapping(target = "auditTrail", ignore = true)
  uk.gov.laa.ccms.caab.api.entity.NotificationAttachment toNotificationAttachment(
      final NotificationAttachmentDetail notificationAttachmentDetail);
  
}
