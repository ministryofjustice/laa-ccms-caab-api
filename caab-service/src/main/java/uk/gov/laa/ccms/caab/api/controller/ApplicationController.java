package uk.gov.laa.ccms.caab.api.controller;


import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.laa.ccms.caab.api.ApplicationsApi;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.Address;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;

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

  @Override
  public ResponseEntity<Address> getApplicationCorrespondenceAddress(
      final Long applicationId) {
    Address correspondenceAddress =
        applicationService.getApplicationCorrespondenceAddress(applicationId);

    return new ResponseEntity<>(correspondenceAddress, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApplicationProviderDetails> getApplicationProviderDetails(
      final Long applicationId) {

    ApplicationProviderDetails applicationProviderDetails =
        applicationService.getApplicationProviderDetails(applicationId);

    return new ResponseEntity<>(applicationProviderDetails, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApplicationType> getApplicationType(
      final Long applicationId) {

    ApplicationType applicationType = applicationService.getApplicationType(applicationId);

    return new ResponseEntity<>(applicationType, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> putApplicationCorrespondenceAddress(
      final Long applicationId,
      final String caabUserLoginId,
      final Address address) {

    applicationService.putCorrespondenceAddress(applicationId, address);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> putApplicationProviderDetails(
      final Long applicationId,
      final String caabUserLoginId,
      final ApplicationProviderDetails applicationProviderDetails) {

    applicationService.putProviderDetails(applicationId, applicationProviderDetails);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> putApplicationType(
      final Long applicationId,
      final String caabUserLoginId,
      final ApplicationType applicationType) {

    applicationService.putApplicationType(applicationId, applicationType);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


}
