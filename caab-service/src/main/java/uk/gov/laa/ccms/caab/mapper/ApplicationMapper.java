package uk.gov.laa.ccms.caab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import uk.gov.laa.ccms.caab.entity.Address;
import uk.gov.laa.ccms.caab.entity.Application;
import uk.gov.laa.ccms.caab.entity.CostStructure;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetailCorrespondenceAddress;
import uk.gov.laa.ccms.caab.model.ApplicationDetailCosts;

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
    Application toApplication(ApplicationDetail applicationDetail);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auditTrail", ignore = true)
    @Mapping(target = "postCode", source = "postcode")
    @Mapping(target = "houseNameNumber", source = "houseNameOrNumber")
    Address toAddress(ApplicationDetailCorrespondenceAddress address);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "auditTrail", ignore = true)
    CostStructure toCostStructure(ApplicationDetailCosts costs);


}
