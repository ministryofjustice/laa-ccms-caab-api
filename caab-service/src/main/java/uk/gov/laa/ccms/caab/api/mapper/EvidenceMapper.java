package uk.gov.laa.ccms.caab.api.mapper;


import java.util.Base64;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetails;

/**
 * Interface responsible for mapping and transforming objects related
 * to the evidence domain. It bridges the gap between the data model
 * and the service or API layers, ensuring consistent object translation.
 */

@Mapper(componentModel = "spring",
    uses = CommonMapper.class)
public interface EvidenceMapper {

  EvidenceDocumentDetails toEvidenceDocumentDetails(
      final Page<uk.gov.laa.ccms.caab.api.entity.EvidenceDocument> evidenceDocuments);

  @Mapping(target = "documentType.id", source = "documentType")
  @Mapping(target = "documentType.displayValue", source = "documentTypeDisplayValue")
  @Mapping(target = "fileData", source = "fileBytes")
  EvidenceDocumentDetail toEvidenceDocumentDetail(
      final uk.gov.laa.ccms.caab.api.entity.EvidenceDocument evidenceDocument);

  @InheritInverseConfiguration
  @Mapping(target = "auditTrail", ignore = true)
  uk.gov.laa.ccms.caab.api.entity.EvidenceDocument toEvidenceDocument(
      final EvidenceDocumentDetail evidenceDocumentDetail);

  default String toBase64String(byte[] bytes) {
    return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
  }

  default byte[] toByteArrayFromBase64EncodedString(String base64EncodedString) {
    return Base64.getDecoder().decode(base64EncodedString);
  }

}
