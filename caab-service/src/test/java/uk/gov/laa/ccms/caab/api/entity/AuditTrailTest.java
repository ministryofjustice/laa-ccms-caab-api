package uk.gov.laa.ccms.caab.api.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.gov.laa.ccms.caab.api.entity.AuditTrail;

import static org.junit.jupiter.api.Assertions.*;

class AuditTrailTest {

    private AuditTrail auditTrail;

    @BeforeEach
    public void setUp() {
        auditTrail = new AuditTrail();
    }

    @Test
    public void testAuditTrailConstructor() {
        assertNull(auditTrail.getLastSavedBy());
        assertNull(auditTrail.getCreatedBy());
    }


}