package uk.gov.laa.ccms.caab.api.service;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.convert.QueryByExamplePredicateBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.EvidenceDocument;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.EvidenceMapper;
import uk.gov.laa.ccms.caab.api.repository.EvidenceDocumentRepository;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetails;

/**
 * Service responsible for handling evidence document operations.
 *
 * @see EvidenceDocumentRepository
 * @see EvidenceMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EvidenceService {

  private final EvidenceDocumentRepository repository;
  private final EvidenceMapper mapper;

  /**
   * Get a Page of EvidenceDocuments for the supplied search criteria.
   *
   * @param applicationOrOutcomeId - the application or outcome id.
   * @param caseReferenceNumber    - the case reference number.
   * @param providerId             - the provider id.
   * @param documentType           - the document type.
   * @param ccmsModule             - the ccms module.
   * @param transferPending        - filter on documents which are yet to be transferred.
   * @param pageable               - the pageable settings.
   * @return EvidenceDocumentDetails wrapping a page of EvidenceDocuments.
   */
  public EvidenceDocumentDetails getEvidenceDocuments(final String applicationOrOutcomeId,
      final String caseReferenceNumber,
      final String providerId,
      final String documentType,
      final String ccmsModule,
      final Boolean transferPending,
      final Pageable pageable) {
    Example<EvidenceDocument> exampleDocument = buildExampleDocument(
        applicationOrOutcomeId,
        caseReferenceNumber,
        providerId,
        documentType,
        ccmsModule);

    return mapper.toEvidenceDocumentDetails(
        repository.findAll(
            buildQuerySpecification(exampleDocument, transferPending),
            pageable));
  }

  /**
   * Updates the evidence with the provided details.
   *
   * @param id the ID of the evidence to update
   * @param evidenceDocumentDetail the new details to update the evidence with
   * @throws CaabApiException if the evidence with the given ID is not found
   */
  @Transactional
  public void updateEvidence(
      final Long id,
      final EvidenceDocumentDetail evidenceDocumentDetail) {

    EvidenceDocument evidence = repository.findById(id)
        .orElseThrow(() -> new CaabApiException(
            String.format("Failed to find evidence with id: %s", id),
            HttpStatus.NOT_FOUND));

    mapper.mapIntoEvidence(evidence, evidenceDocumentDetail);
    repository.save(evidence);
  }

  /**
   * Get a single EvidenceDocumentDetail by id.
   *
   * @param id - the evidence document id.
   * @return EvidenceDocumentDetail with the matching id, or else an error.
   */
  public EvidenceDocumentDetail getEvidenceDocument(final Long id) {

    return repository.findById(id)
        .map(mapper::toEvidenceDocumentDetail)
        .orElseThrow(() -> new CaabApiException(
            String.format("Failed to find evidence with id: %s", id),
            HttpStatus.NOT_FOUND));
  }

  /**
   * Create a new EvidenceDocument entry.
   *
   * @param evidenceDocumentDetail - the evidence to create.
   * @return the id of the newly created evidence document.
   */
  public Long createEvidenceDocument(final EvidenceDocumentDetail evidenceDocumentDetail) {
    uk.gov.laa.ccms.caab.api.entity.EvidenceDocument createdEvidenceDocument =
        repository.save(mapper.toEvidenceDocument(evidenceDocumentDetail));

    return createdEvidenceDocument.getId();
  }

  /**
   * Removes an evidence document entry. If the evidence is not found, a CaabApiException is
   * thrown.
   *
   * @param evidenceDocumentId The unique identifier of the evidence to be removed.
   * @throws CaabApiException If the evidence with the specified ID is not found.
   */
  @Transactional
  public void removeEvidenceDocument(final Long evidenceDocumentId) {
    if (repository.existsById(evidenceDocumentId)) {
      repository.deleteById(evidenceDocumentId);
    } else {
      throw new CaabApiException(
          String.format("EvidenceDocument with id: %s not found", evidenceDocumentId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Remove all evidence documents which match the provided search criteria.
   *
   * @param applicationOrOutcomeId - the application or outcome id.
   * @param caseReferenceNumber    - the case reference number.
   * @param providerId             - the provider id.
   * @param documentType           - the document type.
   * @param ccmsModule             - the ccms module.
   * @param transferPending        - filter on documents which are yet to be transferred.
   */
  @Transactional
  public void removeEvidenceDocuments(
      final String applicationOrOutcomeId,
      final String caseReferenceNumber,
      final String providerId,
      final String documentType,
      final String ccmsModule,
      final Boolean transferPending) {
    Example<EvidenceDocument> exampleDocument = buildExampleDocument(
        applicationOrOutcomeId,
        caseReferenceNumber,
        providerId,
        documentType,
        ccmsModule);

    repository.deleteAll(
        repository.findAll(buildQuerySpecification(exampleDocument, transferPending)));
  }

  private Example<EvidenceDocument> buildExampleDocument(
      final String applicationOrOutcomeId,
      final String caseReferenceNumber,
      final String providerId,
      final String documentType,
      final String ccmsModule) {
    EvidenceDocument exampleDocument =
        new EvidenceDocument();
    exampleDocument.setApplicationOrOutcomeId(applicationOrOutcomeId);
    exampleDocument.setCaseReferenceNumber(caseReferenceNumber);
    exampleDocument.setProviderId(providerId);
    exampleDocument.setDocumentType(documentType);
    exampleDocument.setCcmsModule(ccmsModule);
    return Example.of(exampleDocument);
  }

  private Specification<EvidenceDocument> buildQuerySpecification(
      final Example<EvidenceDocument> evidenceDocument,
      final Boolean transferPending) {
    return (root, query, builder) -> {
      final List<Predicate> predicates = new ArrayList<>();

      if (transferPending != null) {
        Path<Object> transferStatus = root.get("transferStatus");
        predicates.add(transferPending
            ? builder.isNull(transferStatus) : builder.isNotNull(transferStatus));
      }

      predicates.add(
          QueryByExamplePredicateBuilder.getPredicate(root, builder, evidenceDocument));
      return builder.and(predicates.toArray(new Predicate[0]));
    };
  }
}