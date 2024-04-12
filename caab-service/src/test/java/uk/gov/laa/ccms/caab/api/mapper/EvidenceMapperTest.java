package uk.gov.laa.ccms.caab.api.mapper;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildEvidenceDocument;
import static uk.gov.laa.ccms.caab.api.util.ModelUtils.buildEvidenceDocumentDetail;

import java.text.ParseException;
import java.util.Base64;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import uk.gov.laa.ccms.caab.api.entity.EvidenceDocument;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetails;

@ExtendWith(MockitoExtension.class)
public class EvidenceMapperTest {
    @InjectMocks
    private final EvidenceMapper mapper = new EvidenceMapperImpl();

    @BeforeEach
    public void setup() throws ParseException {
    }

    @Test
    public void testToEvidenceDocumentDetails_null() {
        EvidenceDocumentDetails evidenceDocumentDetails = mapper.toEvidenceDocumentDetails(null);
        assertNull(evidenceDocumentDetails);
    }

    @Test
    public void testToEvidenceDocumentDetails() {
        Page<EvidenceDocument> evidenceDocuments = new PageImpl<>(List.of(new EvidenceDocument()));

        EvidenceDocumentDetails result = mapper.toEvidenceDocumentDetails(evidenceDocuments);

        assertNotNull(result);
        assertNotNull(result.getContent());
        assertEquals(1, result.getContent().size());
    }

    @Test
    public void testToEvidenceDocumentDetail() {
        // Construct EvidenceDocument
        EvidenceDocument evidenceDocument = buildEvidenceDocument();

        // Convert EvidenceDocument to EvidenceDocumentDetail
        EvidenceDocumentDetail result = mapper.toEvidenceDocumentDetail(evidenceDocument);

        assertNotNull(result);
        assertEquals(evidenceDocument.getApplicationOrOutcomeId(), result.getApplicationOrOutcomeId());
        assertNotNull(result.getAuditTrail());
        assertEquals(evidenceDocument.getCaseReferenceNumber(), result.getCaseReferenceNumber());
        assertEquals(evidenceDocument.getCcmsModule(), result.getCcmsModule());
        assertEquals(evidenceDocument.getDescription(), result.getDescription());
        assertEquals(evidenceDocument.getDocumentSender(), result.getDocumentSender());
        assertEquals(evidenceDocument.getDocumentType(), result.getDocumentType().getId());
        assertEquals(evidenceDocument.getDocumentTypeDisplayValue(), result.getDocumentType().getDisplayValue());
        assertEquals(evidenceDocument.getEvidenceDescriptions(), result.getEvidenceDescriptions());
        assertEquals(Base64.getEncoder().encodeToString(evidenceDocument.getFileBytes()), result.getFileData());
        assertEquals(evidenceDocument.getFileExtension(), result.getFileExtension());
        assertEquals(evidenceDocument.getFileName(), result.getFileName());
        assertEquals(evidenceDocument.getId().intValue(), result.getId());
        assertEquals(evidenceDocument.getNotificationReference(), result.getNotificationReference());
        assertEquals(evidenceDocument.getProviderId(), result.getProviderId());
        assertEquals(evidenceDocument.getRegisteredDocumentId(), result.getRegisteredDocumentId());
        assertEquals(evidenceDocument.getTransferResponseCode(), result.getTransferResponseCode());
        assertEquals(evidenceDocument.getTransferResponseDescription(), result.getTransferResponseDescription());
        assertEquals(evidenceDocument.getTransferRetryCount(), result.getTransferRetryCount());
        assertEquals(evidenceDocument.getTransferStatus(), result.getTransferStatus());
    }

    @Test
    public void testToEvidenceDocument() {
        // Construct EvidenceDocumentDetail
        EvidenceDocumentDetail evidenceDocumentDetail = buildEvidenceDocumentDetail();

        // Convert EvidenceDocumentDetail to EvidenceDocument
        EvidenceDocument result = mapper.toEvidenceDocument(evidenceDocumentDetail);

        assertNotNull(result);
        assertEquals(evidenceDocumentDetail.getApplicationOrOutcomeId(), result.getApplicationOrOutcomeId());
        assertNotNull(result.getAuditTrail());
        assertEquals(evidenceDocumentDetail.getCaseReferenceNumber(), result.getCaseReferenceNumber());
        assertEquals(evidenceDocumentDetail.getCcmsModule(), result.getCcmsModule());
        assertEquals(evidenceDocumentDetail.getDescription(), result.getDescription());
        assertEquals(evidenceDocumentDetail.getDocumentSender(), result.getDocumentSender());
        assertEquals(evidenceDocumentDetail.getDocumentType().getId(), result.getDocumentType());
        assertEquals(evidenceDocumentDetail.getDocumentType().getDisplayValue(), result.getDocumentTypeDisplayValue());
        assertEquals(evidenceDocumentDetail.getEvidenceDescriptions(), result.getEvidenceDescriptions());
        assertArrayEquals(Base64.getDecoder().decode(evidenceDocumentDetail.getFileData()), result.getFileBytes());
        assertEquals(evidenceDocumentDetail.getFileExtension(), result.getFileExtension());
        assertEquals(evidenceDocumentDetail.getFileName(), result.getFileName());
        assertEquals(evidenceDocumentDetail.getId().intValue(), result.getId());
        assertEquals(evidenceDocumentDetail.getNotificationReference(), result.getNotificationReference());
        assertEquals(evidenceDocumentDetail.getProviderId(), result.getProviderId());
        assertEquals(evidenceDocumentDetail.getRegisteredDocumentId(), result.getRegisteredDocumentId());
        assertEquals(evidenceDocumentDetail.getTransferResponseCode(), result.getTransferResponseCode());
        assertEquals(evidenceDocumentDetail.getTransferResponseDescription(), result.getTransferResponseDescription());
        assertEquals(evidenceDocumentDetail.getTransferRetryCount(), result.getTransferRetryCount());
        assertEquals(evidenceDocumentDetail.getTransferStatus(), result.getTransferStatus());
    }

}
