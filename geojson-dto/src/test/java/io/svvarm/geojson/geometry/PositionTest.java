package io.svvarm.geojson.geometry;

import static io.svvarm.geojson.GeoJsonTestHelper.assertInvalid;
import static io.svvarm.geojson.GeoJsonTestHelper.assertSerialization;
import static io.svvarm.geojson.GeoJsonTestHelper.assertThrowsGeoJsonException;
import static io.svvarm.geojson.GeoJsonTestHelper.assertValid;
import static io.svvarm.geojson.GeoJsonTestHelper.readJson;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PositionTest {

  private static final ImmutablePosition SIMPLE = ImmutablePosition.of(1, 0);
  private static final ImmutablePosition COMPLEX = SIMPLE.withAdditionalElements(10, 20);

  @Test
  void serialize_withoutAdditionalElements() {
    assertSerialization("[1, 0]", SIMPLE);
  }

  @Test
  void serialize_withAdditionalElements() {
    assertSerialization("[1, 0, 10, 20]", COMPLEX);
  }

  @Test
  void deserialize_withoutAdditionalElements() {
    assertThat(readJson("[1, 0]", Position.class), is(SIMPLE));
  }

  @Test
  void deserialize_withAdditionalElements() {
    assertThat(readJson("[1, 0, 10, 20]", Position.class), is(COMPLEX));
  }

  @Test
  void deserialize_derivedClass() {
    assertThat(readJson("[1, 0]", ImmutablePosition.class), is(SIMPLE));
  }

  @ParameterizedTest
  @ValueSource(strings = {"[]", "[1]"})
  void deserialize_invalidSize(final String json) {
    assertThat(assertThrowsGeoJsonException(json, Position.class),
        is("Position must have at least 2 elements"));
  }

  @Test
  void validate_valid_withoutAdditionalElements() {
    assertValid(SIMPLE);
  }

  @Test
  void validate_valid_withAdditionalElements() {
    assertValid(COMPLEX);
  }

  @ParameterizedTest
  @ValueSource(doubles = {-91.0, 91.0})
  void validate_invalidLatitude(final double latitude) {
    assertInvalid(SIMPLE.withLatitude(latitude));
  }

  @ParameterizedTest
  @ValueSource(doubles = {-181.0, 181.0})
  void validate_invalidLongitude(final double longitude) {
    assertInvalid(SIMPLE.withLongitude(longitude));
  }
}
