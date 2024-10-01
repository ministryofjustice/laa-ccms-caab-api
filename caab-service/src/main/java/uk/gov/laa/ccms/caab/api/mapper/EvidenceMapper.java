package uk.gov.laa.ccms.caab.api.mapper;


import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.BaseEvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetail;
import uk.gov.laa.ccms.caab.model.EvidenceDocumentDetails;

/**
 * Interface responsible for mapping and transforming objects related
 * to the evidence domain. It bridges the gap between the data model
 * and the service or API layers, ensuring consistent object translation.
 */

@Mapper(componentModel = "spring",
    uses = CommonMapper.class)
public interface EvidenceMapper extends FileDataMapper {

  EvidenceDocumentDetails toEvidenceDocumentDetails(
      final Page<uk.gov.laa.ccms.caab.api.entity.EvidenceDocument> evidenceDocuments);

  @Mapping(target = "documentType.id", source = "documentType")
  @Mapping(target = "documentType.displayValue", source = "documentTypeDisplayValue")
  BaseEvidenceDocumentDetail toBaseEvidenceDocumentDetail(
      final uk.gov.laa.ccms.caab.api.entity.EvidenceDocument evidenceDocument);

  @Mapping(target = "documentType.id", source = "documentType")
  @Mapping(target = "documentType.displayValue", source = "documentTypeDisplayValue")
  @Mapping(target = "fileData", source = "fileBytes")
  EvidenceDocumentDetail toEvidenceDocumentDetail(
      final uk.gov.laa.ccms.caab.api.entity.EvidenceDocument evidenceDocument);

  @InheritInverseConfiguration
  @Mapping(target = "auditTrail", ignore = true)
  uk.gov.laa.ccms.caab.api.entity.EvidenceDocument toEvidenceDocument(
      final EvidenceDocumentDetail evidenceDocumentDetail);

  @InheritConfiguration(name = "toEvidenceDocument")
  @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
  void mapIntoEvidence(
      @MappingTarget uk.gov.laa.ccms.caab.api.entity.EvidenceDocument evidence,
      EvidenceDocumentDetail evidenceDocumentDetail);

}
