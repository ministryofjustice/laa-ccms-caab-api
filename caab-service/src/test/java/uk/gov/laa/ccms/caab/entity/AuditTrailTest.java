package uk.gov.laa.ccms.caab.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuditTrailTest {

    private AuditTrail auditTrail;

    @BeforeEach
    public void setUp() {
        auditTrail = new AuditTrail("modifiedByUser", "createdByUser");
    }

    @Test
    public void testAuditTrailConstructor() {
        assertEquals("modifiedByUser", auditTrail.getModifiedBy());
        assertEquals("createdByUser", auditTrail.getCreatedBy());
    }


}