package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.LinkedCase;
import uk.gov.laa.ccms.caab.api.entity.Opponent;
import uk.gov.laa.ccms.caab.api.entity.PriorAuthority;
import uk.gov.laa.ccms.caab.api.entity.Proceeding;
import uk.gov.laa.ccms.caab.api.entity.ReferenceDataItem;
import uk.gov.laa.ccms.caab.api.entity.ScopeLimitation;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.ApplicationRepository;
import uk.gov.laa.ccms.caab.model.AddressDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.BaseClientDetail;
import uk.gov.laa.ccms.caab.model.CostStructureDetail;
import uk.gov.laa.ccms.caab.model.LinkedCaseDetail;
import uk.gov.laa.ccms.caab.model.OpponentDetail;
import uk.gov.laa.ccms.caab.model.PriorAuthorityDetail;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;

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
   * Get applications which match the provided search criteria.
   *
   * @param caseReferenceNumber - the case reference number search criteria.
   * @param providerId - the provider id search criteria.
   * @param providerCaseRef - the provider case reference search criteria.
   * @param clientSurname - the client surname search criteria.
   * @param clientReference - the client reference search criteria.
   * @param feeEarner - the fee earner search criteria.
   * @param officeId  - the office id search criteria.
   * @param status - the status search criteria.
   * @param pageable - the pageable settings for the query.
   * @return application details containing a page of BaseApplication.
   */
  @Transactional
  public ApplicationDetails getApplications(
      final String caseReferenceNumber,
      final String providerId,
      final String providerCaseRef,
      final String clientSurname,
      final String clientReference,
      final String feeEarner,
      final Integer officeId,
      final String status,
      final Pageable pageable) {
    Application application = new Application();
    application.setLscCaseReference(caseReferenceNumber);
    application.setProviderId(providerId);
    application.setProviderCaseReference(providerCaseRef);
    application.setClientSurname(clientSurname);
    application.setClientReference(clientReference);
    application.setFeeEarner(feeEarner);
    application.setOfficeId(officeId);
    application.setActualStatus(status);

    return applicationMapper.toApplicationDetails(
        applicationRepository.findAll(Example.of(application), pageable));
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
   * Updates an application with new details.
   *
   * @param applicationId The unique identifier of the application to be updated.
   * @param applicationDetail The ApplicationDetail object containing the details of the
   *                          application to be updated.
   * @throws CaabApiException If the application with the specified ID is not found.
   */
  @Transactional
  public void updateApplication(
      final Long applicationId,
      final ApplicationDetail applicationDetail) {
    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    applicationMapper.mapIntoApplication(application, applicationDetail);
    applicationRepository.save(application);
  }

  /**
   * Remove an application.
   *
   * @param applicationId the TDS id for the application.
   * @throws uk.gov.laa.ccms.caab.api.exception.CaabApiException If the application
   *        with the specified ID is not found.
   */
  @Transactional
  public void removeApplication(final Long applicationId) {
    if (applicationRepository.existsById(applicationId)) {
      applicationRepository.deleteById(applicationId);
    } else {
      throw new CaabApiException(
          String.format("Application with id: %s not found", applicationId),
          HttpStatus.NOT_FOUND);
    }
  }

  /**
   * Updates a client's information in the application repository.
   *
   * @param baseClientDetail The client object containing updated information.
   * @param reference  A unique reference number for the client.
   */
  @Transactional
  public void updateClient(
      final BaseClientDetail baseClientDetail,
      final String reference) {
    applicationRepository.updateClient(
        baseClientDetail.getFirstName(),
        baseClientDetail.getSurname(),
        reference);
  }

  /**
   * Gets an application's linked cases.
   *
   * @param id the TDS id for the application.
   * @return the application's linked cases.
   */
  @Transactional
  public List<LinkedCaseDetail> getLinkedCasesForApplication(final Long id) {
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
   * @param applicationId The unique identifier of the application.
   * @param linkedCaseDetail The details of the case to be linked.
   * @throws CaabApiException If the application with the specified ID is not found.
   */
  @Transactional
  public void createLinkedCaseForApplication(
      final Long applicationId,
      final LinkedCaseDetail linkedCaseDetail) {
    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    LinkedCase linkedCaseDetailEntity =
        applicationMapper.toLinkedCase(linkedCaseDetail);
    application.getLinkedCases().add(linkedCaseDetailEntity);

    linkedCaseDetailEntity.setApplication(application);
    applicationRepository.save(application);
  }

  /**
   * Gets an application's ProceedingDetails.
   *
   * @param id the TDS id for the application.
   * @return the application's ProceedingDetails.
   */
  @Transactional
  public List<ProceedingDetail> getProceedingsForApplication(final Long id) {
    return applicationRepository.findById(id)
        .map(Application::getProceedings)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id),
            HttpStatus.NOT_FOUND))
        .stream()
        .map(applicationMapper::toProceedingModel)
        .collect(Collectors.toList());
  }

  /**
   * Creates and associates a new ProceedingDetail with a specified application.
   * If the application is not found, a CaabApiException is thrown.
   *
   * @param applicationId The unique identifier of the application.
   * @param proceedingDetail The ProceedingDetail object.
   * @throws CaabApiException If the application with the specified ID is not found.
   */
  @Transactional
  public void createProceedingForApplication(
      final Long applicationId,
      final ProceedingDetail proceedingDetail) {
    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    Proceeding proceedingDetailEntity =
        applicationMapper.toProceeding(proceedingDetail);
    application.getProceedings().add(proceedingDetailEntity);

    proceedingDetailEntity.setApplication(application);
    for (ScopeLimitation scopeLimitation : proceedingDetailEntity.getScopeLimitations()) {
      scopeLimitation.setProceeding(proceedingDetailEntity);
    }

    applicationRepository.save(application);
  }

  /**
   * Gets an application's prior authorities.
   *
   * @param id the TDS id for the application.
   * @return the application's prior authorities.
   */
  @Transactional
  public List<PriorAuthorityDetail> getPriorAuthoritiesForApplication(final Long id) {
    return applicationRepository.findById(id)
        .map(Application::getPriorAuthorities)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id),
            HttpStatus.NOT_FOUND))
        .stream()
        .map(applicationMapper::toPriorAuthorityModel)
        .collect(Collectors.toList());
  }

  /**
   * Creates and associates a new prior authority with a specified application.
   * If the application is not found, a CaabApiException is thrown.
   *
   * @param applicationId The unique identifier of the application.
   * @param priorAuthorityDetail The PriorAuthorityDetail object.
   * @throws CaabApiException If the application with the specified ID is not found.
   */
  @Transactional
  public void createPriorAuthorityForApplication(
      final Long applicationId,
      final PriorAuthorityDetail priorAuthorityDetail) {
    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    PriorAuthority priorAuthorityEntity =
        applicationMapper.toPriorAuthority(priorAuthorityDetail);
    application.getPriorAuthorities().add(priorAuthorityEntity);

    priorAuthorityEntity.setApplication(application);
    for (ReferenceDataItem item : priorAuthorityEntity.getItems()) {
      item.setPriorAuthority(priorAuthorityEntity);
    }

    applicationRepository.save(application);
  }


  /**
   * Gets an application's correspondence address.
   *
   * @param applicationId the TDS id for the application.
   * @return the application's correspondence address.
   */
  @Transactional
  public AddressDetail getApplicationCorrespondenceAddress(
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
   * Gets an application's cost structure.
   *
   * @param applicationId the TDS id for the application.
   * @return the application's cost structure.
   */
  @Transactional
  public CostStructureDetail getApplicationCostStructure(
      final Long applicationId) {
    return applicationRepository.findById(applicationId)
        .map(Application::getCosts)
        .map(applicationMapper::toCostStructureModel)
        .orElseThrow(() -> new CaabApiException(
            String.format("Cost structure for application with id %s not found",
                applicationId),
            HttpStatus.NOT_FOUND));
  }

  /**
   * Gets the total amount of applications.
   *
   * @return the total amount of applications.
   */
  @Transactional
  public Long getTotalApplications() {
    return applicationRepository.count();
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
      final AddressDetail address) {

    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    applicationMapper.addCorrespondenceAddressToApplication(application, address);
    applicationRepository.save(application);
  }

  /**
   * Adds/Updates an application with a new cost structure.
   *
   * @param applicationId the TDS id for the application.
   * @param costStructure the applications updated cost structure
   */
  @Transactional
  public void putCostStructure(
      final Long applicationId,
      final CostStructureDetail costStructure) {

    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    applicationMapper.addCostStructureToApplication(application, costStructure);
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

  /**
   * Gets an application's OpponentDetails.
   *
   * @param id the TDS id for the application.
   * @return the application's OpponentDetails.
   */
  @Transactional
  public List<OpponentDetail> getOpponentsForApplication(final Long id) {
    return applicationRepository.findById(id)
        .map(Application::getOpponents)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id),
            HttpStatus.NOT_FOUND))
        .stream()
        .map(applicationMapper::toOpponentModel)
        .collect(Collectors.toList());
  }

  /**
   * Creates and associates a new OpponentDetail with a specified application.
   * If the application is not found, a CaabApiException is thrown.
   *
   * @param applicationId The unique identifier of the application.
   * @param opponentDetail The OpponentDetail object.
   * @throws CaabApiException If the application with the specified ID is not found.
   */
  @Transactional
  public void createOpponentForApplication(
      final Long applicationId,
      final OpponentDetail opponentDetail) {
    Application application = applicationRepository.findById(applicationId)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", applicationId),
            HttpStatus.NOT_FOUND));

    Opponent opponentEntity =
        applicationMapper.toOpponent(opponentDetail);
    application.getOpponents().add(opponentEntity);

    opponentEntity.setApplication(application);

    applicationRepository.save(application);
  }

}
