package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.LinkedCaseRepository;
import uk.gov.laa.ccms.caab.model.LinkedCaseDetail;

/**
 * Service responsible for handling linked-case operations.
 *
 * @see uk.gov.laa.ccms.caab.api.repository.LinkedCaseRepository
 * @see ApplicationMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class LinkedCaseService {

  private final LinkedCaseRepository repository;

  private final ApplicationMapper mapper;

  /**
   * Removes a linked case.
   * If the linked case is not found, a CaabApiException is thrown.
   *
   * @param linkedCaseId The unique identifier of the linked case to be removed.
   * @throws uk.gov.laa.ccms.caab.api.exception.CaabApiException If the application or the
   *        linked case with the specified IDs are not found.
   */
  @Transactional
  public void removeLinkedCase(final Long linkedCaseId) {
    if (repository.existsById(linkedCaseId)) {
      repository.deleteById(linkedCaseId);
    } else {
      throw new CaabApiException(
          String.format("Linked case with id: %s not found", linkedCaseId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Updates a linked case of an application.
   * If either the application or the linked case is not found, a CaabApiException is thrown.
   *
   * @param linkedCaseId The unique identifier of the linked case to be updated.
   * @param linkedCaseModel The LinkedCase object containing the details of the case to be updated.
   * @throws CaabApiException If the application or the linked case with the specified IDs are not
   *        found.
   */
  @Transactional
  public void updateLinkedCase(
      final Long linkedCaseId,
      final LinkedCaseDetail linkedCaseModel) {

    uk.gov.laa.ccms.caab.api.entity.LinkedCase linkedCaseEntity =
        repository.findById(linkedCaseId)
            .orElseThrow(() -> new CaabApiException(
                String.format("Linked case with id %s not found", linkedCaseId),
                HttpStatus.NOT_FOUND));

    mapper.updateLinkedCase(linkedCaseEntity, linkedCaseModel);
    repository.save(linkedCaseEntity);
  }
}
