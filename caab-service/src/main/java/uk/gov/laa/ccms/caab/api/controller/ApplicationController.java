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
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
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
 * Represents the main controller handling application-related requests.
 * Implements the {@code ApplicationsApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve application data.
 */
@RestController
@RequiredArgsConstructor
public class ApplicationController implements ApplicationsApi {

  private final ApplicationService applicationService;

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
   * @param providerId Reference provided by the case provider.
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
      final String providerId,
      final String providerCaseRef,
      final String clientSurname,
      final String clientReference,
      final String feeEarner,
      final Integer officeId,
      final String status,
      final Pageable pageable) {

    ApplicationDetails applicationDetails = applicationService.getApplications(
        caseReferenceNumber,
        providerId,
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

  @Override
  public ResponseEntity<Void> updateApplication(
      final Long id,
      final String caabUserLoginId,
      final ApplicationDetail applicationDetail) {
    applicationService.updateApplication(id, applicationDetail);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> removeApplication(
      final Long id,
      final String caabUserLoginId) {
    applicationService.removeApplication(id);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  //correspondence address

  /**
   * Retrieves the correspondence address for a specific application.
   *
   * @param applicationId the unique identifier of the application
   * @return a ResponseEntity containing the correspondence address of the application
   */
  @Override
  public ResponseEntity<AddressDetail> getApplicationCorrespondenceAddress(
      final Long applicationId) {
    AddressDetail correspondenceAddress =
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
      final AddressDetail address) {
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
  public ResponseEntity<CostStructureDetail> getApplicationCostStructure(final Long applicationId) {
    CostStructureDetail costStructure =
        applicationService.getApplicationCostStructure(applicationId);
    return new ResponseEntity<>(costStructure, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> updateApplicationCostStructure(
      final Long applicationId,
      final String caabUserLoginId,
      final CostStructureDetail costStructure) {
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
  public ResponseEntity<List<LinkedCaseDetail>> getApplicationLinkedCases(
      final Long applicationId) {
    List<LinkedCaseDetail> linkedCases =
        applicationService.getLinkedCasesForApplication(applicationId);
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
      final LinkedCaseDetail linkedCase) {
    applicationService.createLinkedCaseForApplication(applicationId, linkedCase);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  //proceedings

  @Override
  public ResponseEntity<Void> addApplicationProceeding(
      final Long applicationId,
      final String caabUserLoginId,
      final ProceedingDetail proceeding) {
    applicationService.createProceedingForApplication(applicationId, proceeding);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<List<ProceedingDetail>> getApplicationProceedings(
      final Long applicationId) {
    List<ProceedingDetail> proceedings =
        applicationService.getProceedingsForApplication(applicationId);
    return new ResponseEntity<>(proceedings, HttpStatus.OK);
  }

  //prior authorities

  @Override
  public ResponseEntity<Void> addApplicationPriorAuthority(
      final Long id,
      final String caabUserLoginId,
      final PriorAuthorityDetail priorAuthority) {
    applicationService.createPriorAuthorityForApplication(id, priorAuthority);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<List<PriorAuthorityDetail>> getApplicationPriorAuthorities(
      final Long id) {
    List<PriorAuthorityDetail> priorAuthorities =
        applicationService.getPriorAuthoritiesForApplication(id);
    return new ResponseEntity<>(priorAuthorities, HttpStatus.OK);
  }

  //clients
  @Override
  public ResponseEntity<Void> updateApplicationClient(
      final String clientReferenceId,
      final String caabUserLoginId,
      final BaseClientDetail baseClient) {
    applicationService.updateClient(baseClient, clientReferenceId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  // Opponents
  @Override
  public ResponseEntity<List<OpponentDetail>> getApplicationOpponents(final Long id) {
    List<OpponentDetail> opponents = applicationService.getOpponentsForApplication(id);
    return new ResponseEntity<>(opponents, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> addApplicationOpponent(
      final Long id, final String caabUserLoginId, final OpponentDetail opponent) {
    applicationService.createOpponentForApplication(id, opponent);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
