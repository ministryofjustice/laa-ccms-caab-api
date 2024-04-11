package uk.gov.laa.ccms.caab.api.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.laa.ccms.caab.api.service.OpponentService;
import uk.gov.laa.ccms.caab.model.Opponent;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class OpponentControllerTest {
    @Mock
    private OpponentService opponentService;


    @InjectMocks
    private OpponentController opponentController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String caabUserLoginId = "userLoginId";

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(opponentController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
        objectMapper = new ObjectMapper();
    }

  @Test
  public void removeOpponent_RemovesOpponent() throws Exception {
    Long opponentId = 2L;

    doNothing().when(opponentService).removeOpponent(opponentId);

    this.mockMvc.perform(delete("/opponents/{opponentId}", opponentId)
            .header("Caab-User-Login-Id", caabUserLoginId))
        .andExpect(status().isNoContent());

    verify(opponentService).removeOpponent(opponentId);
  }

  @Test
  public void updateOpponent_UpdatesOpponent() throws Exception {
    Long opponentId = 2L;
    Opponent opponent = new Opponent();

    doNothing().when(opponentService).updateOpponent(opponentId, opponent);

    this.mockMvc.perform(patch("/opponents/{opponentId}", opponentId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(opponent)))
        .andExpect(status().isNoContent());

    verify(opponentService).updateOpponent(opponentId, opponent);
  }
}