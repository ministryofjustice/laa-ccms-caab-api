package uk.gov.laa.ccms.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.api.entity.Address;
import uk.gov.laa.ccms.api.entity.Application;
import uk.gov.laa.ccms.api.entity.AuditTrail;
import uk.gov.laa.ccms.api.entity.CostStructure;
import uk.gov.laa.ccms.api.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.api.repository.ApplicationRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    public Long createApplication(final String caabUserLoginId, final ApplicationDetail applicationDetail){

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setCreatedBy(caabUserLoginId);
        auditTrail.setModifiedBy(caabUserLoginId);

        Application application = applicationMapper.toApplication(applicationDetail);
        application.setAuditTrail(auditTrail);

        //set correspondence address
        if (application.getCorrespondenceAddress() == null) {
            Address address = new Address(auditTrail);
            application.setCorrespondenceAddress(address);
        } else {
            application.getCorrespondenceAddress().setAuditTrail(auditTrail);
        }

        //Set Cost Structure
        if (application.getCosts() == null) {
            CostStructure costs = new CostStructure(auditTrail);
            application.setCosts(costs);
        } else {
            application.getCosts().setAuditTrail(auditTrail);
        }

        applicationRepository.save(application);

        return application.getId();
    }
}
