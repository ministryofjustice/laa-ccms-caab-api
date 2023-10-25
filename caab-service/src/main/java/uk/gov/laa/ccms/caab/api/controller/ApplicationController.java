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

    Long id = applicationService.createApplication(caabUserLoginId, applicationDetail);

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
  public ResponseEntity<ApplicationProviderDetails> getApplicationProviderDetails(Long id) {

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
  public ResponseEntity<Void> patchApplicationProviderDetails(
      Long id,
      String caabUserLoginId,
      ApplicationProviderDetails applicationProviderDetails) {

    applicationService.patchProviderDetails(id, caabUserLoginId, applicationProviderDetails);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> patchApplicationType(
      final Long id,
      final String caabUserLoginId,
      final ApplicationType applicationType) {

    applicationService.patchApplicationType(id, caabUserLoginId, applicationType);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


}
