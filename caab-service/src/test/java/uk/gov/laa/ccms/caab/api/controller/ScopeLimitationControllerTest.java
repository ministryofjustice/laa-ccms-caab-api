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
import uk.gov.laa.ccms.caab.api.service.ScopeLimitationService;
import uk.gov.laa.ccms.caab.model.ScopeLimitation;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class ScopeLimitationControllerTest {
    @Mock
    private ScopeLimitationService scopeLimitationService;

    @InjectMocks
    private ScopeLimitationController scopeLimitationController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String caabUserLoginId = "userLoginId";

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(scopeLimitationController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
        objectMapper = new ObjectMapper();
    }

  @Test
  public void removeScopeLimitation_RemovesScopeLimitation() throws Exception {
    Long scopeLimitationId = 2L;

    doNothing().when(scopeLimitationService).removeScopeLimitation(scopeLimitationId);

    this.mockMvc.perform(delete("/scope-limitations/{scopeLimitationId}", scopeLimitationId)
            .header("Caab-User-Login-Id", caabUserLoginId))
        .andExpect(status().isNoContent());

    verify(scopeLimitationService).removeScopeLimitation(scopeLimitationId);
  }

  @Test
  public void updateScopeLimitation_UpdatesScopeLimitation() throws Exception {
    Long scopeLimitationId = 2L;
    ScopeLimitation scopeLimitation = new ScopeLimitation();

    doNothing().when(scopeLimitationService).updateScopeLimitation(scopeLimitationId, scopeLimitation);

    this.mockMvc.perform(patch("/scope-limitations/{scopeLimitationId}", scopeLimitationId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(scopeLimitation)))
        .andExpect(status().isNoContent());

    verify(scopeLimitationService).updateScopeLimitation(scopeLimitationId, scopeLimitation);
  }
}