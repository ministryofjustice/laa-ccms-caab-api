package uk.gov.laa.ccms.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.api.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
}
