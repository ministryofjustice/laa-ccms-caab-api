package uk.gov.laa.ccms.caab.api.mapper;

import java.util.Base64;

/**
 * A Mixin style interface which provides pre-defined file data mapping methods.
 */
public interface FileDataMapper {

  /**
   * Convert a byte array to a base64 encoded string.
   *
   * @param bytes of the file content.
   * @return a base64 encoded String of the file content.
   */
  default String toBase64String(byte[] bytes) {
    return bytes != null ? Base64.getEncoder().encodeToString(bytes) : null;
  }

  /**
   * Convert a base64 encoded string of binary data to a byte array.
   *
   * @param base64EncodedString of the file content
   * @return a byte array of the file content.
   */
  default byte[] toByteArrayFromBase64EncodedString(String base64EncodedString) {
    return base64EncodedString != null ? Base64.getDecoder().decode(base64EncodedString) : null;
  }

}
