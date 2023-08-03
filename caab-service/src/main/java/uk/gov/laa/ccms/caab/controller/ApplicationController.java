package uk.gov.laa.ccms.caab.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uk.gov.laa.ccms.caab.api.ApplicationsApi;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.service.ApplicationService;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class ApplicationController implements ApplicationsApi {

    private final ApplicationService applicationService;

    @Override
    public ResponseEntity<Void> createApplication(final String caabUserLoginId, final ApplicationDetail applicationDetail) {

        String id = applicationService.createApplication(caabUserLoginId, applicationDetail);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{case-reference}")
                .buildAndExpand(id)
                .toUri();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(uri);

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
}
