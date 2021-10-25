package io.svvarm.geojson.geometry;

import static io.svvarm.geojson.GeoJsonTestHelper.assertInvalid;
import static io.svvarm.geojson.GeoJsonTestHelper.assertSerialization;
import static io.svvarm.geojson.GeoJsonTestHelper.assertValid;
import static io.svvarm.geojson.GeoJsonTestHelper.readJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class LinearRingTest {

  private static final ImmutableLinearRing LINEAR_RING = ImmutableLinearRing.builder()
      .addPosition(0, 0)
      .addPosition(1, 1)
      .addPosition(2, 2)
      .addPosition(0, 0)
      .build();

  @Test
  void serialize() {
    assertSerialization("[[0,0],[1,1],[2,2],[0,0]]", LINEAR_RING);
  }

  @Test
  void deserialize() {
    assertThat(readJson("[[0,0],[1,1],[2,2],[0,0]]", LinearRing.class), is(LINEAR_RING));
  }

  @Test
  void deserialize_derivedClass() {
    assertThat(
        readJson("[[0,0],[1,1],[2,2],[0,0]]", ImmutableLinearRing.class),
        is(LINEAR_RING));
  }

  @Test
  void validate_valid() {
    assertValid(LINEAR_RING);
  }

  @ParameterizedTest
  @ValueSource(strings = {
      "[]",
      "[[0,0],[1,1],[2,2],[3,3]]",
      "[[0,0],[1,1],[0,0]]"
  })
  void validate_invalid(final String json) {
    assertInvalid(json, LinearRing.class);
  }
}
