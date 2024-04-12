package uk.gov.laa.ccms.data.api.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import uk.gov.laa.ccms.caab.api.controller.OpponentController;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.Opponent;

public abstract class BaseOpponentControllerIntegrationTest
    extends AbstractControllerIntegrationTest {

  @Autowired
  private OpponentController opponentController;

  @Autowired
  private ApplicationService applicationService;

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/opponent_insert.sql"})
  public void updateOpponent() throws IOException {
    Long opponentId = 3L;

    Opponent updatedOpponent = loadObjectFromJson("/json/opponent_new.json", Opponent.class);

    ResponseEntity<Void> response = opponentController.updateOpponent(opponentId, caabUserLoginId, updatedOpponent);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
  }

  @Test
  @Sql(scripts = {"/sql/application_insert.sql", "/sql/opponent_insert.sql"})
  public void removeOpponent() {
    Long caseRef = 41L;
    Long opponentId = 3L;

    opponentController.removeOpponent(opponentId, caabUserLoginId);
    List<Opponent> opponents = applicationService.getOpponentsForApplication(caseRef);
    assertEquals(0, opponents.size());
  }

}
