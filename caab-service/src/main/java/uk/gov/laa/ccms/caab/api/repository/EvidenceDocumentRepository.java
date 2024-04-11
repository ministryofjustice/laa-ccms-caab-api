package uk.gov.laa.ccms.caab.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.caab.api.entity.EvidenceDocument;

/**
 * Repository interface for managing {@link EvidenceDocument}
 * entities.
 *
 * <p>This interface provides CRUD (Create, Read, Update, Delete) operations
 * for the {@link EvidenceDocument} entity, leveraging the power of
 * Spring Data JPA. It automatically provides methods to save, find, and delete evidence documents,
 * amongst other common operations found within
 * {@link JpaRepository}.</p>
 */
public interface EvidenceDocumentRepository extends JpaRepository<EvidenceDocument, Long> {

}
