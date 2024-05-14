package uk.gov.laa.ccms.caab.api.controller;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.laa.ccms.caab.api.ProceedingsApi;
import uk.gov.laa.ccms.caab.api.service.ProceedingService;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;

/**
 * Controller handling proceedings-related requests.
 * Implements the {@code ProceedingsApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve proceedings data.
 */
@RestController
@RequiredArgsConstructor
public class ProceedingController implements ProceedingsApi {

  private final ProceedingService proceedingService;

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
      final ProceedingDetail proceeding) {
    proceedingService.updateProceeding(proceedingId, proceeding);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<List<ScopeLimitationDetail>> getProceedingsScopeLimitations(
      final Long proceedingId) {
    List<ScopeLimitationDetail> scopeLimitations =
        proceedingService.getScopeLimitationsForProceeding(proceedingId);
    return new ResponseEntity<>(scopeLimitations, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> addProceedingScopeLimitation(
      final Long proceedingId,
      final String caabUserLoginId,
      final ScopeLimitationDetail scopeLimitation) {
    proceedingService.createScopeLimitationForProceeding(proceedingId, scopeLimitation);
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
