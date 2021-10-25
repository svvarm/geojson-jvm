package io.svvarm.geojson.geometry;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.svvarm.geojson.validation.LinearRingConstraint;
import java.util.List;
import org.immutables.value.Value;

/**
 * A linear ring is a closed array with four or more positions. The first and last positions are
 * equivalent, and they <em>must</em> contain identical values; their representation <em>should</em>
 * also be identical. A linear ring is the boundary of a surface or the boundary of a hole in a
 * surface. A linear ring <em>must</em> follow the right-hand rule with respect to the area it
 * bounds, i.e., exterior rings are counterclockwise, and holes are clockwise.
 *
 * @since 1.0
 */
@Value.Immutable
@JsonDeserialize(as = ImmutableLinearRing.class)
public abstract class LinearRing {

  /**
   * Returns the positions.
   *
   * @return positions
   */
  @JsonValue
  @Value.Parameter
  @LinearRingConstraint
  public abstract List<Position> getPositions();
}
