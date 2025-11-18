package uk.gov.laa.ccms.caab.api.controller;


import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.laa.ccms.caab.api.CaseoutcomeApi;
import uk.gov.laa.ccms.caab.api.service.CaseOutcomeService;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetails;
import uk.gov.laa.ccms.caab.model.ProceedingOutcomeDetail;

/**
 * Controller handling case outcome-related requests.
 * Implements the {@code CaseoutcomeApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve case outcome data.
 */
@RestController
@RequiredArgsConstructor
public class CaseOutcomeController implements CaseoutcomeApi {

  private final CaseOutcomeService caseOutcomeService;

  @Override
  public ResponseEntity<CaseOutcomeDetails> getCaseOutcomes(
      final String caseReferenceNumber,
      final String providerId) {

    CaseOutcomeDetails caseOutcomeDetails = caseOutcomeService.getCaseOutcomes(
        caseReferenceNumber,
        providerId);

    return new ResponseEntity<>(caseOutcomeDetails, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<CaseOutcomeDetail> getCaseOutcome(final Long caseOutcomeId) {

    CaseOutcomeDetail caseOutcomeDetail = caseOutcomeService.getCaseOutcome(caseOutcomeId);

    return new ResponseEntity<>(caseOutcomeDetail, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<ProceedingOutcomeDetail> getProceedingOutcome(
      final String caseReferenceNumber,
      final String providerId,
      final Long proceedingId) {

    ProceedingOutcomeDetail proceedingOutcomeDetail = caseOutcomeService.getProceedingOutcome(
        proceedingId, caseReferenceNumber, providerId);

    return new ResponseEntity<>(proceedingOutcomeDetail, HttpStatus.OK);
  }

  @Override
  public ResponseEntity<Void> createCaseOutcome(
      final String caabUserLoginId,
      final CaseOutcomeDetail caseOutcomeDetail) {
    Long caseOutcomeId = caseOutcomeService.createCaseOutcome(caseOutcomeDetail);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(caseOutcomeId)
        .toUri();

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(uri);

    return new ResponseEntity<>(headers, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<Void> removeCaseOutcome(
      final Long caseOutcomeId, final String caabUserLoginId) {
    caseOutcomeService.removeCaseOutcome(caseOutcomeId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> removeCaseOutcomes(
      final String caabUserLoginId,
      final String caseReferenceNumber,
      final String providerId) {

    caseOutcomeService.removeCaseOutcomes(
        caseReferenceNumber,
        providerId);

    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }


}
