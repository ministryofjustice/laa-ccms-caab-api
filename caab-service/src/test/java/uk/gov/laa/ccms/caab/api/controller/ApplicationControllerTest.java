package uk.gov.laa.ccms.caab.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
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
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.api.service.LinkedCaseService;
import uk.gov.laa.ccms.caab.api.service.OpponentService;
import uk.gov.laa.ccms.caab.api.service.PriorAuthorityService;
import uk.gov.laa.ccms.caab.api.service.ProceedingService;
import uk.gov.laa.ccms.caab.api.service.ScopeLimitationService;
import uk.gov.laa.ccms.caab.model.Address;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.Client;
import uk.gov.laa.ccms.caab.model.CostStructure;
import uk.gov.laa.ccms.caab.model.IntDisplayValue;
import uk.gov.laa.ccms.caab.model.LinkedCase;
import uk.gov.laa.ccms.caab.model.Opponent;
import uk.gov.laa.ccms.caab.model.PriorAuthority;
import uk.gov.laa.ccms.caab.model.Proceeding;
import uk.gov.laa.ccms.caab.model.ScopeLimitation;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
class ApplicationControllerTest {

    @Mock
    private ApplicationService applicationService;

    @Mock
    private LinkedCaseService linkedCaseService;
    @Mock
    private ProceedingService proceedingService;
    @Mock
    private PriorAuthorityService priorAuthorityService;
    @Mock
    private ScopeLimitationService scopeLimitationService;
    @Mock
    private OpponentService opponentService;


    @InjectMocks
    private ApplicationController applicationController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    private final String caabUserLoginId = "userLoginId";

    @BeforeEach
    public void setup() {
        mockMvc = standaloneSetup(applicationController)
            .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
            .build();
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

        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails()
            .provider(new IntDisplayValue().id(providerId));

        Client client = new Client().reference(clientRef);

        StringDisplayValue categoryOfLaw = new StringDisplayValue()
            .id(categoryOfLawId);

        ApplicationDetail applicationDetail = new ApplicationDetail()
            .caseReferenceNumber(caseReferenceNumber)
            .categoryOfLaw(categoryOfLaw)
            .client(client)
            .providerDetails(providerDetails);

        when(applicationService.createApplication(applicationDetail)).thenReturn(id);

        this.mockMvc.perform(post("/applications")
                .header("Caab-User-Login-Id", loginId)
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
    public void putApplicationType() throws Exception {
        Long id = 123L;
        String caabUserLoginId = "test";
        ApplicationType applicationType = new ApplicationType();

        // Assuming that your service method returns void (no return value)
        doNothing().when(applicationService).putApplicationType(id, applicationType);

        this.mockMvc.perform(put("/applications/{id}/application-type", id)
                .header("Caab-User-Login-Id", caabUserLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(applicationType)))
            .andExpect(status().isNoContent());

        verify(applicationService).putApplicationType(id, applicationType);
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
    public void putApplicationProviderDetails() throws Exception {
        Long id = 123L;
        String caabUserLoginId = "test";
        ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();

        doNothing().when(applicationService).putProviderDetails(id, providerDetails);

        this.mockMvc.perform(put("/applications/{id}/provider-details", id)
                .header("Caab-User-Login-Id", caabUserLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(providerDetails)))
            .andExpect(status().isNoContent());

        verify(applicationService).putProviderDetails(id, providerDetails);
    }

    @Test
    public void getApplicationCorrespondenceAddress() throws Exception {
        Long id = 123L;
        Address address = new Address();

        when(applicationService.getApplicationCorrespondenceAddress(id)).thenReturn(address);

        this.mockMvc.perform(get("/applications/{id}/correspondence-address", id))
            .andExpect(status().isOk());
    }

    @Test
    public void putApplicationCorrespondenceAddress() throws Exception {
        Long id = 123L;
        String caabUserLoginId = "test";
        Address address = new Address();

        doNothing().when(applicationService).putCorrespondenceAddress(id, address);

        this.mockMvc.perform(put("/applications/{id}/correspondence-address", id)
                .header("Caab-User-Login-Id", caabUserLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(address)))
            .andExpect(status().isNoContent());

        verify(applicationService).putCorrespondenceAddress(id, address);
    }

    @Test
    public void getApplicationLinkedCases_returnsLinkedCases() throws Exception {
        Long id = 1L;
        List<LinkedCase> linkedCases = Arrays.asList(new LinkedCase(), new LinkedCase()); // Assuming some mock linked cases

        when(applicationService.getLinkedCasesForApplication(id)).thenReturn(linkedCases);

        this.mockMvc.perform(get("/applications/{id}/linked-cases", id))
            .andExpect(status().isOk());

        verify(applicationService).getLinkedCasesForApplication(id);
    }

    @Test
    public void addApplicationLinkedCase_isCreated() throws Exception {
        Long id = 1L;
        String caabUserLoginId = "userLoginId";
        LinkedCase linkedCase = new LinkedCase(); // Set up linked case details as required

        doNothing().when(applicationService).createLinkedCaseForApplication(id, linkedCase);

        this.mockMvc.perform(post("/applications/{id}/linked-cases", id)
                .header("Caab-User-Login-Id", caabUserLoginId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(linkedCase)))
            .andExpect(status().isCreated());

        verify(applicationService).createLinkedCaseForApplication(id, linkedCase);
    }

    @Test
    public void updateApplicationLinkedCase_noContent() throws Exception {
        Long linkedCaseId = 2L;
        String caabUserLoginId = "userLoginId";
        LinkedCase linkedCase = new LinkedCase(); // Set up updated linked case details as required

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
        String caabUserLoginId = "userLoginId";

        doNothing().when(linkedCaseService).removeLinkedCase(linkedCaseId);

        this.mockMvc.perform(delete("/linked-cases/{linkedCaseId}", linkedCaseId)
                .header("Caab-User-Login-Id", caabUserLoginId))
            .andExpect(status().isNoContent());

        verify(linkedCaseService).removeLinkedCase(linkedCaseId);
    }

    @Test
    public void getApplications() throws Exception {
        String caseRef = "caseref";
        String providerRef = "provref";
        String clientSurname = "surname";
        String clientRef = "ref";
        String feeEarner = "fee";
        String officeId = "123";
        String status = "stat";

        when(applicationService.getApplications(
            eq(caseRef),
            eq(providerRef),
            eq(clientSurname),
            eq(clientRef),
            eq(feeEarner),
            eq(Integer.parseInt(officeId)),
            eq(status),
            any(Pageable.class))).thenReturn(new ApplicationDetails());

        this.mockMvc.perform(get("/applications")
                .param("case-reference-number", caseRef)
                .param("provider-case-ref", providerRef)
                .param("client-surname", clientSurname)
                .param("client-reference", clientRef)
                .param("fee-earner", feeEarner)
                .param("office-id", officeId)
                .param("status", status))
            .andDo(print())
            .andExpect(status().isOk());
    }

  @Test
  public void getApplicationCostStructure_ReturnsCostStructure() throws Exception {
    Long applicationId = 1L;
    CostStructure costStructure = new CostStructure();

    when(applicationService.getApplicationCostStructure(applicationId)).thenReturn(costStructure);

    this.mockMvc.perform(get("/applications/{id}/cost-structure", applicationId))
        .andExpect(status().isOk());

    verify(applicationService).getApplicationCostStructure(applicationId);
  }

  @Test
  public void updateApplicationCostStructure_UpdatesSuccessfully() throws Exception {
    Long applicationId = 1L;
    CostStructure costStructure = new CostStructure();

    doNothing().when(applicationService).putCostStructure(applicationId, costStructure);

    this.mockMvc.perform(put("/applications/{id}/cost-structure", applicationId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(costStructure)))
        .andExpect(status().isNoContent());

    verify(applicationService).putCostStructure(applicationId, costStructure);
  }

  @Test
  public void addApplicationProceeding_CreatesProceeding() throws Exception {
    Long applicationId = 1L;
    Proceeding proceeding = new Proceeding();

    doNothing().when(applicationService).createProceedingForApplication(applicationId, proceeding);

    this.mockMvc.perform(post("/applications/{id}/proceedings", applicationId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proceeding)))
        .andExpect(status().isCreated());

    verify(applicationService).createProceedingForApplication(applicationId, proceeding);
  }

  @Test
  public void getApplicationProceedings_ReturnsProceedingsList() throws Exception {
    Long applicationId = 1L;
    List<Proceeding> proceedings = List.of(new Proceeding());

    when(applicationService.getProceedingsForApplication(applicationId)).thenReturn(proceedings);

    this.mockMvc.perform(get("/applications/{id}/proceedings", applicationId))
        .andExpect(status().isOk());

    verify(applicationService).getProceedingsForApplication(applicationId);
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
    Proceeding proceeding = new Proceeding();


    doNothing().when(proceedingService).updateProceeding(proceedingId, proceeding);

    this.mockMvc.perform(patch("/proceedings/{proceeding-id}", proceedingId)
            .contentType(MediaType.APPLICATION_JSON)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .content(objectMapper.writeValueAsString(proceeding)))
        .andExpect(status().isNoContent());

    verify(proceedingService).updateProceeding(proceedingId, proceeding);
  }

  @Test
  public void addApplicationPriorAuthority_CreatesPriorAuthority() throws Exception {
    Long id = 1L;
    PriorAuthority priorAuthority = new PriorAuthority();

    doNothing().when(applicationService).createPriorAuthorityForApplication(id, priorAuthority);

    this.mockMvc.perform(post("/applications/{id}/prior-authorities", id)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(priorAuthority)))
        .andExpect(status().isCreated());

    verify(applicationService).createPriorAuthorityForApplication(id, priorAuthority);
  }

  @Test
  public void getApplicationPriorAuthorities_ReturnsPriorAuthoritiesList() throws Exception {
    Long id = 1L;
    List<PriorAuthority> priorAuthorities = List.of(new PriorAuthority());

    when(applicationService.getPriorAuthoritiesForApplication(id)).thenReturn(priorAuthorities);

    this.mockMvc.perform(get("/applications/{id}/prior-authorities", id))
        .andExpect(status().isOk());

    verify(applicationService).getPriorAuthoritiesForApplication(id);
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

  @Test
  public void addProceedingScopeLimitation_CreatesScopeLimitation() throws Exception {
    Long proceedingId = 1L;
    ScopeLimitation scopeLimitation = new ScopeLimitation();

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
    List<ScopeLimitation> scopeLimitations = List.of(new ScopeLimitation());

    when(proceedingService.getScopeLimitationsForProceeding(proceedingId)).thenReturn(scopeLimitations);

    this.mockMvc.perform(get("/proceedings/{proceedingId}/scope-limitations", proceedingId))
        .andExpect(status().isOk());

    verify(proceedingService).getScopeLimitationsForProceeding(proceedingId);
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

  @Test
  public void addApplicationOpponent_CreatesOpponent() throws Exception {
    Long applicationId = 1L;
    Opponent opponent = new Opponent();

    doNothing().when(applicationService).createOpponentForApplication(applicationId, opponent);

    this.mockMvc.perform(post("/applications/{applicationId}/opponents", applicationId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(opponent)))
        .andExpect(status().isCreated());

    verify(applicationService).createOpponentForApplication(applicationId, opponent);
  }

  @Test
  public void getApplicationOpponents_ReturnsOpponents() throws Exception {
    Long applicationId = 1L;
    List<Opponent> opponents = List.of(new Opponent());

    when(applicationService.getOpponentsForApplication(applicationId)).thenReturn(opponents);

    this.mockMvc.perform(get("/applications/{applicationId}/opponents", applicationId))
        .andExpect(status().isOk());

    verify(applicationService).getOpponentsForApplication(applicationId);
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