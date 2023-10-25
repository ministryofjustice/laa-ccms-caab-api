package uk.gov.laa.ccms.caab.api.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

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
import uk.gov.laa.ccms.caab.api.controller.ApplicationController;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.Client;
import uk.gov.laa.ccms.caab.model.IntDisplayValue;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;

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

        IntDisplayValue provider = new IntDisplayValue().id(providerId);

        Client client = new Client().reference(clientRef);

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

    @Test
    public void getApplication() throws Exception {
        Long id = 123456L;

        when(applicationService.getApplication(id)).thenReturn(new ApplicationDetail());

        this.mockMvc.perform(get("/applications/{id}", id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void getApplicationType() throws Exception {
        Long id = 123L;
        ApplicationType applicationType = new ApplicationType();

        when(applicationService.getApplicationType(id)).thenReturn(applicationType);

        this.mockMvc.perform(get("/applications/{id}/application-type", id))
            .andExpect(status().isOk());
    }

    @Test
    public void patchApplicationType() throws Exception {
        Long id = 123L;
        String caabUserLoginId = "test";
        ApplicationType applicationType = new ApplicationType();

        // Assuming that your service method returns void (no return value)
        doNothing().when(applicationService).patchApplicationType(id, caabUserLoginId, applicationType);

        this.mockMvc.perform(patch("/applications/{id}/application-type", id)
                .header("Caab-user-Login-Id", caabUserLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicationType)))
            .andExpect(status().isNoContent());

        verify(applicationService).patchApplicationType(id, caabUserLoginId, applicationType);
    }

    @Test
    public void getApplicationProviderDetails() throws Exception {
        Long id = 123L;
        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();

        when(applicationService.getApplicationProviderDetails(id)).thenReturn(providerDetails);

        this.mockMvc.perform(get("/applications/{id}/provider-details", id))
            .andExpect(status().isOk());
    }

    @Test
    public void patchApplicationProviderDetails() throws Exception {
        Long id = 123L;
        String caabUserLoginId = "test";
        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();

        doNothing().when(applicationService).patchProviderDetails(id, caabUserLoginId, providerDetails);

        this.mockMvc.perform(patch("/applications/{id}/provider-details", id)
                .header("Caab-user-Login-Id", caabUserLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(providerDetails)))
            .andExpect(status().isNoContent());

        verify(applicationService).patchProviderDetails(id, caabUserLoginId, providerDetails);
    }
}