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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.laa.ccms.caab.api.service.LinkedCaseService;
import uk.gov.laa.ccms.caab.model.LinkedCaseDetail;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class LinkedCaseControllerTest {

    @Mock
    private LinkedCaseService linkedCaseService;

    @InjectMocks
    private LinkedCaseController linkedCaseController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String caabUserLoginId = "userLoginId";

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(linkedCaseController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void updateLinkedCase_noContent() throws Exception {
        Long linkedCaseId = 2L;
        LinkedCaseDetail linkedCase = new LinkedCaseDetail(); // Set up updated linked case details as required

        doNothing().when(linkedCaseService).updateLinkedCase(linkedCaseId, linkedCase);

        this.mockMvc.perform(patch("/linked-cases/{linkedCaseId}", linkedCaseId)
                .header("Caab-User-Login-Id", caabUserLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(linkedCase)))
            .andExpect(status().isNoContent());

        verify(linkedCaseService).updateLinkedCase(linkedCaseId, linkedCase);
    }

    @Test
    public void removeLinkedCase_noContent() throws Exception {
        Long linkedCaseId = 2L;

        doNothing().when(linkedCaseService).removeLinkedCase(linkedCaseId);

        this.mockMvc.perform(delete("/linked-cases/{linkedCaseId}", linkedCaseId)
                .header("Caab-User-Login-Id", caabUserLoginId))
            .andExpect(status().isNoContent());

        verify(linkedCaseService).removeLinkedCase(linkedCaseId);
    }
}