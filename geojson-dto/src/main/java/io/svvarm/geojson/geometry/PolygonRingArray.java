package io.svvarm.geojson.geometry;

import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.validation.constraints.Size;
import org.immutables.value.Value;

/**
 * An array of linear ring coordinate arrays. For polygons with more than one of these rings, the
 * first <em>must</em> be the exterior ring, and any others <em>must</em> be interior rings. The
 * exterior ring bounds the surface, and the interior rings (if present) bound holes within the
 * surface.
 *
 * @since 1.0
 */
@Value.Immutable
@JsonDeserialize(as = ImmutablePolygonRingArray.class)
public abstract class PolygonRingArray {

  /**
   * Returns the polygon rings.
   *
   * @return polygon rings
   */
  @JsonValue
  @Value.Parameter
  @Size(min = 1, message = "must have at least one ring")
  public abstract List<LinearRing> getRings();

  /**
   * Returns the exterior ring, which is the first ring.
   *
   * @return exterior/first ring
   */
  public LinearRing getExteriorRing() {
    return getRings().get(0);
  }

  /**
   * Returns the interior rings.
   *
   * @return interior rings
   */
  @Value.Lazy
  public List<LinearRing> getInteriorRings() {
    return getRings().subList(1, getRings().size());
  }
}
