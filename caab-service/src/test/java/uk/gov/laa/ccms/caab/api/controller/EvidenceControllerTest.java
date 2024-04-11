package uk.gov.laa.ccms.caab.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
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
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildEvidenceDocumentDetail;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.laa.ccms.caab.api.service.EvidenceService;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetails;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class EvidenceControllerTest {

  private final String caabUserLoginId = "userLoginId";
  @Mock
  private EvidenceService evidenceService;
  @InjectMocks
  private EvidenceController evidenceController;
  private MockMvc mockMvc;
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setup() {
    mockMvc = standaloneSetup(evidenceController).setCustomArgumentResolvers(
        new PageableHandlerMethodArgumentResolver()).build();

    objectMapper = new ObjectMapper();
  }

  @Test
  public void removeEvidenceDocument_RemovesEntry() throws Exception {
    Long evidenceDocumentId = 2L;

    doNothing().when(evidenceService).removeEvidenceDocument(evidenceDocumentId);

    this.mockMvc.perform(
        delete("/evidence/{evidenceDocumentId}", evidenceDocumentId).header("Caab-User-Login-Id",
            caabUserLoginId)).andExpect(status().isNoContent());

    verify(evidenceService).removeEvidenceDocument(evidenceDocumentId);
  }

  @Test
  public void getEvidenceDocuments_returnsCorrectly() throws Exception {
    EvidenceDocumentDetail evidenceDocumentDetail = buildEvidenceDocumentDetail();

    when(
        evidenceService.getEvidenceDocuments(eq(evidenceDocumentDetail.getApplicationOrOutcomeId()),
            eq(evidenceDocumentDetail.getCaseReferenceNumber()),
            eq(evidenceDocumentDetail.getProviderId()),
            eq(evidenceDocumentDetail.getDocumentType().getId()),
            eq(evidenceDocumentDetail.getTransferStatus()),
            eq(evidenceDocumentDetail.getCcmsModule()), any(Pageable.class))).thenReturn(
        new EvidenceDocumentDetails());

    this.mockMvc.perform(get("/evidence").param("application-or-outcome-id",
                evidenceDocumentDetail.getApplicationOrOutcomeId())
            .param("case-reference-number", evidenceDocumentDetail.getCaseReferenceNumber())
            .param("provider-id", evidenceDocumentDetail.getProviderId())
            .param("document-type", evidenceDocumentDetail.getDocumentType().getId())
            .param("transfer-status", evidenceDocumentDetail.getTransferStatus())
            .param("ccms-module", evidenceDocumentDetail.getCcmsModule())).andDo(print())
        .andExpect(status().isOk());

    verify(evidenceService).getEvidenceDocuments(
        eq(evidenceDocumentDetail.getApplicationOrOutcomeId()),
        eq(evidenceDocumentDetail.getCaseReferenceNumber()),
        eq(evidenceDocumentDetail.getProviderId()),
        eq(evidenceDocumentDetail.getDocumentType().getId()),
        eq(evidenceDocumentDetail.getTransferStatus()), eq(evidenceDocumentDetail.getCcmsModule()),
        any(Pageable.class));
  }

  @Test
  public void getEvidenceDocument_returnsCorrectly() throws Exception {
    EvidenceDocumentDetail evidenceDocumentDetail = buildEvidenceDocumentDetail();

    when(
        evidenceService.getEvidenceDocument(evidenceDocumentDetail.getId().longValue())).thenReturn(
        new EvidenceDocumentDetail());

    this.mockMvc.perform(
            get("/evidence/{evidence-document-id}", evidenceDocumentDetail.getId().longValue()))
        .andDo(print()).andExpect(status().isOk());

    verify(evidenceService).getEvidenceDocument(evidenceDocumentDetail.getId().longValue());
  }

  @Test
  public void createEvidenceDocument_isCreated() throws Exception {
    EvidenceDocumentDetail evidenceDocumentDetail = buildEvidenceDocumentDetail();

    when(evidenceService.createEvidenceDocument(evidenceDocumentDetail)).thenReturn(
        evidenceDocumentDetail.getId().longValue());

    this.mockMvc.perform(post("/evidence").header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(evidenceDocumentDetail)))
        .andExpect(status().isCreated()).andExpect(
            header().string("Location", "http://localhost/evidence/" + evidenceDocumentDetail.getId()));
  }
}