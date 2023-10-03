package uk.gov.laa.ccms.api.service;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.api.entity.Address;
import uk.gov.laa.ccms.api.entity.Application;
import uk.gov.laa.ccms.api.entity.AuditTrail;
import uk.gov.laa.ccms.api.entity.CostStructure;
import uk.gov.laa.ccms.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.api.repository.ApplicationRepository;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;

/**
 * Service responsible for handling application-related operations.
 *
 * <p>The {@code ApplicationService} provides methods for creating and managing
 * applications within the CCMS system. It interacts with the underlying
 * data repositories to persist application data and provides additional
 * functionalities like auditing, and address and cost structure management.</p>
 *
 * @see ApplicationRepository
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
  public ApplicationDetail getApplication(Long id) {
    Optional<Application> application =  applicationRepository.findById(id);

    return application.map(applicationMapper::toApplicationDetail)
        .orElse(null);
  }
}
