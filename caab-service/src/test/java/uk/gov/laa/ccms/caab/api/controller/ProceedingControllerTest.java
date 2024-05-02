package uk.gov.laa.ccms.caab.api.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
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
import uk.gov.laa.ccms.caab.api.service.ProceedingService;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class ProceedingControllerTest {

    @Mock
    private ProceedingService proceedingService;

    @InjectMocks
    private ProceedingController proceedingController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String caabUserLoginId = "userLoginId";

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(proceedingController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
        objectMapper = new ObjectMapper();
    }

  @Test
  public void removeProceeding_RemovesProceeding() throws Exception {
    Long proceedingId = 2L;

    doNothing().when(proceedingService).removeProceeding(proceedingId);

    this.mockMvc.perform(delete("/proceedings/{proceedingId}", proceedingId)
            .header("Caab-User-Login-Id", caabUserLoginId))
        .andExpect(status().isNoContent());

    verify(proceedingService).removeProceeding(proceedingId);
  }

  @Test
  public void updateProceeding_UpdatesProceeding() throws Exception {
    Long proceedingId = 2L;
    ProceedingDetail proceeding = new ProceedingDetail();


    doNothing().when(proceedingService).updateProceeding(proceedingId, proceeding);

    this.mockMvc.perform(patch("/proceedings/{proceeding-id}", proceedingId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .content(objectMapper.writeValueAsString(proceeding)))
        .andExpect(status().isNoContent());

    verify(proceedingService).updateProceeding(proceedingId, proceeding);
  }

  @Test
  public void addProceedingScopeLimitation_CreatesScopeLimitation() throws Exception {
    Long proceedingId = 1L;
    ScopeLimitationDetail scopeLimitation = new ScopeLimitationDetail();

    doNothing().when(proceedingService).createScopeLimitationForProceeding(proceedingId, scopeLimitation);

    this.mockMvc.perform(post("/proceedings/{proceedingId}/scope-limitations", proceedingId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(scopeLimitation)))
        .andExpect(status().isCreated());

    verify(proceedingService).createScopeLimitationForProceeding(proceedingId, scopeLimitation);
  }

  @Test
  public void getApplicationScopeLimitations_ReturnsScopeLimitations() throws Exception {
    Long proceedingId = 1L;
    List<ScopeLimitationDetail> scopeLimitations = List.of(new ScopeLimitationDetail());

    when(proceedingService.getScopeLimitationsForProceeding(proceedingId)).thenReturn(scopeLimitations);

    this.mockMvc.perform(get("/proceedings/{proceedingId}/scope-limitations", proceedingId))
        .andExpect(status().isOk());

    verify(proceedingService).getScopeLimitationsForProceeding(proceedingId);
  }
}