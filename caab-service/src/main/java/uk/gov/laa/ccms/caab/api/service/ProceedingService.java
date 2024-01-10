package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.ProceedingRepository;
import uk.gov.laa.ccms.caab.model.Proceeding;
import uk.gov.laa.ccms.caab.model.ScopeLimitation;

/**
 * Service responsible for handling proceedings operations.
 *
 * @see uk.gov.laa.ccms.caab.api.repository.ProceedingRepository
 * @see ApplicationMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProceedingService {

  private final ProceedingRepository repository;
  private final ApplicationMapper mapper;

  /**
   * Removes a proceeding.
   * If the proceeding is not found, a CaabApiException is thrown.
   *
   * @param proceedingId The unique identifier of the proceeding to be removed.
   * @throws uk.gov.laa.ccms.caab.api.exception.CaabApiException If the application or the
   *        proceeding with the specified IDs are not found.
   */
  @Transactional
  public void removeProceeding(final Long proceedingId) {
    if (repository.existsById(proceedingId)) {
      repository.deleteById(proceedingId);
    } else {
      throw new CaabApiException(
          String.format("Proceeding with id: %s not found", proceedingId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Updates a proceeding of an application.
   * If either the application or the proceeding is not found, a CaabApiException is thrown.
   *
   * @param proceedingId The unique identifier of the proceeding to be updated.
   * @param proceedingModel The Proceeding object containing the details of the proceeding to be
   *                        updated.
   * @throws uk.gov.laa.ccms.caab.api.exception.CaabApiException If the application or the
   *         proceeding with the specified IDs are not found.
   */
  @Transactional
  public void updateProceeding(
      final Long proceedingId,
      final Proceeding proceedingModel) {

    uk.gov.laa.ccms.caab.api.entity.Proceeding proceedingEntity =
        repository.findById(proceedingId)
            .orElseThrow(() -> new CaabApiException(
                String.format("Proceeding with id %s not found", proceedingId),
                HttpStatus.NOT_FOUND));

    mapper.updateProceeding(proceedingEntity, proceedingModel);
    for (uk.gov.laa.ccms.caab.api.entity.ScopeLimitation scopeLimitationEntity :
        proceedingEntity.getScopeLimitations()) {
      scopeLimitationEntity.setProceeding(proceedingEntity);
    }
    repository.save(proceedingEntity);
  }

  /**
   * Gets a proceeding's scope limitations.
   *
   * @param id the TDS id for the proceeding.
   * @return the proceeding's scope limitations.
   */
  @Transactional
  public List<ScopeLimitation> getScopeLimitationsForProceeding(final Long id) {
    return repository.findById(id)
        .map(uk.gov.laa.ccms.caab.api.entity.Proceeding::getScopeLimitations)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id),
            HttpStatus.NOT_FOUND))
        .stream()
        .map(mapper::toScopeLimitationModel)
        .collect(Collectors.toList());
  }

  /**
   * Creates and associates a new scope limitation with a specified proceeding.
   * If the proceeding is not found, a CaabApiException is thrown.
   *
   * @param proceedingId The unique identifier of the proceeding.
   * @param scopeLimitation The Scope Limitation object.
   * @throws CaabApiException If the proceeding with the specified ID is not found.
   */
  @Transactional
  public void createScopeLimitationForProceeding(
      final Long proceedingId,
      final ScopeLimitation scopeLimitation) {

    uk.gov.laa.ccms.caab.api.entity.Proceeding proceeding = repository.findById(proceedingId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Proceeding with id %s not found", proceedingId),
            HttpStatus.NOT_FOUND));

    uk.gov.laa.ccms.caab.api.entity.ScopeLimitation scopeLimitationEntity =
        mapper.toScopeLimitation(scopeLimitation);
    scopeLimitationEntity.setProceeding(proceeding);
    proceeding.getScopeLimitations().add(scopeLimitationEntity);

    repository.save(proceeding);
  }
}
