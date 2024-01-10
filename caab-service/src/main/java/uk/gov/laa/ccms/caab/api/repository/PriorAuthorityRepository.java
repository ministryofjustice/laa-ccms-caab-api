package uk.gov.laa.ccms.caab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.caab.api.entity.PriorAuthority;
import uk.gov.laa.ccms.caab.api.entity.Proceeding;

/**
 * Repository interface for managing {@link uk.gov.laa.ccms.caab.api.entity.Application} entities.
 *
 * <p>This interface provides CRUD (Create, Read, Update, Delete) operations
 * for the {@link uk.gov.laa.ccms.caab.api.entity.PriorAuthority} entity, leveraging the power of
 * Spring Data JPA. It automatically provides methods to save, find, and delete applications,
 * amongst other common operations found within
 * {@link org.springframework.data.jpa.repository.JpaRepository}.</p>
 */
public interface PriorAuthorityRepository extends JpaRepository<PriorAuthority, Long> {

}
