package io.svvarm.geojson.validation;

/**
 * A runtime exception thrown during processing of GeoJSON objects.
 *
 * @since 1.0
 */
public final class GeoJsonException extends RuntimeException {

  /**
   * Constructs a new exception with the specified detail message.
   *
   * @param message the detail message
   */
  public GeoJsonException(final String message) {
    super(message);
  }
}
