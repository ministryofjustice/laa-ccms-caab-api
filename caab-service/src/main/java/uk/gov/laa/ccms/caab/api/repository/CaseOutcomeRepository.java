package uk.gov.laa.ccms.caab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;

/**
 * Repository interface for managing {@link CaseOutcome} entities.
 *
 * <p>This interface provides CRUD (Create, Read, Update, Delete) operations
 * for the {@link CaseOutcome} entity, leveraging the power of
 * Spring Data JPA.It automatically provides methods to save, find, and delete case outcomes,
 * amongst other common operations found within
 * {@link JpaRepository}.</p>
 */
public interface CaseOutcomeRepository extends JpaRepository<CaseOutcome, Long> {

}
