package uk.gov.laa.ccms.caab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.caab.api.entity.Proceeding;

/**
 * Repository interface for managing {@link uk.gov.laa.ccms.caab.api.entity.Proceeding} entities.
 *
 * <p>This interface provides CRUD (Create, Read, Update, Delete) operations
 * for the {@link uk.gov.laa.ccms.caab.api.entity.Proceeding} entity, leveraging the power of
 * Spring Data JPA. It automatically provides methods to save, find, and delete proceedings,
 * amongst other common operations found within
 * {@link org.springframework.data.jpa.repository.JpaRepository}.</p>
 */
public interface ProceedingRepository extends JpaRepository<Proceeding, Long> {

}
