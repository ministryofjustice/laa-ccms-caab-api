package uk.gov.laa.ccms.caab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.caab.api.entity.Application;

/**
 * Repository interface for managing {@link Application} entities.
 *
 * <p>This interface provides CRUD (Create, Read, Update, Delete) operations
 * for the {@link Application} entity, leveraging the power of Spring Data JPA.
 * It automatically provides methods to save, find, and delete applications,
 * amongst other common operations found within {@link JpaRepository}.</p>
 */
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
}
