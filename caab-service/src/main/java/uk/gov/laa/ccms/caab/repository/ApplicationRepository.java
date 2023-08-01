package uk.gov.laa.ccms.caab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uk.gov.laa.ccms.caab.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    
}
