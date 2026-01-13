package uk.gov.laa.ccms.caab.api.model;

import uk.gov.laa.ccms.caab.model.CostEntryDetail;

/**
 * CostEntryDetail.
 */
public class CostEntryDetailCopyable extends CostEntryDetail {

  public CostEntryDetailCopyable() {
    super();
  }

  /**
   * Constructor to be used by the CostEntryDetail
   * itself when it wants to make a copy of itself.
   *
   */
  private CostEntryDetailCopyable(final CostEntryDetail costEntryDetailToCopy) {
    super();
    this.setAmountBilled(costEntryDetailToCopy.getAmountBilled());
    this.setRequestedCosts(costEntryDetailToCopy.getRequestedCosts());
    this.setCostCategory(costEntryDetailToCopy.getCostCategory());
    this.setEbsId(costEntryDetailToCopy.getEbsId());
    this.setNewEntry(costEntryDetailToCopy.getNewEntry());
    this.setLscResourceId(costEntryDetailToCopy.getLscResourceId());
    this.setResourceName(costEntryDetailToCopy.getResourceName());
    this.setSubmitted(costEntryDetailToCopy.getSubmitted());
    this.setAuditTrail(costEntryDetailToCopy.getAuditTrail());
  }
}
