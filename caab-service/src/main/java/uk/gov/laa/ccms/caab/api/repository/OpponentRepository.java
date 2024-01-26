package uk.gov.laa.ccms.caab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.caab.api.entity.Opponent;

/**
 * Repository interface for managing {@link Opponent}
 * entities.
 *
 * <p>This interface provides CRUD (Create, Read, Update, Delete) operations
 * for the {@link Opponent} entity, leveraging the power of
 * Spring Data JPA. It automatically provides methods to save, find, and delete opponents,
 * amongst other common operations found within
 * {@link JpaRepository}.</p>
 */
public interface OpponentRepository extends JpaRepository<Opponent, Long> {

}
