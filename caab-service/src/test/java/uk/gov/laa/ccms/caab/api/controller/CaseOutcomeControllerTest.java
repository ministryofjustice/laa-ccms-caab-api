package uk.gov.laa.ccms.caab.api.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildCaseOutcomeDetail;

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
import uk.gov.laa.ccms.caab.api.service.CaseOutcomeService;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetail;
import uk.gov.laa.ccms.caab.model.CaseOutcomeDetails;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class CaseOutcomeControllerTest {

  private final String caabUserLoginId = "userLoginId";

  @Mock
  private CaseOutcomeService caseOutcomeService;
  @InjectMocks
  private CaseOutcomeController caseOutcomeController;
  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    mockMvc = standaloneSetup(caseOutcomeController).setCustomArgumentResolvers(
        new PageableHandlerMethodArgumentResolver()).build();

    objectMapper = new ObjectMapper();
  }

  @Test
  public void getCaseOutcomes_returnsCorrectly() throws Exception {
    final String caseReferenceNumber = "123";
    final String providerId = "456";

    when(caseOutcomeService.getCaseOutcomes(caseReferenceNumber, providerId))
        .thenReturn(new CaseOutcomeDetails());

    this.mockMvc.perform(get("/case-outcomes")
            .param("case-reference-number", caseReferenceNumber)
            .param("provider-id", providerId))
        .andDo(print())
        .andExpect(status().isOk());

    verify(caseOutcomeService).getCaseOutcomes(caseReferenceNumber, providerId);
  }

  @Test
  public void getCaseOutcome_returnsCorrectly() throws Exception {
    final Long caseOutcomeId = 123L;

    when(caseOutcomeService.getCaseOutcome(caseOutcomeId))
        .thenReturn(new CaseOutcomeDetail());

    this.mockMvc.perform(
            get("/case-outcomes/{case-outcome-id}", caseOutcomeId))
        .andDo(print()).andExpect(status().isOk());

    verify(caseOutcomeService).getCaseOutcome(caseOutcomeId);
  }

  @Test
  public void removeCaseOutcome_removesEntry() throws Exception {
    Long caseOutcomeId = 2L;

    doNothing().when(caseOutcomeService).removeCaseOutcome(caseOutcomeId);

    this.mockMvc.perform(
            delete("/case-outcomes/{caseOutcomeId}", caseOutcomeId)
                .header("Caab-User-Login-Id", caabUserLoginId))
        .andExpect(status().isNoContent());

    verify(caseOutcomeService).removeCaseOutcome(caseOutcomeId);
  }

  @Test
  public void createCaseOutcome_isCreated() throws Exception {
    CaseOutcomeDetail caseOutcomeDetail = buildCaseOutcomeDetail();

    when(caseOutcomeService.createCaseOutcome(caseOutcomeDetail)).thenReturn(
        caseOutcomeDetail.getId().longValue());

    this.mockMvc.perform(post("/case-outcomes").header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(caseOutcomeDetail)))
        .andExpect(status().isCreated()).andExpect(
            header().string("Location", "http://localhost/case-outcomes/" + caseOutcomeDetail.getId()));
  }

  @Test
  public void removeCaseOutcomes_callsServiceMethod() throws Exception {
    CaseOutcomeDetail caseOutcomeDetail = buildCaseOutcomeDetail();

    this.mockMvc.perform(delete("/case-outcomes")
            .header("Caab-User-Login-Id", caabUserLoginId)
            .param("case-reference-number", caseOutcomeDetail.getCaseReferenceNumber())
            .param("provider-id", caseOutcomeDetail.getProviderId()))
        .andDo(print())
        .andExpect(status().isNoContent());

    verify(caseOutcomeService).removeCaseOutcomes(
        caseOutcomeDetail.getCaseReferenceNumber(),
        caseOutcomeDetail.getProviderId());
  }
}