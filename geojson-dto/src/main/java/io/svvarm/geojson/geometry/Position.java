package io.svvarm.geojson.geometry;

import static java.util.Collections.unmodifiableList;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;
import io.svvarm.geojson.geometry.Position.Parser;
import io.svvarm.geojson.validation.GeoJsonException;
import io.svvarm.geojson.validation.LatitudeConstraint;
import io.svvarm.geojson.validation.LongitudeConstraint;
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
@JsonDeserialize(converter = Parser.class)
public abstract class Position {

  /**
   * Returns the longitude.
   *
   * @return longitude
   */
  @LongitudeConstraint
  @Value.Parameter(order = 1)
  public abstract Number getLongitude();

  /**
   * Returns the latitude.
   *
   * @return latitude
   */
  @LatitudeConstraint
  @Value.Parameter(order = 2)
  public abstract Number getLatitude();

  /**
   * Returns optional additional elements, such as altitude.
   *
   * @return optional additional elements
   */
  public abstract List<Number> getAdditionalElements();

  /**
   * Converts the position to a flattened list of elements.
   *
   * @return list of elements
   */
  @JsonValue
  @Value.Lazy
  public List<Number> asList() {
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
  public static ImmutablePosition parsePosition(final List<? extends Number> elements) {
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

  /**
   * Internal parser class to work with Jackson. A converter is instead of the {@code JsonCreator}
   * annotation so that deserialization can work with the abstract and generated class.
   */
  static final class Parser extends StdConverter<List<? extends Number>, ImmutablePosition> {

    @Override
    public ImmutablePosition convert(List<? extends Number> value) {
      return Position.parsePosition(value);
    }
  }
}
