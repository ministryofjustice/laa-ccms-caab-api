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

  public CostStructureDetailCopyable(final CostStructureDetail costStructureDetailToCopy) {
    super();
    this.setDefaultCostLimitation(costStructureDetailToCopy.getDefaultCostLimitation());
    this.setGrantedCostLimitation(costStructureDetailToCopy.getGrantedCostLimitation());
    this.setRequestedCostLimitation(costStructureDetailToCopy.getRequestedCostLimitation());
    this.setCostEntries(costStructureDetailToCopy.getCostEntries());
    this.setCurrentProviderBilledAmount(costStructureDetailToCopy.getCurrentProviderBilledAmount());
    this.setAuditTrail(costStructureDetailToCopy.getAuditTrail());
  }
}
