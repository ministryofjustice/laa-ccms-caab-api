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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.laa.ccms.caab.api.service.ApplicationService;
import uk.gov.laa.ccms.caab.model.AddressDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.ClientDetail;
import uk.gov.laa.ccms.caab.model.CostStructureDetail;
import uk.gov.laa.ccms.caab.model.IntDisplayValue;
import uk.gov.laa.ccms.caab.model.LinkedCaseDetail;
import uk.gov.laa.ccms.caab.model.OpponentDetail;
import uk.gov.laa.ccms.caab.model.PriorAuthorityDetail;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;
import uk.gov.laa.ccms.caab.model.StringDisplayValue;

@ExtendWith(MockitoExtension.class)
@WebAppConfiguration
class ApplicationControllerTest {

  @Mock
  private ApplicationService applicationService;

  @InjectMocks
  private ApplicationController applicationController;

  private MockMvc mockMvc;

  private ObjectMapper objectMapper;

  private final String caabUserLoginId = "userLoginId";

  @BeforeEach
  void setup() {
    mockMvc = standaloneSetup(applicationController)
        .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
        .build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void createApplication_isCreated() throws Exception {
    String loginId = "test";
    String caseReferenceNumber = "30001234";
    String categoryOfLawId = "TEST";
    int providerId = 12345;
    String clientRef = "clientRef";
    Long id = 1L;

    ApplicationProviderDetails providerDetails = new ApplicationProviderDetails()
        .provider(new IntDisplayValue().id(providerId));

    ClientDetail client = new ClientDetail().reference(clientRef);

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
  void getApplication() throws Exception {
    Long id = 123456L;

    when(applicationService.getApplication(id)).thenReturn(new ApplicationDetail());

    this.mockMvc.perform(get("/applications/{id}", id))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void getApplicationCount() throws Exception {
    when(applicationService.getTotalApplications()).thenReturn(123L);

    this.mockMvc.perform(get("/applications/_count"))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  void updateApplication() throws Exception {
    Long id = 1L;
    String caabUserLoginId = "testUserLoginId";
    ApplicationDetail applicationDetail = new ApplicationDetail();

    doNothing().when(applicationService).updateApplication(id, applicationDetail);

    this.mockMvc.perform(patch("/applications/{id}", id)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(applicationDetail)))
        .andExpect(status().isNoContent());

    verify(applicationService).updateApplication(id, applicationDetail);
  }

  @Test
  void removeApplication() throws Exception {
    Long id = 1L;
    String caabUserLoginId = "testUserLoginId";

    doNothing().when(applicationService).removeApplication(id);

    this.mockMvc.perform(delete("/applications/{id}", id)
            .header("Caab-User-Login-Id", caabUserLoginId))
        .andExpect(status().isNoContent());

    verify(applicationService).removeApplication(id);
  }


  @Test
  void getApplicationType() throws Exception {
    Long id = 123L;
    ApplicationType applicationType = new ApplicationType();

    when(applicationService.getApplicationType(id)).thenReturn(applicationType);

    this.mockMvc.perform(get("/applications/{id}/application-type", id))
        .andExpect(status().isOk());
  }

  @Test
  void putApplicationType() throws Exception {
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
  void getApplicationProviderDetails() throws Exception {
    Long id = 123L;
    ApplicationProviderDetails providerDetails = new ApplicationProviderDetails();

    when(applicationService.getApplicationProviderDetails(id)).thenReturn(providerDetails);

    this.mockMvc.perform(get("/applications/{id}/provider-details", id))
        .andExpect(status().isOk());
  }

  @Test
  void putApplicationProviderDetails() throws Exception {
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
  void getApplicationCorrespondenceAddressDetail() throws Exception {
    Long id = 123L;
    AddressDetail address = new AddressDetail();

    when(applicationService.getApplicationCorrespondenceAddress(id)).thenReturn(address);

    this.mockMvc.perform(get("/applications/{id}/correspondence-address", id))
        .andExpect(status().isOk());
  }

  @Test
  void putApplicationCorrespondenceAddressDetail() throws Exception {
    Long id = 123L;
    String caabUserLoginId = "test";
    AddressDetail address = new AddressDetail();

    doNothing().when(applicationService).putCorrespondenceAddress(id, address);

    this.mockMvc.perform(put("/applications/{id}/correspondence-address", id)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(address)))
        .andExpect(status().isNoContent());

    verify(applicationService).putCorrespondenceAddress(id, address);
  }

  @Test
  void getApplicationLinkedCases_returnsLinkedCases() throws Exception {
    Long id = 1L;
    List<LinkedCaseDetail> linkedCases = Arrays.asList(new LinkedCaseDetail(),
        new LinkedCaseDetail()); // Assuming some mock linked cases

    when(applicationService.getLinkedCasesForApplication(id)).thenReturn(linkedCases);

    this.mockMvc.perform(get("/applications/{id}/linked-cases", id))
        .andExpect(status().isOk());

    verify(applicationService).getLinkedCasesForApplication(id);
  }

  @Test
  void addApplicationLinkedCase_isCreated() throws Exception {
    Long id = 1L;
    String caabUserLoginId = "userLoginId";
    LinkedCaseDetail linkedCase = new LinkedCaseDetail(); // Set up linked case details as required

    doNothing().when(applicationService).createLinkedCaseForApplication(id, linkedCase);

    this.mockMvc.perform(post("/applications/{id}/linked-cases", id)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(linkedCase)))
        .andExpect(status().isCreated());

    verify(applicationService).createLinkedCaseForApplication(id, linkedCase);
  }

  @Test
  void getApplications() throws Exception {
    String caseRef = "caseref";
    String providerId = "provid";
    String providerRef = "provref";
    String clientSurname = "surname";
    String clientRef = "ref";
    String feeEarner = "fee";
    String officeId = "123";
    String status = "stat";

    when(applicationService.getApplications(
        eq(caseRef),
        eq(providerId),
        eq(providerRef),
        eq(clientSurname),
        eq(clientRef),
        eq(feeEarner),
        eq(Integer.parseInt(officeId)),
        eq(status),
        any(Pageable.class))).thenReturn(new ApplicationDetails());

    this.mockMvc.perform(get("/applications")
            .param("case-reference-number", caseRef)
            .param("provider-id", providerId)
            .param("provider-case-ref", providerRef)
            .param("client-surname", clientSurname)
            .param("client-reference", clientRef)
            .param("fee-earner", feeEarner)
            .param("office-id", officeId)
            .param("status", status))
        .andDo(print())
        .andExpect(status().isOk());

    verify(applicationService).getApplications(
        eq(caseRef),
        eq(providerId),
        eq(providerRef),
        eq(clientSurname),
        eq(clientRef),
        eq(feeEarner),
        eq(Integer.parseInt(officeId)),
        eq(status),
        any(Pageable.class));
  }

  @Test
  void getApplicationCostStructureDetail_ReturnsCostStructureDetail() throws Exception {
    Long applicationId = 1L;
    CostStructureDetail costStructure = new CostStructureDetail();

    when(applicationService.getApplicationCostStructure(applicationId)).thenReturn(costStructure);

    this.mockMvc.perform(get("/applications/{id}/cost-structure", applicationId))
        .andExpect(status().isOk());

    verify(applicationService).getApplicationCostStructure(applicationId);
  }

  @Test
  void updateApplicationCostStructureDetail_UpdatesSuccessfully() throws Exception {
    Long applicationId = 1L;
    CostStructureDetail costStructure = new CostStructureDetail();

    doNothing().when(applicationService).putCostStructure(applicationId, costStructure);

    this.mockMvc.perform(put("/applications/{id}/cost-structure", applicationId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(costStructure)))
        .andExpect(status().isNoContent());

    verify(applicationService).putCostStructure(applicationId, costStructure);
  }

  @Test
  void addApplicationProceeding_CreatesProceeding() throws Exception {
    Long applicationId = 1L;
    ProceedingDetail proceeding = new ProceedingDetail();

    doNothing().when(applicationService).createProceedingForApplication(applicationId, proceeding);

    this.mockMvc.perform(post("/applications/{id}/proceedings", applicationId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(proceeding)))
        .andExpect(status().isCreated());

    verify(applicationService).createProceedingForApplication(applicationId, proceeding);
  }

  @Test
  void getApplicationProceedings_ReturnsProceedingsList() throws Exception {
    Long applicationId = 1L;
    List<ProceedingDetail> proceedings = List.of(new ProceedingDetail());

    when(applicationService.getProceedingsForApplication(applicationId)).thenReturn(proceedings);

    this.mockMvc.perform(get("/applications/{id}/proceedings", applicationId))
        .andExpect(status().isOk());

    verify(applicationService).getProceedingsForApplication(applicationId);
  }

  @Test
  void addApplicationPriorAuthority_CreatesPriorAuthority() throws Exception {
    Long id = 1L;
    PriorAuthorityDetail priorAuthority = new PriorAuthorityDetail();

    doNothing().when(applicationService).createPriorAuthorityForApplication(id, priorAuthority);

    this.mockMvc.perform(post("/applications/{id}/prior-authorities", id)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(priorAuthority)))
        .andExpect(status().isCreated());

    verify(applicationService).createPriorAuthorityForApplication(id, priorAuthority);
  }

  @Test
  void getApplicationPriorAuthorities_ReturnsPriorAuthoritiesList() throws Exception {
    Long id = 1L;
    List<PriorAuthorityDetail> priorAuthorities = List.of(new PriorAuthorityDetail());

    when(applicationService.getPriorAuthoritiesForApplication(id)).thenReturn(priorAuthorities);

    this.mockMvc.perform(get("/applications/{id}/prior-authorities", id))
        .andExpect(status().isOk());

    verify(applicationService).getPriorAuthoritiesForApplication(id);
  }

  @Test
  void addApplicationOpponent_CreatesOpponent() throws Exception {
    Long applicationId = 1L;
    OpponentDetail opponent = new OpponentDetail();

    doNothing().when(applicationService).createOpponentForApplication(applicationId, opponent);

    this.mockMvc.perform(post("/applications/{applicationId}/opponents", applicationId)
            .header("Caab-User-Login-Id", caabUserLoginId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(opponent)))
        .andExpect(status().isCreated());

    verify(applicationService).createOpponentForApplication(applicationId, opponent);
  }

  @Test
  void getApplicationOpponents_ReturnsOpponents() throws Exception {
    Long applicationId = 1L;
    List<OpponentDetail> opponents = List.of(new OpponentDetail());

    when(applicationService.getOpponentsForApplication(applicationId)).thenReturn(opponents);

    this.mockMvc.perform(get("/applications/{applicationId}/opponents", applicationId))
        .andExpect(status().isOk());

    verify(applicationService).getOpponentsForApplication(applicationId);
  }
}