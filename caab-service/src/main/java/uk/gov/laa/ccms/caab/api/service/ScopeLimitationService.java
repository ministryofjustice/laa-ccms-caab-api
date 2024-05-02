package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.ScopeLimitationRepository;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;

/**
 * Service responsible for handling scope-limitations operations.
 *
 * @see uk.gov.laa.ccms.caab.api.repository.ScopeLimitationRepository
 * @see ApplicationMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ScopeLimitationService {

  private final ScopeLimitationRepository repository;
  private final ApplicationMapper mapper;

  /**
   * Removes a scope limitation.
   * If the scope limitation is not found, a CaabApiException is thrown.
   *
   * @param scopeLimitationId The unique identifier of the scope limitation to be removed.
   * @throws uk.gov.laa.ccms.caab.api.exception.CaabApiException If the scope limitation with the
   *         specified ID is not found.
   */
  @Transactional
  public void removeScopeLimitation(final Long scopeLimitationId) {
    if (repository.existsById(scopeLimitationId)) {
      repository.deleteById(scopeLimitationId);
    } else {
      throw new CaabApiException(
          String.format("Scope Limitation with id: %s not found", scopeLimitationId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Updates a scope limitation.
   * If the scope limitation is not found, a CaabApiException is thrown.
   *
   * @param scopeLimitationId The unique identifier of the scope limitation to be updated.
   * @param scopeLimitationModel The ScopeLimitation object containing the details of the scope
   *                             limitation to be updated.
   * @throws uk.gov.laa.ccms.caab.api.exception.CaabApiException If the scope limitation with the
   *         specified ID is not found.
   */
  @Transactional
  public void updateScopeLimitation(
      final Long scopeLimitationId,
      final ScopeLimitationDetail scopeLimitationModel) {

    uk.gov.laa.ccms.caab.api.entity.ScopeLimitation scopeLimitationEntity =
        repository.findById(scopeLimitationId)
            .orElseThrow(() -> new CaabApiException(
                String.format("Scope Limitation with id %s not found", scopeLimitationId),
                HttpStatus.NOT_FOUND));

    mapper.updateScopeLimitation(scopeLimitationEntity, scopeLimitationModel);
    repository.save(scopeLimitationEntity);
  }
}
