package uk.gov.laa.ccms.caab.api.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.api.entity.Address;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;
import uk.gov.laa.ccms.caab.api.entity.CostStructure;
import uk.gov.laa.ccms.caab.api.exception.CaabApiException;
import uk.gov.laa.ccms.caab.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.api.repository.ApplicationRepository;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationType;

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
   * @param caabUserLoginId the CAAB user login ID responsible for the application creation.
   * @param applicationDetail the details of the application to be created.
   * @return the unique ID of the newly created application.
   */
  public Long createApplication(
          final String caabUserLoginId,
          final ApplicationDetail applicationDetail) {

    AuditTrail auditTrail = new AuditTrail();
    auditTrail.setCreatedBy(caabUserLoginId);
    auditTrail.setModifiedBy(caabUserLoginId);

    Application application = applicationMapper.toApplication(applicationDetail);
    application.setAuditTrail(auditTrail);

    //set correspondence address
    if (application.getCorrespondenceAddress() == null) {
      Address address = new Address(auditTrail);
      application.setCorrespondenceAddress(address);
    } else {
      application.getCorrespondenceAddress().setAuditTrail(auditTrail);
    }

    //Set Cost Structure
    if (application.getCosts() == null) {
      CostStructure costs = new CostStructure(auditTrail);
      application.setCosts(costs);
    } else {
      application.getCosts().setAuditTrail(auditTrail);
    }

    applicationRepository.save(application);

    return application.getId();
  }

  /**
   * Gets an application.
   *
   * @param id the TDS id for the application.
   * @return the application details.
   */
  @Transactional
  public ApplicationDetail getApplication(final Long id) {
    return applicationRepository.findById(id)
        .map(applicationMapper::toApplicationDetail)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id), HttpStatus.NOT_FOUND));
  }

  /**
   * Gets an applications application type.
   *
   * @param id the TDS id for the application.
   * @return the applications application type.
   */
  public ApplicationType getApplicationType(final Long id) {

    return applicationRepository.findById(id)
        .map(applicationMapper::toApplicationType)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id), HttpStatus.NOT_FOUND));
  }

  /**
   * Patch an application with new application type.
   *
   * @param id the TDS id for the application.
   * @param caabUserLoginId the CAAB user login ID responsible for the application creation.
   * @param applicationType the new application type to update the application with
   */
  public void patchApplicationType(
      final Long id,
      final String caabUserLoginId,
      final ApplicationType applicationType) {

    Application application = applicationRepository.findById(id)
        .orElseThrow(() -> new CaabApiException(
            String.format("Application with id %s not found", id), HttpStatus.NOT_FOUND));

    applicationMapper.addApplicationType(application, applicationType, caabUserLoginId);
    applicationRepository.save(application);
  }
}
