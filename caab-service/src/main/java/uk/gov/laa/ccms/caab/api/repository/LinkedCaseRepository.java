package uk.gov.laa.ccms.caab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.caab.api.entity.LinkedCase;

/**
 * Repository interface for managing {@link uk.gov.laa.ccms.caab.api.entity.LinkedCase} entities.
 *
 * <p>This interface provides CRUD (Create, Read, Update, Delete) operations
 * for the {@link uk.gov.laa.ccms.caab.api.entity.LinkedCase} entity, leveraging the power of
 * Spring Data JPA.It automatically provides methods to save, find, and delete linked cases,
 * amongst other common operations found within
 * {@link org.springframework.data.jpa.repository.JpaRepository}.</p>
 */
public interface LinkedCaseRepository extends JpaRepository<LinkedCase, Long> {

}
