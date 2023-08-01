package uk.gov.laa.ccms.caab.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.gov.laa.ccms.caab.entity.Application;
import uk.gov.laa.ccms.caab.entity.AuditTrail;
import uk.gov.laa.ccms.caab.mapper.ApplicationMapper;
import uk.gov.laa.ccms.caab.repository.ApplicationRepository;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;

@Service
@RequiredArgsConstructor
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    private final ApplicationMapper applicationMapper;

    public void createApplication(final String caabUserLoginId, final ApplicationDetail applicationDetail){

        Application application = applicationMapper.toApplication(applicationDetail);

        AuditTrail auditTrail = new AuditTrail();
        auditTrail.setCreatedBy(caabUserLoginId);
        auditTrail.setModifiedBy(caabUserLoginId);

        application.setAuditTrail(auditTrail);
        application.getCorrespondenceAddress().setAuditTrail(auditTrail);
        application.getCosts().setAuditTrail(auditTrail);

        applicationRepository.save(application);
    }
}
