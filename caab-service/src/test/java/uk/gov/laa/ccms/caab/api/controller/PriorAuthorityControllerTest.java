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
import uk.gov.laa.ccms.caab.api.service.PriorAuthorityService;
import uk.gov.laa.ccms.caab.model.PriorAuthority;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class PriorAuthorityControllerTest {
    @Mock
    private PriorAuthorityService priorAuthorityService;

    @InjectMocks
    private PriorAuthorityController priorAuthorityController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String caabUserLoginId = "userLoginId";

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(priorAuthorityController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
        objectMapper = new ObjectMapper();
    }

  @Test
  public void removePriorAuthority_RemovesPriorAuthority() throws Exception {
    Long priorAuthorityId = 2L;

    doNothing().when(priorAuthorityService).removePriorAuthority(priorAuthorityId);

    this.mockMvc.perform(delete("/prior-authorities/{priorAuthorityId}", priorAuthorityId)
            .header("Caab-User-Login-Id", caabUserLoginId))
        .andExpect(status().isNoContent());

    verify(priorAuthorityService).removePriorAuthority(priorAuthorityId);
  }

  @Test
  public void updatePriorAuthority_UpdatesPriorAuthority() throws Exception {
    Long priorAuthorityId = 2L;
    PriorAuthority priorAuthority = new PriorAuthority();

    doNothing().when(priorAuthorityService).updatePriorAuthority(priorAuthorityId, priorAuthority);

    this.mockMvc.perform(patch("/prior-authorities/{priorAuthorityId}", priorAuthorityId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(priorAuthority)))
        .andExpect(status().isNoContent());

    verify(priorAuthorityService).updatePriorAuthority(priorAuthorityId, priorAuthority);
  }

}