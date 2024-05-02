package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.OpponentRepository;
import uk.gov.laa.ccms.caab.api.repository.PriorAuthorityRepository;
import uk.gov.laa.ccms.caab.model.OpponentDetail;

/**
 * Service responsible for handling prior-authorities operations.
 *
 * @see PriorAuthorityRepository
 * @see ApplicationMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OpponentService {

  private final OpponentRepository repository;
  private final ApplicationMapper mapper;

  /**
   * Removes an opponent.
   * If the opponent is not found, a CaabApiException is thrown.
   *
   * @param opponentId The unique identifier of the opponent to be removed.
   * @throws CaabApiException If the opponent with the specified ID is not found.
   */
  @Transactional
  public void removeOpponent(final Long opponentId) {
    if (repository.existsById(opponentId)) {
      repository.deleteById(opponentId);
    } else {
      throw new CaabApiException(
          String.format("Opponent with id: %s not found", opponentId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Updates an opponent.
   * If the opponent is not found, a CaabApiException is thrown.
   *
   * @param opponentId The unique identifier of the opponent to be updated.
   * @param opponentModel The Opponent object containing the details of the opponent to be updated.
   * @throws CaabApiException If the opponent with the specified ID is not found.
   */
  @Transactional
  public void updateOpponent(
      final Long opponentId,
      final OpponentDetail opponentModel) {

    uk.gov.laa.ccms.caab.api.entity.Opponent opponentEntity =
        repository.findById(opponentId)
            .orElseThrow(() -> new CaabApiException(
                String.format("Opponent with id %s not found", opponentId),
                HttpStatus.NOT_FOUND));

    mapper.updateOpponent(opponentEntity, opponentModel);
    repository.save(opponentEntity);
  }
}
