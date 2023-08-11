package uk.gov.laa.ccms.api.controller;


import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.laa.ccms.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.api.ApplicationsApi;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;

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

    return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
  }
}
