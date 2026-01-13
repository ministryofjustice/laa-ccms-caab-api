package uk.gov.laa.ccms.caab.api.model;

import uk.gov.laa.ccms.caab.model.CostStructureDetail;

/**
 * CostStructureDetail.
 */
public class CostStructureDetailCopyable extends CostStructureDetail {

  public CostStructureDetailCopyable() {
    super();
  }

  /**
   * Constructor to be used by the CostStructureDetail
   * itself when it wants to make a copy of itself.
   *
   */

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
