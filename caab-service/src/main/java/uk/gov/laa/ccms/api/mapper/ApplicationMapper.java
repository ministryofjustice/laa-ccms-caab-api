package uk.gov.laa.ccms.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import uk.gov.laa.ccms.api.entity.Address;
import uk.gov.laa.ccms.api.entity.Application;
import uk.gov.laa.ccms.api.entity.AuditTrail;
import uk.gov.laa.ccms.api.entity.CostStructure;
import uk.gov.laa.ccms.api.entity.Opponent;
import uk.gov.laa.ccms.api.entity.PriorAuthority;
import uk.gov.laa.ccms.api.entity.Proceeding;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetailCorrespondenceAddress;
import uk.gov.laa.ccms.caab.model.ApplicationDetailCosts;
import uk.gov.laa.ccms.caab.model.AuditDetail;
import uk.gov.laa.ccms.caab.model.OpponentDetail;
import uk.gov.laa.ccms.caab.model.PriorAuthorityDetail;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;

/**
 * Interface responsible for mapping and transforming objects related
 * to the application domain. It bridges the gap between the data model
 * and the service or API layers, ensuring consistent object translation.
 */
@Mapper(componentModel = "spring")
public interface ApplicationMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "lscCaseReference", source = "caseReferenceNumber")
  @Mapping(target = "providerId", source = "provider.id")
  @Mapping(target = "providerDisplayValue", source = "provider.displayValue")
  @Mapping(target = "providerCaseReference", source = "provider.caseReference")
  @Mapping(target = "officeId", source = "office.id")
  @Mapping(target = "officeDisplayValue", source = "office.displayValue")
  @Mapping(target = "supervisor", source = "supervisor.id")
  @Mapping(target = "supervisorDisplayValue", source = "supervisor.displayValue")
  @Mapping(target = "feeEarner", source = "feeEarner.id")
  @Mapping(target = "feeEarnerDisplayValue", source = "feeEarner.displayValue")
  @Mapping(target = "providerContact", source = "providerContact.id")
  @Mapping(target = "providerContactDisplayValue", source = "providerContact.displayValue")
  @Mapping(target = "categoryOfLaw", source = "categoryOfLaw.id")
  @Mapping(target = "categoryOfLawDisplayValue", source = "categoryOfLaw.displayValue")
  @Mapping(target = "displayStatus", source = "status.displayValue")
  @Mapping(target = "actualStatus", source = "status.id")
  @Mapping(target = "clientFirstName", source = "client.firstName")
  @Mapping(target = "clientSurname", source = "client.surname")
  @Mapping(target = "clientReference", source = "client.reference")
  @Mapping(target = "costLimitChanged", source = "costLimit.changed")
  @Mapping(target = "costLimitAtTimeOfMerits", source = "costLimit.atTimeOfMerits")
  @Mapping(target = "applicationType", source = "applicationType.id")
  @Mapping(target = "applicationTypeDisplayValue", source = "applicationType.displayValue")
  @Mapping(target = "devolvedPowersUsed", source = "devolvedPowers.used")
  @Mapping(target = "dateDevolvedPowersUsed", source = "devolvedPowers.dateUsed")
  @Mapping(target = "devolvedPowersContractFlag", source = "devolvedPowers.contractFlag")
  @Mapping(target = "meritsReassessmentReqdInd", source = "meritsReassessmentRequired")
  @Mapping(target = "leadProceedingChangedOpaInput", source = "leadProceedingChanged")
  @Mapping(target = "proceedings", ignore = true)
  @Mapping(target = "priorAuthorities", ignore = true)
  @Mapping(target = "opponents", ignore = true)
  Application toApplication(ApplicationDetail applicationDetail);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "postCode", source = "postcode")
  @Mapping(target = "houseNameNumber", source = "houseNameOrNumber")
  Address toAddress(ApplicationDetailCorrespondenceAddress address);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  CostStructure toCostStructure(ApplicationDetailCosts costs);

  @Mapping(target = "caseReferenceNumber", source = "lscCaseReference")
  @Mapping(target = "provider.id", source = "providerId")
  @Mapping(target = "provider.displayValue", source = "providerDisplayValue")
  @Mapping(target = "provider.caseReference", source = "providerCaseReference")
  @Mapping(target = "office.id", source = "officeId")
  @Mapping(target = "office.displayValue", source = "officeDisplayValue")
  @Mapping(target = "supervisor.id", source = "supervisor")
  @Mapping(target = "supervisor.displayValue", source = "supervisorDisplayValue")
  @Mapping(target = "feeEarner.id", source = "feeEarner")
  @Mapping(target = "feeEarner.displayValue", source = "feeEarnerDisplayValue")
  @Mapping(target = "providerContact.id", source = "providerContact")
  @Mapping(target = "providerContact.displayValue", source = "providerContactDisplayValue")
  @Mapping(target = "categoryOfLaw.id", source = "categoryOfLaw")
  @Mapping(target = "categoryOfLaw.displayValue", source = "categoryOfLawDisplayValue")
  @Mapping(target = "status.displayValue", source = "displayStatus")
  @Mapping(target = "status.id", source = "actualStatus")
  @Mapping(target = "client.firstName", source = "clientFirstName")
  @Mapping(target = "client.surname", source = "clientSurname")
  @Mapping(target = "client.reference", source = "clientReference")
  @Mapping(target = "costLimit.changed", source = "costLimitChanged")
  @Mapping(target = "costLimit.atTimeOfMerits", source = "costLimitAtTimeOfMerits")
  @Mapping(target = "applicationType.id", source = "applicationType")
  @Mapping(target = "applicationType.displayValue", source = "applicationTypeDisplayValue")
  @Mapping(target = "devolvedPowers.used", source = "devolvedPowersUsed")
  @Mapping(target = "devolvedPowers.dateUsed", source = "dateDevolvedPowersUsed")
  @Mapping(target = "devolvedPowers.contractFlag", source = "devolvedPowersContractFlag")
  @Mapping(target = "meritsReassessmentRequired", source = "meritsReassessmentReqdInd")
  @Mapping(target = "leadProceedingChanged", source = "leadProceedingChangedOpaInput")
  @Mapping(target = "auditTrail", source = "auditTrail",
      qualifiedByName = "toAuditDetail")
  ApplicationDetail toApplicationDetail(Application application);

  @Mapping(target = "postcode", source = "postCode")
  @Mapping(target = "houseNameOrNumber", source = "houseNameNumber")
  ApplicationDetailCorrespondenceAddress toApplicationDetailCorrespondenceAddress(Address address);

  @Mapping(target = "auditTrail", source = "auditTrail",
      qualifiedByName = "toAuditDetail")
  ApplicationDetailCosts toApplicationDetailCosts(CostStructure costs);

  @Mapping(target = "auditTrail", source = "auditTrail",
      qualifiedByName = "toAuditDetail")
  ProceedingDetail toProceedingDetail(Proceeding proceeding);

  @Mapping(target = "auditTrail", source = "auditTrail",
      qualifiedByName = "toAuditDetail")
  PriorAuthorityDetail toPriorAuthorityDetail(PriorAuthority priorAuthority);

  @Mapping(target = "auditTrail", source = "auditTrail",
      qualifiedByName = "toAuditDetail")
  OpponentDetail toOpponentDetail(Opponent opponent);

  @Mapping(target = "lastSaved", source = "modified")
  @Mapping(target = "lastSavedBy", source = "modifiedBy")
  @Named("toAuditDetail")
  AuditDetail toAuditDetail(AuditTrail auditTrail);

}
