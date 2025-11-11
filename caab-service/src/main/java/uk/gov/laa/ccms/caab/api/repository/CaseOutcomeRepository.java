package uk.gov.laa.ccms.caab.api.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.entity.ProceedingOutcome;

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

  @Query("select po from ProceedingOutcome po where po.caseOutcome.lscCaseReference = ?1 "
      + "and po.caseOutcome.providerId = ?2 and po.proceedingCaseId = ?3")
  Optional<ProceedingOutcome> findProceedingOutcome(String caseReferenceNumber, String providerId, Long proceedingId);

}
