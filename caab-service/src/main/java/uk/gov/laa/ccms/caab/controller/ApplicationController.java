package uk.gov.laa.ccms.caab.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.api.ApplicationsApi;
import uk.gov.laa.ccms.caab.service.ApplicationService;

@RestController
@RequiredArgsConstructor
public class ApplicationController implements ApplicationsApi {

    private final ApplicationService applicationService;


    @Override
    public ResponseEntity<Void> createApplication(final String caabUserLoginId, final ApplicationDetail applicationDetail) {

        applicationService.createApplication(caabUserLoginId, applicationDetail);
        return null;
    }
}
