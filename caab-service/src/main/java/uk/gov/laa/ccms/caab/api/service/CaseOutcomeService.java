package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.mapper.CaseOutcomeMapper;
import uk.gov.laa.ccms.caab.api.repository.CaseOutcomeRepository;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetails;

/**
 * Service responsible for handling case outcome operations.
 *
 * @see CaseOutcomeRepository
 * @see ApplicationMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CaseOutcomeService {

  private final CaseOutcomeRepository repository;

  private final CaseOutcomeMapper mapper;

  /**
   * Retrieve case outcomes based on the supplied search criteria.
   *
   * @param caseReferenceNumber - the related case reference number
   * @param providerId - the related provider id.
   *
   * @return Wrapper containing a List of CaseOutcomeDetail.
   */
  @Transactional
  public CaseOutcomeDetails getCaseOutcomes(
      final String caseReferenceNumber,
      final String providerId) {

    CaseOutcome caseOutcome = new CaseOutcome();
    caseOutcome.setLscCaseReference(caseReferenceNumber);
    caseOutcome.setProviderId(providerId);

    return mapper.toCaseOutcomeDetails(
        repository.findAll(Example.of(caseOutcome)));

  }

  /**
   * Get a single CaseOutcomeDetail by id.
   *
   * @param id - the case outcome id.
   * @return EvidenceDocumentDetail with the matching id, or else an error.
   */
  @Transactional
  public CaseOutcomeDetail getCaseOutcome(final Long id) {

    return repository.findById(id)
        .map(mapper::toCaseOutcomeDetail)
        .orElseThrow(() -> new CaabApiException(
            String.format("Failed to find case outcome with id: %s", id),
            HttpStatus.NOT_FOUND));
  }

  /**
   * Create a new CaseOutcome entry.
   *
   * @param caseOutcomeDetail - the case outcome to create.
   * @return the id of the newly created evidence document.
   */
  public Long createCaseOutcome(final CaseOutcomeDetail caseOutcomeDetail) {
    CaseOutcome caseOutcome = mapper.toCaseOutcome(caseOutcomeDetail);

    // Add the parent/child relationships
    Optional.ofNullable(caseOutcome.getOpponents()).ifPresent(
        opponents -> opponents.forEach(opponent -> opponent.setCaseOutcome(caseOutcome)));

    Optional.ofNullable(caseOutcome.getCostAwards()).ifPresent(
        awards -> awards.forEach(award -> {
          award.setCaseOutcome(caseOutcome);

          Optional.ofNullable(award.getLiableParties())
              .ifPresent(liableParties -> liableParties.forEach(
                  liableParty -> liableParty.setCostAward(award)));
        }));

    Optional.ofNullable(caseOutcome.getFinancialAwards()).ifPresent(
        awards -> awards.forEach(award -> {
          award.setCaseOutcome(caseOutcome);

          Optional.ofNullable(award.getLiableParties()).ifPresent(
              liableParties -> liableParties.forEach(liableParty ->
                  liableParty.setFinancialAward(award)));
        }));

    Optional.ofNullable(caseOutcome.getLandAwards()).ifPresent(
        awards -> awards.forEach(award -> {
          award.setCaseOutcome(caseOutcome);

          Optional.ofNullable(award.getLiableParties()).ifPresent(
              liableParties -> liableParties.forEach(liableParty ->
                  liableParty.setLandAward(award)));
        }));

    Optional.ofNullable(caseOutcome.getOtherAssetAwards()).ifPresent(
        awards -> awards.forEach(award -> {
          award.setCaseOutcome(caseOutcome);

          Optional.ofNullable(award.getLiableParties()).ifPresent(
              liableParties -> liableParties.forEach(liableParty ->
                  liableParty.setOtherAssetAward(award)));
        }));

    caseOutcome.getProceedingOutcomes().forEach(
        proceedingOutcome -> proceedingOutcome.setCaseOutcome(caseOutcome));

    uk.gov.laa.ccms.caab.api.entity.CaseOutcome createdCaseOutcome =
        repository.save(caseOutcome);

    return createdCaseOutcome.getId();
  }

  /**
   * Removes a case outcome entry. If the case outccome is not found, a CaabApiException is
   * thrown.
   *
   * @param caseOutcomeId The unique identifier of the case outcome to be removed.
   * @throws CaabApiException If the case outcome with the specified ID is not found.
   */
  @Transactional
  public void removeCaseOutcome(final Long caseOutcomeId) {
    if (repository.existsById(caseOutcomeId)) {
      repository.deleteById(caseOutcomeId);
    } else {
      throw new CaabApiException(
          String.format("CaseOutcome with id: %s not found", caseOutcomeId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Remove all case outcomes which match the provided search criteria.
   *
   * @param caseReferenceNumber    - the case reference number.
   * @param providerId             - the provider id.
   */
  @Transactional
  public void removeCaseOutcomes(
      final String caseReferenceNumber,
      final String providerId) {
    CaseOutcome caseOutcome = new CaseOutcome();
    caseOutcome.setLscCaseReference(caseReferenceNumber);
    caseOutcome.setProviderId(providerId);

    repository.deleteAll(
        repository.findAll(Example.of(caseOutcome)));
  }

}
