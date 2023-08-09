package uk.gov.laa.ccms.caab.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.laa.ccms.api.controller.ApplicationController;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetailClient;
import uk.gov.laa.ccms.caab.model.ApplicationDetailProvider;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;
import uk.gov.laa.ccms.api.service.ApplicationService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationController applicationController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(applicationController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createApplication_isCreated() throws Exception {
        String loginId = "test";
        String caseReferenceNumber = "30001234";
        String categoryOfLawId = "TEST";
        int providerId = 12345;
        String clientRef = "clientRef";
        Long id = 1L;

        ApplicationDetailProvider provider = new ApplicationDetailProvider(providerId);

        ApplicationDetailClient client = new ApplicationDetailClient()
                .reference(clientRef);

        StringDisplayValue categoryOfLaw = new StringDisplayValue()
                .id(categoryOfLawId);

        ApplicationDetail applicationDetail = new ApplicationDetail()
                .caseReferenceNumber(caseReferenceNumber)
                .provider(provider)
                .client(client)
                .categoryOfLaw(categoryOfLaw);

        when(applicationService.createApplication(loginId, applicationDetail)).thenReturn(id);

        this.mockMvc.perform(post("/applications")
                        .header("Caab-user-Login-Id", loginId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(applicationDetail)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/applications/" + id));
    }
}