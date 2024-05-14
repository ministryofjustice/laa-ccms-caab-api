package uk.gov.laa.ccms.caab.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.laa.ccms.caab.api.LinkedCasesApi;
import uk.gov.laa.ccms.caab.api.service.LinkedCaseService;
import uk.gov.laa.ccms.caab.model.LinkedCaseDetail;

/**
 * Controller handling linked case-related requests.
 * Implements the {@code LinkedCasesApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve linked case data.
 */
@RestController
@RequiredArgsConstructor
public class LinkedCaseController implements LinkedCasesApi {

  private final LinkedCaseService linkedCaseService;

  /**
   * Updates a linked case.
   *
   * @param linkedCaseId the unique identifier of the linked case
   * @param caabUserLoginId the user login ID used for audit trail
   * @param linkedCase the updated linked case
   * @return a ResponseEntity with no content
   */
  @Override
  public ResponseEntity<Void> updateLinkedCase(
      final Long linkedCaseId,
      final String caabUserLoginId,
      final LinkedCaseDetail linkedCase) {
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
  public ResponseEntity<Void> removeLinkedCase(
      final Long linkedCaseId,
      final String caabUserLoginId) {
    linkedCaseService.removeLinkedCase(linkedCaseId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
