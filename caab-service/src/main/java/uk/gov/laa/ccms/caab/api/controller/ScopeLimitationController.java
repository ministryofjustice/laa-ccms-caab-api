package uk.gov.laa.ccms.caab.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.laa.ccms.caab.api.ScopeLimitationsApi;
import uk.gov.laa.ccms.caab.api.service.ScopeLimitationService;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;

/**
 * Controller handling scope limitation-related requests.
 * Implements the {@code ScopeLimitationsApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve scope limitation data.
 */
@RestController
@RequiredArgsConstructor
public class ScopeLimitationController implements ScopeLimitationsApi {

  private final ScopeLimitationService scopeLimitationService;

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
      final ScopeLimitationDetail scopeLimitation) {
    scopeLimitationService.updateScopeLimitation(scopeLimitationId, scopeLimitation);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
