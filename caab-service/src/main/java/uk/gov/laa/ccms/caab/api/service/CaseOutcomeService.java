package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.entity.CaseOutcome;
import uk.gov.laa.ccms.caab.api.entity.Opponent;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.mapper.CaseOutcomeMapper;
import uk.gov.laa.ccms.caab.api.repository.CaseOutcomeRepository;
import uk.gov.laa.ccms.caab.api.repository.OpponentRepository;
import uk.gov.laa.ccms.caab.model.BaseAwardDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetails;
import uk.gov.laa.ccms.caab.model.LiablePartyDetail;

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

  private final CaseOutcomeRepository caseOutcomeRepository;

  private final OpponentRepository opponentRepository;

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
        caseOutcomeRepository.findAll(Example.of(caseOutcome)));

  }

  /**
   * Get a single CaseOutcomeDetail by id.
   *
   * @param id - the case outcome id.
   * @return EvidenceDocumentDetail with the matching id, or else an error.
   */
  @Transactional
  public CaseOutcomeDetail getCaseOutcome(final Long id) {

    return caseOutcomeRepository.findById(id)
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
  @Transactional
  public Long createCaseOutcome(final CaseOutcomeDetail caseOutcomeDetail) {
    // Build a Set of all opponentIds across the CaseOutcome and the liable parties for each
    // award type.
    List<Integer> caseOutcomeOpponentIds =
        Optional.ofNullable(caseOutcomeDetail.getOpponentIds())
            .orElse(new ArrayList<>());

    final Set<Integer> allOpponentIds = new HashSet<>(caseOutcomeOpponentIds);
    allOpponentIds.addAll(getLiablePartyOpponentIds(caseOutcomeDetail.getCostAwards()));
    allOpponentIds.addAll(getLiablePartyOpponentIds(caseOutcomeDetail.getFinancialAwards()));
    allOpponentIds.addAll(getLiablePartyOpponentIds(caseOutcomeDetail.getLandAwards()));
    allOpponentIds.addAll(getLiablePartyOpponentIds(caseOutcomeDetail.getOtherAssetAwards()));

    // Now retrieve the Opponent entity for each opponentId.
    List<Opponent> allOpponents = allOpponentIds.stream().map(this::getOpponentEntity).toList();

    final CaseOutcome caseOutcome = mapper.toCaseOutcome(caseOutcomeDetail, allOpponents);

    CaseOutcome createdCaseOutcome = caseOutcomeRepository.save(caseOutcome);

    return createdCaseOutcome.getId();
  }

  private List<Integer> getLiablePartyOpponentIds(List<? extends BaseAwardDetail> awardDetails) {
    return Optional.ofNullable(awardDetails)
        .map(costAwardDetails -> costAwardDetails.stream()
            .flatMap(awards -> Optional.ofNullable(awards.getLiableParties())
                .map(liableParties -> liableParties.stream()
                    .map(LiablePartyDetail::getOpponentId))
                .orElse(Stream.empty())
            )
            .toList())
        .orElse(Collections.emptyList());
  }

  private Opponent getOpponentEntity(Integer id) {
    return opponentRepository.findById(id.longValue())
        .orElseThrow(() -> new CaabApiException(
            String.format("Failed to find Opponent with id: %s", id),
            HttpStatus.NOT_FOUND));
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
    if (caseOutcomeRepository.existsById(caseOutcomeId)) {
      caseOutcomeRepository.deleteById(caseOutcomeId);
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

    caseOutcomeRepository.deleteAll(caseOutcomeRepository.findAll(Example.of(caseOutcome)));
  }

}
