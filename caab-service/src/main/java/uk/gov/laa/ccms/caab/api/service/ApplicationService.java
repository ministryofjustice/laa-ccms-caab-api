package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.ApplicationRepository;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.LinkedCase;

/**
 * Service responsible for handling application-related operations.
 *
 * <p>The {@code ApplicationService} provides methods for creating and managing
 * applications within the CCMS system. It interacts with the underlying
 * data repositories to persist application data and provides additional
 * functionalities like auditing, and address and cost structure management.</p>
 *
 * @see uk.gov.laa.ccms.caab.api.repository.ApplicationRepository
 * @see ApplicationMapper
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

  private final ApplicationRepository applicationRepository;

  private final ApplicationMapper applicationMapper;

  /**
   * Creates a new application entry in the system.
   *
   * <p>This method takes in a user login ID and application details, sets the audit trail,
   * initializes or updates the correspondence address and cost structure, and persists
   * the application in the repository.</p>
   *
   * @param applicationDetail the details of the application to be created.
   * @return the unique ID of the newly created application.
   */
  public Long createApplication(
          final ApplicationDetail applicationDetail) {

    Application application = applicationMapper.toApplication(applicationDetail);
    applicationMapper.setParentInChildEntities(application);

    applicationRepository.save(application);

    return application.getId();
  }

  /**
   * Gets an application.
   *
   * @param applicationId the TDS id for the application.
   * @return the application details.
   */
  @Transactional
  public ApplicationDetail getApplication(final Long applicationId) {
    return applicationRepository.findById(applicationId)
        .map(applicationMapper::toApplicationDetail)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));
  }

  /**
   * Gets an application's linked cases.
   *
   * @param id the TDS id for the application.
   * @return the application's linked cases.
   */
  @Transactional
  public List<LinkedCase> getLinkedCasesForApplication(final Long id) {
    return applicationRepository.findById(id)
        .map(Application::getLinkedCases)
        .map(applicationMapper::toLinkedCaseModelList)
        .orElseThrow(() -> new CaabApiException(
            String.format("Linked cases for application with id %s not found", id),
            HttpStatus.NOT_FOUND));
  }

  /**
   * Creates and associates a new linked case with a specified application.
   * If the application is not found, a CaabApiException is thrown.
   *
   * @param id The unique identifier of the application.
   * @param linkedCase The LinkedCase object containing the details of the case to be linked.
   * @throws CaabApiException If the application with the specified ID is not found.
   */
  @Transactional
  public void createLinkedCaseForApplication(final Long id, final LinkedCase linkedCase) {
    Application application = applicationRepository.findById(id)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id), HttpStatus.NOT_FOUND));

    uk.gov.laa.ccms.caab.api.entity.LinkedCase linkedCaseEntity =
        applicationMapper.toLinkedCase(linkedCase);
    application.getLinkedCases().add(linkedCaseEntity);
    linkedCaseEntity.setApplication(application);
    applicationRepository.save(application);
  }

  /**
   * Removes a linked case from the specified application.
   * If either the application or the linked case is not found, a CaabApiException is thrown.
   *
   * @param id The unique identifier of the application.
   * @param linkedCaseId The unique identifier of the linked case to be removed.
   * @throws CaabApiException If the application or the linked case with the specified IDs are
   *        not found.
   */
  @Transactional
  public void removeLinkedCaseFromApplication(final Long id, final Long linkedCaseId) {
    Application application = applicationRepository.findById(id)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id), HttpStatus.NOT_FOUND));

    uk.gov.laa.ccms.caab.api.entity.LinkedCase linkedCaseEntity =
        application.getLinkedCases().stream()
        .filter(linkedCase -> linkedCase.getId().equals(linkedCaseId))
        .findFirst()
        .orElseThrow(() -> new CaabApiException(
            String.format("Linked case with id %s not found", linkedCaseId), HttpStatus.NOT_FOUND));

    application.getLinkedCases().remove(linkedCaseEntity);
    applicationRepository.save(application);
  }

  /**
   * Updates a linked case of a specified application.
   * If either the application or the linked case is not found, a CaabApiException is thrown.
   *
   * @param id The unique identifier of the application.
   * @param linkedCaseId The unique identifier of the linked case to be updated.
   * @param linkedCaseModel The LinkedCase object containing the details of the case to be updated.
   * @throws CaabApiException If the application or the linked case with the specified IDs are not
   *        found.
   */
  @Transactional
  public void updateLinkedCaseForApplication(
      final Long id, final Long linkedCaseId,
      final LinkedCase linkedCaseModel) {
    Application application = applicationRepository.findById(id)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id), HttpStatus.NOT_FOUND));

    uk.gov.laa.ccms.caab.api.entity.LinkedCase linkedCaseEntity =
        application.getLinkedCases().stream()
        .filter(linkedCase -> linkedCase.getId().equals(linkedCaseId))
        .findFirst()
        .orElseThrow(() -> new CaabApiException(
            String.format("Linked case with id %s not found", linkedCaseId), HttpStatus.NOT_FOUND));

    applicationMapper.updateLinkedCase(linkedCaseEntity, linkedCaseModel);
    applicationRepository.save(application);
  }



  /**
   * Gets an application's correspondence address.
   *
   * @param applicationId the TDS id for the application.
   * @return the application's correspondence address.
   */
  @Transactional
  public uk.gov.laa.ccms.caab.model.Address getApplicationCorrespondenceAddress(
      final Long applicationId) {
    return applicationRepository.findById(applicationId)
        .map(Application::getCorrespondenceAddress)
        .map(applicationMapper::toAddressModel)
        .orElseThrow(() -> new CaabApiException(
            String.format("Correspondence address for application with id %s not found",
                applicationId),
            HttpStatus.NOT_FOUND));
  }

  /**
   * Gets an applications provider details.
   *
   * @param applicationId the TDS id for the application.
   * @return the applications provider details.
   */
  public ApplicationProviderDetails getApplicationProviderDetails(final Long applicationId) {
    return applicationRepository.findById(applicationId)
        .map(applicationMapper::toProviderDetails)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));
  }

  /**
   * Gets an applications application type.
   *
   * @param applicationId the TDS id for the application.
   * @return the applications application type.
   */
  public ApplicationType getApplicationType(final Long applicationId) {
    return applicationRepository.findById(applicationId)
        .map(applicationMapper::toApplicationType)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));
  }

  /**
   * Adds/Updates an application with a new correspondence address.
   *
   * @param applicationId the TDS id for the application.
   * @param address the applications updated correspondence address
   */
  @Transactional
  public void putCorrespondenceAddress(
      final Long applicationId,
      final uk.gov.laa.ccms.caab.model.Address address) {

    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    applicationMapper.addCorrespondenceAddressToApplication(application, address);
    applicationRepository.save(application);
  }

  /**
   * Amends an application with new provider details.
   *
   * @param applicationId the TDS id for the application.
   * @param providerDetails the new application provider details to update the application with
   */
  public void putProviderDetails(
      final Long applicationId,
      final ApplicationProviderDetails providerDetails) {

    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    applicationMapper.addProviderDetails(application, providerDetails);
    applicationRepository.save(application);
  }

  /**
   * Amends an application with new application type.
   *
   * @param applicationId the TDS id for the application.
   * @param applicationType the new application type to update the application with
   */
  public void putApplicationType(
      final Long applicationId,
      final ApplicationType applicationType) {

    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    applicationMapper.addApplicationType(application, applicationType);
    applicationRepository.save(application);
  }
}
