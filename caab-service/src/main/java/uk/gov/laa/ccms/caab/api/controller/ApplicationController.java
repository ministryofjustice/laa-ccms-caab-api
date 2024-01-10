package uk.gov.laa.ccms.caab.api.controller;


import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.laa.ccms.caab.api.ApplicationsApi;
import uk.gov.laa.ccms.caab.api.ClientsApi;
import uk.gov.laa.ccms.caab.api.LinkedCasesApi;
import uk.gov.laa.ccms.caab.api.PriorAuthoritiesApi;
import uk.gov.laa.ccms.caab.api.ProceedingsApi;
import uk.gov.laa.ccms.caab.api.ScopeLimitationsApi;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.api.service.LinkedCaseService;
import uk.gov.laa.ccms.caab.api.service.PriorAuthorityService;
import uk.gov.laa.ccms.caab.api.service.ProceedingService;
import uk.gov.laa.ccms.caab.api.service.ScopeLimitationService;
import uk.gov.laa.ccms.caab.model.Address;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.BaseClient;
import uk.gov.laa.ccms.caab.model.CostStructure;
import uk.gov.laa.ccms.caab.model.LinkedCase;
import uk.gov.laa.ccms.caab.model.PriorAuthority;
import uk.gov.laa.ccms.caab.model.Proceeding;
import uk.gov.laa.ccms.caab.model.ScopeLimitation;

/**
 * Represents the main controller handling application-related requests.
 * Implements the {@code ApplicationsApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve application data.
 */
@RestController
@RequiredArgsConstructor
public class ApplicationController implements ApplicationsApi, LinkedCasesApi, ProceedingsApi,
    ScopeLimitationsApi, PriorAuthoritiesApi, ClientsApi {

  private final ApplicationService applicationService;
  private final LinkedCaseService linkedCaseService;
  private final ProceedingService proceedingService;
  private final PriorAuthorityService priorAuthorityService;
  private final ScopeLimitationService scopeLimitationService;

  /**
   * Creates a new application and returns the URI of the created resource.
   *
   * @param caabUserLoginId the user login ID used for audit trail
   * @param applicationDetail the details of the application to be created
   * @return a ResponseEntity with the location header set to the URI of the created application
   */
  @Override
  public ResponseEntity<Void> createApplication(
      final String caabUserLoginId,
      final ApplicationDetail applicationDetail) {

    Long applicationId = applicationService.createApplication(applicationDetail);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(applicationId)
            .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(uri);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }


  /**
   * Retrieves the details of a specific application.
   *
   * @param caseReferenceNumber Unique reference number for the case.
   * @param providerCaseRef Reference provided by the case provider.
   * @param clientSurname Surname of the client associated with the case.
   * @param clientReference Client's personal reference for the case.
   * @param feeEarner The individual responsible for handling the case fees.
   * @param officeId Identifier for the office managing the case.
   * @param status Current status of the application.
   * @param pageable Pagination information for the response.
   * @return ResponseEntity containing the application details.
   */
  @Override
  public ResponseEntity<ApplicationDetails> getApplications(
      final String caseReferenceNumber,
      final String providerCaseRef,
      final String clientSurname,
      final String clientReference,
      final String feeEarner,
      final Integer officeId,
      final String status,
      final Pageable pageable) {

    ApplicationDetails applicationDetails = applicationService.getApplications(
        caseReferenceNumber,
        providerCaseRef,
        clientSurname,
        clientReference,
        feeEarner,
        officeId,
        status,
        pageable);

    return new ResponseEntity<>(applicationDetails, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApplicationDetail> getApplication(
      final Long applicationId) {
    ApplicationDetail application = applicationService.getApplication(applicationId);
    return new ResponseEntity<>(application, HttpStatus.OK);
  }

  //correspondence address

  /**
   * Retrieves the correspondence address for a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @return a ResponseEntity containing the correspondence address of the application
   */
  @Override
  public ResponseEntity<Address> getApplicationCorrespondenceAddress(
      final Long applicationId) {
    Address correspondenceAddress =
        applicationService.getApplicationCorrespondenceAddress(applicationId);
    return new ResponseEntity<>(correspondenceAddress, HttpStatus.OK);
  }

  /**
   * Updates the correspondence address of a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @param caabUserLoginId the user login ID used for audit trail
   * @param address the new correspondence address
   * @return a ResponseEntity with no content
   */
  @Override
  public ResponseEntity<Void> putApplicationCorrespondenceAddress(
      final Long applicationId,
      final String caabUserLoginId,
      final Address address) {
    applicationService.putCorrespondenceAddress(applicationId, address);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //provider details

  /**
   * Retrieves the provider details for a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @return a ResponseEntity containing the provider details of the application
   */
  @Override
  public ResponseEntity<ApplicationProviderDetails> getApplicationProviderDetails(
      final Long applicationId) {
    ApplicationProviderDetails applicationProviderDetails =
        applicationService.getApplicationProviderDetails(applicationId);
    return new ResponseEntity<>(applicationProviderDetails, HttpStatus.OK);
  }

  /**
   * Updates the provider details of a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @param caabUserLoginId the user login ID used for audit trail
   * @param applicationProviderDetails the new provider details
   * @return a ResponseEntity with no content
   */
  @Override
  public ResponseEntity<Void> putApplicationProviderDetails(
      final Long applicationId,
      final String caabUserLoginId,
      final ApplicationProviderDetails applicationProviderDetails) {
    applicationService.putProviderDetails(applicationId, applicationProviderDetails);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //application type

  /**
   * Retrieves the application type for a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @return a ResponseEntity containing the type of the application
   */
  @Override
  public ResponseEntity<ApplicationType> getApplicationType(
      final Long applicationId) {
    ApplicationType applicationType = applicationService.getApplicationType(applicationId);
    return new ResponseEntity<>(applicationType, HttpStatus.OK);
  }

  /**
   * Updates the application type for a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @param caabUserLoginId the user login ID used for audit trail
   * @param applicationType the new type of the application
   * @return a ResponseEntity with no content
   */
  @Override
  public ResponseEntity<Void> putApplicationType(
      final Long applicationId,
      final String caabUserLoginId,
      final ApplicationType applicationType) {
    applicationService.putApplicationType(applicationId, applicationType);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //Cost Structure

  @Override
  public ResponseEntity<CostStructure> getApplicationCostStructure(final Long applicationId) {
    CostStructure costStructure =
        applicationService.getApplicationCostStructure(applicationId);
    return new ResponseEntity<>(costStructure, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> updateApplicationCostStructure(
      final Long applicationId,
      final String caabUserLoginId,
      final CostStructure costStructure) {
    applicationService.putCostStructure(applicationId, costStructure);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //Linked Cases

  /**
   * Retrieves all linked cases for a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @return a ResponseEntity containing a list of linked cases
   */
  @Override
  public ResponseEntity<List<LinkedCase>> getApplicationLinkedCases(final Long applicationId) {
    List<LinkedCase> linkedCases = applicationService.getLinkedCasesForApplication(applicationId);
    return new ResponseEntity<>(linkedCases, HttpStatus.OK);
  }

  /**
   * Adds a linked case to a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @param caabUserLoginId the user login ID used for audit trail
   * @param linkedCase the linked case to be added
   * @return a ResponseEntity indicating the case was successfully created
   */
  @Override
  public ResponseEntity<Void> addApplicationLinkedCase(
      final Long applicationId,
      final String caabUserLoginId,
      final LinkedCase linkedCase) {
    applicationService.createLinkedCaseForApplication(applicationId, linkedCase);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  /**
   * Updates a linked case.
   *
   * @param linkedCaseId the unique identifier of the linked case
   * @param caabUserLoginId the user login ID used for audit trail
   * @param linkedCase the updated linked case
   * @return a ResponseEntity with no content
   */
  @Override
  public ResponseEntity<Void> updateApplicationLinkedCase(
      final Long linkedCaseId,
      final String caabUserLoginId,
      final LinkedCase linkedCase) {
    linkedCaseService.updateLinkedCase(linkedCaseId, linkedCase);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  /**
   * Removes a linked case from a specific application.
   *
   * @param linkedCaseId the unique identifier of the linked case
   * @param caabUserLoginId the user login ID used for audit trail
   * @return a ResponseEntity indicating the case was successfully removed
   */
  @Override
  public ResponseEntity<Void> removeApplicationLinkedCase(
      final Long linkedCaseId,
      final String caabUserLoginId) {
    linkedCaseService.removeLinkedCase(linkedCaseId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //proceedings

  @Override
  public ResponseEntity<Void> addApplicationProceeding(
      final Long applicationId,
      final String caabUserLoginId,
      final Proceeding proceeding) {
    applicationService.createProceedingForApplication(applicationId, proceeding);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<List<Proceeding>> getApplicationProceedings(
      final Long applicationId) {
    List<Proceeding> proceedings = applicationService.getProceedingsForApplication(applicationId);
    return new ResponseEntity<>(proceedings, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> removeProceeding(
      final Long proceedingId,
      final String caabUserLoginId) {
    proceedingService.removeProceeding(proceedingId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> updateProceeding(
      final Long proceedingId,
      final String caabUserLoginId,
      final Proceeding proceeding) {
    proceedingService.updateProceeding(proceedingId, proceeding);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //scope limitations

  @Override
  public ResponseEntity<Void> addProceedingScopeLimitation(
      final Long proceedingId,
      final String caabUserLoginId,
      final ScopeLimitation scopeLimitation) {
    proceedingService.createScopeLimitationForProceeding(proceedingId, scopeLimitation);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<List<ScopeLimitation>> getApplicationScopeLimitations(
      final Long proceedingId) {
    List<ScopeLimitation> scopeLimitations =
        proceedingService.getScopeLimitationsForProceeding(proceedingId);
    return new ResponseEntity<>(scopeLimitations, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> removeScopeLimitation(
      final Long scopeLimitationId,
      final String caabUserLoginId) {
    scopeLimitationService.removeScopeLimitation(scopeLimitationId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> updateScopeLimitation(
      final Long scopeLimitationId,
      final String caabUserLoginId,
      final ScopeLimitation scopeLimitation) {
    scopeLimitationService.updateScopeLimitation(scopeLimitationId, scopeLimitation);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //prior authorities

  @Override
  public ResponseEntity<Void> addApplicationPriorAuthority(
      final Long id,
      final String caabUserLoginId,
      final PriorAuthority priorAuthority) {
    applicationService.createPriorAuthorityForApplication(id, priorAuthority);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<List<PriorAuthority>> getApplicationPriorAuthorities(
      final Long id) {
    List<PriorAuthority> priorAuthorities =
        applicationService.getPriorAuthoritiesForApplication(id);
    return new ResponseEntity<>(priorAuthorities, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> removePriorAuthority(
      final Long priorAuthorityId,
      final String caabUserLoginId) {
    priorAuthorityService.removePriorAuthority(priorAuthorityId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> updatePriorAuthority(
      final Long priorAuthorityId,
      final String caabUserLoginId,
      final PriorAuthority priorAuthority) {
    priorAuthorityService.updatePriorAuthority(priorAuthorityId, priorAuthority);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //clients
  @Override
  public ResponseEntity<Void> updateApplicationClient(final String clientReferenceId,
                                                      final String caabUserLoginId,
                                                      final BaseClient baseClient) {
    applicationService.updateClient(baseClient, clientReferenceId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
