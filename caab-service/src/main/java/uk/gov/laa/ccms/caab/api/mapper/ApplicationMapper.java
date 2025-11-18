package uk.gov.laa.ccms.caab.api.mapper;


import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.InheritConfiguration;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.data.domain.Page;
import uk.gov.laa.ccms.caab.api.entity.Address;
import uk.gov.laa.ccms.caab.api.entity.Application;
import uk.gov.laa.ccms.caab.api.entity.CostEntry;
import uk.gov.laa.ccms.caab.api.entity.CostStructure;
import uk.gov.laa.ccms.caab.api.entity.LinkedCase;
import uk.gov.laa.ccms.caab.api.entity.Opponent;
import uk.gov.laa.ccms.caab.api.entity.PriorAuthority;
import uk.gov.laa.ccms.caab.api.entity.Proceeding;
import uk.gov.laa.ccms.caab.api.entity.ReferenceDataItem;
import uk.gov.laa.ccms.caab.api.entity.ScopeLimitation;
import uk.gov.laa.ccms.caab.model.AddressDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetail;
import uk.gov.laa.ccms.caab.model.ApplicationDetails;
import uk.gov.laa.ccms.caab.model.ApplicationProviderDetails;
import uk.gov.laa.ccms.caab.model.ApplicationType;
import uk.gov.laa.ccms.caab.model.BaseApplicationDetail;
import uk.gov.laa.ccms.caab.model.CostEntryDetail;
import uk.gov.laa.ccms.caab.model.CostStructureDetail;
import uk.gov.laa.ccms.caab.model.LinkedCaseDetail;
import uk.gov.laa.ccms.caab.model.OpponentDetail;
import uk.gov.laa.ccms.caab.model.PriorAuthorityDetail;
import uk.gov.laa.ccms.caab.model.ProceedingDetail;
import uk.gov.laa.ccms.caab.model.ReferenceDataItemDetail;
import uk.gov.laa.ccms.caab.model.ScopeLimitationDetail;

/**
 * Interface responsible for mapping and transforming objects related
 * to the application domain. It bridges the gap between the data model
 * and the service or API layers, ensuring consistent object translation.
 */

@Mapper(componentModel = "spring",
    config = IgnoreUnmappedMapperConfig.class,
    uses = CommonMapper.class)
public interface ApplicationMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "lscCaseReference", source = "caseReferenceNumber")
  @Mapping(target = "providerId", source = "providerDetails.provider.id")
  @Mapping(target = "providerDisplayValue", source = "providerDetails.provider.displayValue")
  @Mapping(target = "officeId", source = "providerDetails.office.id")
  @Mapping(target = "officeDisplayValue", source = "providerDetails.office.displayValue")
  @Mapping(target = "supervisor", source = "providerDetails.supervisor.id")
  @Mapping(target = "supervisorDisplayValue", source = "providerDetails.supervisor.displayValue")
  @Mapping(target = "feeEarner", source = "providerDetails.feeEarner.id")
  @Mapping(target = "feeEarnerDisplayValue", source = "providerDetails.feeEarner.displayValue")
  @Mapping(target = "providerContact", source = "providerDetails.providerContact.id")
  @Mapping(target = "providerContactDisplayValue",
      source = "providerDetails.providerContact.displayValue")
  @Mapping(target = "providerCaseReference", source = "providerDetails.providerCaseReference")
  @Mapping(target = "categoryOfLaw", source = "categoryOfLaw.id")
  @Mapping(target = "categoryOfLawDisplayValue", source = "categoryOfLaw.displayValue")
  @Mapping(target = "displayStatus", source = "status.displayValue")
  @Mapping(target = "actualStatus", source = "status.id")
  @Mapping(target = "clientFirstName", source = "client.firstName")
  @Mapping(target = "clientSurname", source = "client.surname")
  @Mapping(target = "clientReference", source = "client.reference")
  @Mapping(target = "costLimitChanged", source = "costLimit.changed")
  @Mapping(target = "costLimitAtTimeOfMerits", source = "costLimit.limitAtTimeOfMerits")
  @Mapping(target = "applicationType", source = "applicationType.id")
  @Mapping(target = "applicationTypeDisplayValue", source = "applicationType.displayValue")
  @Mapping(target = "devolvedPowersUsed", source = "applicationType.devolvedPowers.used")
  @Mapping(target = "dateDevolvedPowersUsed",
      source = "applicationType.devolvedPowers.dateUsed")
  @Mapping(target = "devolvedPowersContractFlag",
      source = "applicationType.devolvedPowers.contractFlag")
  @Mapping(target = "meritsReassessmentReqdInd", source = "meritsReassessmentRequired")
  @Mapping(target = "leadProceedingChangedOpaInput", source = "leadProceedingChanged")
  @Mapping(target = "meansAssessmentAmended", source = "meansAssessmentAmended")
  @Mapping(target = "meritsAssessmentAmended", source = "meritsAssessmentAmended")
  @Mapping(target = "amendment", source = "amendment")
  @Mapping(target = "costs", source = "costs", qualifiedByName = "toCostStructure")
  @Mapping(target = "correspondenceAddress", source = "correspondenceAddress",
      qualifiedByName = "toAddress")
  Application toApplication(ApplicationDetail applicationDetail);

  @InheritConfiguration(name = "toApplication")
  @Mapping(target = "opponents", ignore = true)
  @Mapping(target = "linkedCases", ignore = true)
  @Mapping(target = "proceedings", ignore = true)
  @Mapping(target = "priorAuthorities", ignore = true)
  @BeanMapping(nullValuePropertyMappingStrategy =  NullValuePropertyMappingStrategy.IGNORE)
  void mapIntoApplication(
      @MappingTarget Application application,
      ApplicationDetail applicationDetail);

  /**
   * After mapping, set the parent entity in the child entities.
   *
   * @param application the application entity containing parent and child entities.
   */
  default void setParentInChildEntities(@MappingTarget Application application) {
    updateCostEntryIds(application.getCosts());

    if (application.getProceedings() != null) {
      application.getProceedings().forEach(proceeding -> {
        proceeding.setApplication(application);
        if (proceeding.getScopeLimitations() != null) {
          proceeding.getScopeLimitations().forEach(
              scopeLimitation -> scopeLimitation.setProceeding(proceeding));
        }
      });
    }

    if (application.getPriorAuthorities() != null) {
      application.getPriorAuthorities().forEach(priorAuthority -> {
        priorAuthority.setApplication(application);
        if (priorAuthority.getItems() != null) {
          priorAuthority.getItems().forEach(item -> item.setPriorAuthority(priorAuthority));
        }
      });
    }

    if (application.getOpponents() != null) {
      application.getOpponents().forEach(opponent -> opponent.setApplication(application));
    }

    if (application.getLinkedCases() != null) {
      application.getLinkedCases().forEach(linkedCase -> linkedCase.setApplication(application));
    }
  }

  private void updateCostEntryIds(CostStructure costs) {
    if (costs != null) {
      if (costs.getCostEntries() != null) {
        costs.getCostEntries().forEach(
            costEntry -> costEntry.setCostStructure(costs));
      }
    }
  }

  @InheritInverseConfiguration(name = "toApplication")
  @Mapping(target = "id")
  @Mapping(target = "auditTrail")
  @Mapping(target = "costs", source = "costs", qualifiedByName = "toCostStructureModel")
  @Mapping(target = "correspondenceAddress", source = "correspondenceAddress",
      qualifiedByName = "toAddressModel")
  ApplicationDetail toApplicationDetail(Application application);

  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "matterType", source = "matterType.id")
  @Mapping(target = "matterTypeDisplayValue", source = "matterType.displayValue")
  @Mapping(target = "proceedingType", source = "proceedingType.id")
  @Mapping(target = "proceedingTypeDisplayValue", source = "proceedingType.displayValue")
  @Mapping(target = "levelOfService", source = "levelOfService.id")
  @Mapping(target = "levelOfServiceDisplayValue", source = "levelOfService.displayValue")
  @Mapping(target = "clientInvolvement", source = "clientInvolvement.id")
  @Mapping(target = "clientInvolvementDisplayValue", source = "clientInvolvement.displayValue")
  @Mapping(target = "status", source = "status.id")
  @Mapping(target = "displayStatus", source = "status.displayValue")
  @Mapping(target = "typeOfOrder", source = "typeOfOrder.id")
  @Mapping(target = "leadProceedingInd", source = "leadProceedingInd")
  Proceeding toProceeding(ProceedingDetail proceeding);

  @InheritInverseConfiguration(name = "toProceeding")
  @Mapping(target = "auditTrail")
  ProceedingDetail toProceedingModel(Proceeding proceeding);

  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "type", source = "type.id")
  @Mapping(target = "typeDisplayValue", source = "type.displayValue")
  PriorAuthority toPriorAuthority(PriorAuthorityDetail priorAuthority);

  @InheritInverseConfiguration(name = "toPriorAuthority")
  @Mapping(target = "auditTrail")
  PriorAuthorityDetail toPriorAuthorityModel(PriorAuthority priorAuthority);

  @Mapping(target = "code", source = "code.id")
  @Mapping(target = "label", source = "code.displayValue")
  @Mapping(target = "value", source = "value.id")
  @Mapping(target = "displayValue", source = "value.displayValue")
  ReferenceDataItem toReferenceDataItem(ReferenceDataItemDetail referenceDataItem);

  @InheritInverseConfiguration
  ReferenceDataItemDetail toReferenceDataItemModel(ReferenceDataItem referenceDataItem);

  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "scopeLimitation", source = "scopeLimitation.id")
  @Mapping(target = "scopeLimitationDisplayValue", source = "scopeLimitation.displayValue")
  @Mapping(target = "delegatedFuncApplyInd", source = "delegatedFuncApplyInd.flag",
      defaultValue = "false")
  ScopeLimitation toScopeLimitation(ScopeLimitationDetail scopeLimitation);

  @InheritInverseConfiguration(name = "toScopeLimitation")
  @Mapping(target = "auditTrail")
  ScopeLimitationDetail toScopeLimitationModel(
      ScopeLimitation scopeLimitation);

  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "address", source = "address", qualifiedByName = "toAddress")
  @Mapping(target = "appMode", source = "appMode", defaultValue = "true")
  @Mapping(target = "amendment", source = "amendment")
  @Mapping(target = "award", source = "award")
  @Mapping(target = "sharedInd", source = "sharedInd")
  @Mapping(target = "deleteInd", source = "deleteInd", defaultValue = "true")
  Opponent toOpponent(OpponentDetail opponent);

  @InheritInverseConfiguration(name = "toOpponent")
  @Mapping(target = "auditTrail")
  @Mapping(target = "address", source = "address", qualifiedByName = "toAddressModel")
  OpponentDetail toOpponentModel(Opponent opponent);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "address", source = "address", qualifiedByName = "toAddress")
  void updateOpponent(@MappingTarget Opponent opponent, OpponentDetail opponentModel);

  List<LinkedCase> toLinkedCaseList(List<LinkedCaseDetail> linkedCases);

  List<LinkedCaseDetail> toLinkedCaseModelList(List<LinkedCase> linkedCases);

  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "clientSurname", source = "client.surname")
  @Mapping(target = "clientReference", source = "client.reference")
  @Mapping(target = "clientFirstName", source = "client.firstName")
  LinkedCase toLinkedCase(LinkedCaseDetail linkedCase);

  @InheritInverseConfiguration(name = "toLinkedCase")
  @Mapping(target = "auditTrail")
  LinkedCaseDetail toLinkedCaseModel(LinkedCase linkedCase);

  @Named("toAddress")
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "postCode", source = "postcode")
  @Mapping(target = "houseNameNumber", source = "houseNameOrNumber")
  Address toAddress(AddressDetail address);

  @Named("toAddressModel")
  @InheritInverseConfiguration(name = "toAddress")
  @Mapping(target = "id")
  @Mapping(target = "auditTrail")
  AddressDetail toAddressModel(Address address);

  @Named("toCostStructure")
  @Mapping(target = "auditTrail", ignore = true)
  CostStructure toCostStructure(CostStructureDetail costs);

  @Named("toCostStructureModel")
  @InheritInverseConfiguration(name = "toCostStructure")
  @Mapping(target = "auditTrail")
  CostStructureDetail toCostStructureModel(CostStructure costs);

  @Mapping(target = "newEntry", source = "newEntry", defaultValue = "true")
  @Mapping(target = "auditTrail", ignore = true)
  CostEntry toCostEntry(CostEntryDetail costEntry);

  @InheritInverseConfiguration
  @Mapping(target = "auditTrail")
  CostEntryDetail toCostEntryModel(CostEntry costEntry);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "applicationType", source = "applicationType.id")
  @Mapping(target = "applicationTypeDisplayValue", source = "applicationType.displayValue")
  @Mapping(target = "devolvedPowersUsed", source = "applicationType.devolvedPowers.used")
  @Mapping(target = "dateDevolvedPowersUsed",
      source = "applicationType.devolvedPowers.dateUsed")
  @Mapping(target = "devolvedPowersContractFlag",
      source = "applicationType.devolvedPowers.contractFlag")
  void addApplicationType(@MappingTarget Application application,
                          ApplicationType applicationType);

  @Mapping(target = "id", source = "applicationType")
  @Mapping(target = "displayValue", source = "applicationTypeDisplayValue")
  @Mapping(target = "devolvedPowers.used", source = "devolvedPowersUsed")
  @Mapping(target = "devolvedPowers.dateUsed",
      source = "dateDevolvedPowersUsed")
  @Mapping(target = "devolvedPowers.contractFlag",
      source = "devolvedPowersContractFlag")
  ApplicationType toApplicationType(Application application);

  @Mapping(target = "provider.id", source = "providerId")
  @Mapping(target = "provider.displayValue", source = "providerDisplayValue")
  @Mapping(target = "office.id", source = "officeId")
  @Mapping(target = "office.displayValue", source = "officeDisplayValue")
  @Mapping(target = "supervisor.id", source = "supervisor")
  @Mapping(target = "supervisor.displayValue", source = "supervisorDisplayValue")
  @Mapping(target = "feeEarner.id", source = "feeEarner")
  @Mapping(target = "feeEarner.displayValue", source = "feeEarnerDisplayValue")
  @Mapping(target = "providerContact.id", source = "providerContact")
  @Mapping(target = "providerContact.displayValue", source = "providerContactDisplayValue")
  ApplicationProviderDetails toProviderDetails(Application application);

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "providerId", source = "providerDetails.provider.id")
  @Mapping(target = "providerDisplayValue", source = "providerDetails.provider.displayValue")
  @Mapping(target = "officeId", source = "providerDetails.office.id")
  @Mapping(target = "officeDisplayValue", source = "providerDetails.office.displayValue")
  @Mapping(target = "supervisor", source = "providerDetails.supervisor.id")
  @Mapping(target = "supervisorDisplayValue", source = "providerDetails.supervisor.displayValue")
  @Mapping(target = "feeEarner", source = "providerDetails.feeEarner.id")
  @Mapping(target = "feeEarnerDisplayValue", source = "providerDetails.feeEarner.displayValue")
  @Mapping(target = "providerContact", source = "providerDetails.providerContact.id")
  @Mapping(target = "providerContactDisplayValue",
      source = "providerDetails.providerContact.displayValue")
  void addProviderDetails(@MappingTarget Application application,
                          ApplicationProviderDetails providerDetails);


  /**
   * Adds/Updates an application with a new correspondence address.
   */
  default void addCorrespondenceAddressToApplication(
      @MappingTarget Application application, AddressDetail addressModel) {
    if (application.getCorrespondenceAddress() == null) {
      application.setCorrespondenceAddress(new Address());
    }
    updateAddress(application.getCorrespondenceAddress(), addressModel);
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "postCode", source = "postcode")
  @Mapping(target = "houseNameNumber", source = "houseNameOrNumber")
  void updateAddress(@MappingTarget Address address, AddressDetail addressModel);

  /**
   * Adds/Updates an application with a new cost structure.
   */
  default void addCostStructureToApplication(
      @MappingTarget Application application, CostStructureDetail costStructureModel) {
    if (application.getCosts() == null) {
      application.setCosts(new CostStructure());
    }
    updateCostStructure(application.getCosts(), costStructureModel);
    updateCostEntryIds(application.getCosts());
  }

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "auditTrail", ignore = true)
  void updateCostStructure(
      @MappingTarget CostStructure costStructure, CostStructureDetail costStructureModel);

  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "clientReference", source = "client.reference")
  @Mapping(target = "clientSurname", source = "client.surname")
  @Mapping(target = "clientFirstName", source = "client.firstName")
  void updateLinkedCase(
      @MappingTarget LinkedCase linkedCase, LinkedCaseDetail linkedCaseModel);

  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "type", source = "type.id")
  @Mapping(target = "typeDisplayValue", source = "type.displayValue")
  void updatePriorAuthority(
      @MappingTarget PriorAuthority priorAuthority, PriorAuthorityDetail priorAuthorityModel);

  @Mapping(target = "id", ignore = true)
  @InheritConfiguration(name = "toProceeding")
  void updateProceeding(
      @MappingTarget Proceeding proceeding, ProceedingDetail proceedingModel);

  @Mapping(target = "auditTrail", ignore = true)
  @Mapping(target = "id", ignore = true)
  @Mapping(target = "scopeLimitation", source = "scopeLimitation.id")
  @Mapping(target = "scopeLimitationDisplayValue", source = "scopeLimitation.displayValue")
  @Mapping(target = "delegatedFuncApplyInd", source = "delegatedFuncApplyInd.flag")
  void updateScopeLimitation(
      @MappingTarget ScopeLimitation scopeLimitation, ScopeLimitationDetail scopeLimitationModel);

  @Mapping(target = "providerDetails", source = ".")
  @Mapping(target = "caseReferenceNumber", source = "lscCaseReference")
  @Mapping(target = "categoryOfLaw.id", source = "categoryOfLaw")
  @Mapping(target = "categoryOfLaw.displayValue", source = "categoryOfLawDisplayValue")
  @Mapping(target = "status.id", source = "actualStatus")
  @Mapping(target = "status.displayValue", source = "displayStatus")
  @Mapping(target = "client.firstName", source = "clientFirstName")
  @Mapping(target = "client.surname", source = "clientSurname")
  @Mapping(target = "client.reference", source = "clientReference")
  BaseApplicationDetail toBaseApplication(Application application);

  ApplicationDetails toApplicationDetails(Page<Application> application);

}
