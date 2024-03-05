package uk.gov.laa.ccms.caab.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.type.YesNoConverter;

/**
 * Class used to represent a question retrieved from the reference data. It is currently used by
 * both the NotificationRequest and the PriorAuthority.
 */
@Entity
@Table(name = "XXCCMS_REFERENCE_DATA_ITEM")
@SequenceGenerator(
    allocationSize = 1,
    sequenceName = "XXCCMS_GENERATED_ID_S",
    name = "XXCCMS_REFERENCE_DATA_ITEM_S")
@Setter
@Getter
@RequiredArgsConstructor
public class ReferenceDataItem implements Serializable {

  /**
   * unique number for serialization.
   */
  @Serial
  private static final long serialVersionUID = -3157803785583905514L;

  @Id
  @GeneratedValue(generator = "XXCCMS_REFERENCE_DATA_ITEM_S")
  private Long id;

  /**
   * unique code for an item.
   */
  @Column(name = "CODE", length = 50)
  private String code;

  /**
   * The question label, also known as the 'description' for the code.
   */
  @Column(name = "LABEL", length = 150)
  private String label;

  /**
   * The question type, can be FTS, FTL, INT, AMT, DAT, LOV.
   */
  @Column(name = "TYPE", length = 5)
  private String type;

  /**
   * If the question is mandatory or not, can be Y or N.
   */
  @Convert(converter = YesNoConverter.class)
  @Column(name = "MANDATORY", length = 5)
  private Boolean mandatory;

  /**
   * Corresponds to the LOV_TYPE in table XXCCMS_COMMON_LOV.
   */
  @Column(name = "LOV_LOOKUP", length = 200)
  private String lovLookUp;

  /**
   * The question response from the user.
   */
  @Column(name = "VALUE", length = 200)
  private String value;

  /**
   * The parent prior autority if applicable.
   */
  @ManyToOne
  @JoinColumn(name = "FK_PRIOR_AUTHORITY", nullable = false)
  private PriorAuthority priorAuthority;

  /**
   * If this item is a dropdown, this is the label for the dropdown value selected by the user.
   */
  @Column(name = "DISPLAY_VALUE", length = 200)
  private String displayValue;
}
