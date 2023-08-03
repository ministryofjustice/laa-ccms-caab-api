package uk.gov.laa.ccms.caab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.entity.Address;
import uk.gov.laa.ccms.caab.entity.Application;
import uk.gov.laa.ccms.caab.entity.AuditTrail;
import uk.gov.laa.ccms.caab.entity.CostStructure;
import uk.gov.laa.ccms.caab.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.repository.ApplicationRepository;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    public void createApplication(final String caabUserLoginId, final ApplicationDetail applicationDetail){

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
    }
}
