package uk.gov.laa.ccms.caab.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.laa.ccms.caab.api.PriorAuthoritiesApi;
import uk.gov.laa.ccms.caab.api.service.PriorAuthorityService;
import uk.gov.laa.ccms.caab.model.PriorAuthority;

/**
 * Controller handling prior authorities-related requests.
 * Implements the {@code PriorAuthoritiesApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve prior authorities data.
 */
@RestController
@RequiredArgsConstructor
public class PriorAuthorityController implements PriorAuthoritiesApi {

  private final PriorAuthorityService priorAuthorityService;

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
}
