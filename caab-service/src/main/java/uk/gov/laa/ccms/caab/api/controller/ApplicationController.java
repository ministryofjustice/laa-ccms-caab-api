package uk.gov.laa.ccms.caab.api.controller;


import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.laa.ccms.caab.api.ApplicationsApi;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.Address;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
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

    Long id = applicationService.createApplication(applicationDetail);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(uri);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<ApplicationDetail> getApplication(
      final Long id) {

    ApplicationDetail application = applicationService.getApplication(id);

    return new ResponseEntity<>(application, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Address> getApplicationCorrespondenceAddress(
      final Long id) {
    Address correspondenceAddress =
        applicationService.getApplicationCorrespondenceAddress(id);

    return new ResponseEntity<>(correspondenceAddress, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApplicationProviderDetails> getApplicationProviderDetails(
      final Long id) {

    ApplicationProviderDetails applicationProviderDetails =
        applicationService.getApplicationProviderDetails(id);

    return new ResponseEntity<>(applicationProviderDetails, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ApplicationType> getApplicationType(
      final Long id) {

    ApplicationType applicationType = applicationService.getApplicationType(id);

    return new ResponseEntity<>(applicationType, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> putApplicationCorrespondenceAddress(
      final Long id,
      final String caabUserLoginId,
      final Address address) {

    applicationService.putCorrespondenceAddress(id, address);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> putApplicationProviderDetails(
      final Long id,
      final String caabUserLoginId,
      final ApplicationProviderDetails applicationProviderDetails) {

    applicationService.putProviderDetails(id, applicationProviderDetails);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> putApplicationType(
      final Long id,
      final String caabUserLoginId,
      final ApplicationType applicationType) {

    applicationService.putApplicationType(id, applicationType);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


}
