package uk.gov.laa.ccms.caab.api.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.laa.ccms.caab.api.OpponentsApi;
import uk.gov.laa.ccms.caab.api.service.OpponentService;
import uk.gov.laa.ccms.caab.model.OpponentDetail;

/**
 * Controller handling opponent-related requests.
 * Implements the {@code OpponentsApi} interface for standardized
 * endpoint definitions and provides the necessary endpoints to manage
 * and retrieve opponent data.
 */
@RestController
@RequiredArgsConstructor
public class OpponentController implements OpponentsApi {

  private final OpponentService opponentService;

  @Override
  public ResponseEntity<Void> updateOpponent(
      final Long opponentId, final String caabUserLoginId, final OpponentDetail opponent) {
    opponentService.updateOpponent(opponentId, opponent);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }

  @Override
  public ResponseEntity<Void> removeOpponent(
      final Long opponentId, final String caabUserLoginId) {
    opponentService.removeOpponent(opponentId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
  }
}
