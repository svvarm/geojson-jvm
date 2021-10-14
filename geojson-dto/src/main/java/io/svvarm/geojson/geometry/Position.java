package io.svvarm.geojson.geometry;

import static java.util.Collections.unmodifiableList;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.svvarm.geojson.validation.GeoJsonException;
import io.svvarm.geojson.validation.ValidLatitude;
import io.svvarm.geojson.validation.ValidLongitude;
import java.util.ArrayList;
import java.util.List;
import org.immutables.value.Value;

/**
 * A position is an array of numbers. There <em>must</em> be two or more elements. The first two
 * elements are longitude and latitude, or easting and northing, precisely in that order and using
 * decimal numbers. Altitude or elevation <em>may</em> be included as an optional third element. The
 * interpretation and meaning of additional elements is beyond the scope of this implementation.
 *
 * @since 1.0
 */
@Value.Immutable
@Value.Style(depluralize = true, jacksonIntegration = false)
public interface Position {

  /**
   * Returns the longitude.
   *
   * @return longitude
   */
  @ValidLongitude
  @Value.Parameter(order = 1)
  Number getLongitude();

  /**
   * Returns the latitude.
   *
   * @return latitude
   */
  @ValidLatitude
  @Value.Parameter(order = 2)
  Number getLatitude();

  /**
   * Returns optional additional elements, such as altitude.
   *
   * @return optional additional elements
   */
  List<Number> getAdditionalElements();

  /**
   * Converts the position to a flattened list of elements.
   *
   * @return list of elements
   */
  @JsonValue
  @Value.Lazy
  default List<Number> asList() {
    final List<Number> additional = getAdditionalElements();

    final List<Number> result = new ArrayList<>(2 + additional.size());
    result.add(getLongitude());
    result.add(getLatitude());
    result.addAll(additional);

    return unmodifiableList(result);
  }

  /**
   * Parses a list of elements to a position.
   *
   * @param elements the list of elements
   * @return immutable position
   */
  @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
  static ImmutablePosition parsePosition(final List<? extends Number> elements) {
    final int size = elements.size();
    switch (size) {
      case 0:
      case 1:
        throw new GeoJsonException("Position must have at least 2 elements");
      case 2:
        return ImmutablePosition.of(elements.get(0), elements.get(1));
      default:
        return ImmutablePosition.builder()
            .longitude(elements.get(0))
            .latitude(elements.get(1))
            .addAllAdditionalElements(elements.subList(2, size))
            .build();
    }
  }
}
