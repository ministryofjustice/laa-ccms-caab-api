package uk.gov.laa.ccms.caab.api.model;

import uk.gov.laa.ccms.caab.model.CostStructureDetail;

public class CostStructureDetailCopyable extends CostStructureDetail {


  public CostStructureDetailCopyable() { super(); }

  private CostStructureDetailCopyable(final CostStructureDetail costEntryDetailToCopy) {
    super();
    this.setDefaultCostLimitation(costEntryDetailToCopy.getDefaultCostLimitation());
    this.setGrantedCostLimitation(costEntryDetailToCopy.getGrantedCostLimitation());
    this.setRequestedCostLimitation(costEntryDetailToCopy.getRequestedCostLimitation());
    this.setCostEntries(costEntryDetailToCopy.getCostEntries());
    this.setCurrentProviderBilledAmount(costEntryDetailToCopy.getCurrentProviderBilledAmount());
    this.setAuditTrail(costEntryDetailToCopy.getAuditTrail());
  }
}
