package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.PriorAuthorityRepository;
import uk.gov.laa.ccms.caab.model.PriorAuthority;

/**
 * Service responsible for handling prior-authorities operations.
 *
 * @see uk.gov.laa.ccms.caab.api.repository.PriorAuthorityRepository
 * @see ApplicationMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PriorAuthorityService {

  private final PriorAuthorityRepository repository;
  private final ApplicationMapper mapper;

  /**
   * Removes a prior authority.
   * If the prior authority is not found, a CaabApiException is thrown.
   *
   * @param priorAuthorityId The unique identifier of the prior authority to be removed.
   * @throws uk.gov.laa.ccms.caab.api.exception.CaabApiException If the prior authority with the
   *         specified ID is not found.
   */
  @Transactional
  public void removePriorAuthority(final Long priorAuthorityId) {
    if (repository.existsById(priorAuthorityId)) {
      repository.deleteById(priorAuthorityId);
    } else {
      throw new CaabApiException(
          String.format("Prior Authority with id: %s not found", priorAuthorityId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Updates a prior authority.
   * If the prior authority is not found, a CaabApiException is thrown.
   *
   * @param priorAuthorityId The unique identifier of the prior authority to be updated.
   * @param priorAuthorityModel The PriorAuthority object containing the details of the prior
   *                            authority to be updated.
   * @throws uk.gov.laa.ccms.caab.api.exception.CaabApiException If the prior authority with the
   *         specified ID is not found.
   */
  @Transactional
  public void updatePriorAuthority(
      final Long priorAuthorityId,
      final PriorAuthority priorAuthorityModel) {

    uk.gov.laa.ccms.caab.api.entity.PriorAuthority priorAuthorityEntity =
        repository.findById(priorAuthorityId)
            .orElseThrow(() -> new CaabApiException(
                String.format("Prior Authority with id %s not found", priorAuthorityId),
                HttpStatus.NOT_FOUND));

    mapper.updatePriorAuthority(priorAuthorityEntity, priorAuthorityModel);
    for (uk.gov.laa.ccms.caab.api.entity.ReferenceDataItem referenceDataItemEntity :
        priorAuthorityEntity.getItems()) {
      referenceDataItemEntity.setPriorAuthority(priorAuthorityEntity);
    }
    repository.save(priorAuthorityEntity);
  }
}
