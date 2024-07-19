package uk.gov.laa.ccms.caab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import uk.gov.laa.ccms.caab.api.entity.NotificationAttachment;

/**
 * Repository interface for managing {@link NotificationAttachment} entities.
 *
 * <p>This interface provides CRUD (Create, Read, Update, Delete) operations for the {@link
 * NotificationAttachment} entity, leveraging the power of Spring Data JPA. It automatically
 * provides methods to save, find, and delete notification attachment documents, amongst other
 * common operations found within {@link JpaRepository}.
 */
public interface NotificationAttachmentRepository
    extends JpaRepository<NotificationAttachment, Long>,
        JpaSpecificationExecutor<NotificationAttachment> {}
