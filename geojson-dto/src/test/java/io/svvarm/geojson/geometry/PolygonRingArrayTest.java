package io.svvarm.geojson.geometry;

import static io.svvarm.geojson.GeoJsonTestHelper.assertInvalid;
import static io.svvarm.geojson.GeoJsonTestHelper.assertSerialization;
import static io.svvarm.geojson.GeoJsonTestHelper.assertValid;
import static io.svvarm.geojson.GeoJsonTestHelper.readJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

class PolygonRingArrayTest {

  private static final ImmutableLinearRing EXTERIOR_LINEAR_RING = ImmutableLinearRing.builder()
      .addPosition(0, 0)
      .addPosition(1, 1)
      .addPosition(2, 2)
      .addPosition(0, 0)
      .build();

  private static final ImmutableLinearRing INTERIOR_LINEAR_RING = ImmutableLinearRing.builder()
      .addPosition(0, 0)
      .addPosition(2, 2)
      .addPosition(1, 1)
      .addPosition(0, 0)
      .build();

  private static final ImmutablePolygonRingArray SIMPLE = ImmutablePolygonRingArray
      .builder()
      .addRing(EXTERIOR_LINEAR_RING)
      .build();

  private static final ImmutablePolygonRingArray COMPLEX = ImmutablePolygonRingArray.builder()
      .addRing(EXTERIOR_LINEAR_RING)
      .addRing(INTERIOR_LINEAR_RING)
      .build();

  @Test
  void serialize_withoutInteriorRings() {
    assertSerialization("[[[0,0],[1,1],[2,2],[0,0]]]", SIMPLE);
  }

  @Test
  void serialize_withInteriorRings() {
    assertSerialization("[[[0,0],[1,1],[2,2],[0,0]],[[0,0],[2,2],[1,1],[0,0]]]", COMPLEX);
  }

  @Test
  void deserialize_withoutInteriorRings() {
    assertThat(readJson("[[[0,0],[1,1],[2,2],[0,0]]]", PolygonRingArray.class), is(SIMPLE));
  }

  @Test
  void deserialize_withInteriorRings() {
    assertThat(
        readJson("[[[0,0],[1,1],[2,2],[0,0]],[[0,0],[2,2],[1,1],[0,0]]]", PolygonRingArray.class),
        is(COMPLEX));
  }

  @Test
  void deserialize_derivedClass() {
    assertThat(
        readJson("[[[0,0],[1,1],[2,2],[0,0]]]", ImmutablePolygonRingArray.class),
        is(SIMPLE));
  }

  @Test
  void validate_valid_withoutInteriorRings() {
    assertValid(SIMPLE);
  }

  @Test
  void validate_valid_withInteriorRings() {
    assertValid(COMPLEX);
  }

  @Test
  void validate_invalid() {
    assertInvalid("[]", PolygonRingArray.class);
  }

  @Test
  void getExteriorRing_withoutInteriorRings() {
    assertThat(SIMPLE.getExteriorRing(), is(EXTERIOR_LINEAR_RING));
  }

  @Test
  void getExteriorRing_withInteriorRings() {
    assertThat(COMPLEX.getExteriorRing(), is(EXTERIOR_LINEAR_RING));
  }

  @Test
  void getInteriorRing_withoutInteriorRings() {
    assertThat(SIMPLE.getInteriorRings(), empty());
  }

  @Test
  void getInteriorRing_withInteriorRings() {
    assertThat(COMPLEX.getInteriorRings(), contains(INTERIOR_LINEAR_RING));
  }
}
