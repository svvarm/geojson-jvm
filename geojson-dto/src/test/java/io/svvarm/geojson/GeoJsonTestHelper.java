package io.svvarm.geojson;

import static javax.validation.Validation.buildDefaultValidatorFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import io.svvarm.geojson.validation.GeoJsonException;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.skyscreamer.jsonassert.JSONAssert;

/**
 * Utility class to help test GeoJSON classes.
 */
public final class GeoJsonTestHelper {

  private GeoJsonTestHelper() {
  }

  // load all modules on the classpath
  public static final JsonMapper JSON_MAPPER = JsonMapper.builder().findAndAddModules().build();

  public static final Validator VALIDATOR = buildDefaultValidatorFactory().getValidator();

  /**
   * Asserts that the expected JSON is strictly equal to the serialized POJO.
   *
   * @param expectedJson the expected JSON
   * @param pojo         the POJO to serialize
   * @throws AssertionError if not strictly equal
   */
  public static void assertSerialization(final String expectedJson, final Object pojo) {
    try {
      JSONAssert.assertEquals(expectedJson, JSON_MAPPER.writeValueAsString(pojo), true);
    } catch (final JsonProcessingException | JSONException e) {
      Assertions.fail(e);
    }
  }

  /**
   * Asserts that deserialization throws {@link GeoJsonException}.
   *
   * @param json  the JSON to deserialize
   * @param klass the POJO class
   * @return message of the {@link GeoJsonException} instance
   * @throws AssertionError if {@link GeoJsonException} is not thrown
   */
  public static String assertThrowsGeoJsonException(final String json, final Class<?> klass) {

    final Throwable t = Assertions.assertThrows(Throwable.class, () -> readJson(json, klass));
    final GeoJsonException e = ExceptionUtils.throwableOfThrowable(t, GeoJsonException.class);
    MatcherAssert.assertThat("GeoJsonException is not thrown", e, Matchers.notNullValue());
    return e.getMessage();
  }

  /**
   * Deserializes and maps the JSON to the given class.
   *
   * @param json  the JSON to deserialize
   * @param klass the POJO class
   * @param <T>   the POJO type
   * @return POJO instance
   * @throws AssertionError if unable to deserialize
   */
  public static <T> T readJson(final String json, final Class<T> klass) {
    try {
      return JSON_MAPPER.readValue(json, klass);
    } catch (final JsonProcessingException e) {
      return Assertions.fail(e);
    }
  }

  /**
   * Asserts that no constraint violations are found.
   *
   * @param object the object to validate
   * @throws AssertionError if unexpected constraint violations found
   */
  public static void assertValid(final Object object) {
    MatcherAssert.assertThat(
        "Unexpected constraint violations found",
        VALIDATOR.validate(object), Matchers.empty());
  }

  /**
   * Asserts that some constraint violations are found.
   *
   * @param object the object to validate
   * @return set of violations
   * @throws AssertionError if no constraint violations found
   */
  public static <T> Set<ConstraintViolation<T>> assertInvalid(final T object) {
    final Set<ConstraintViolation<T>> violations = VALIDATOR.validate(object);
    MatcherAssert.assertThat(
        "No constraint violations found",
        violations, Matchers.not(Matchers.empty()));
    return violations;
  }

  /**
   * Asserts that some constraint violations are found in the deserialized JSON.
   *
   * @param json  the JSON to deserialize
   * @param klass the POJO class
   * @param <T>   the POJO type
   * @return set of violations
   * @throws AssertionError if unable to deserialize or no constraint violations found
   */
  public static <T> Set<ConstraintViolation<T>> assertInvalid(
      final String json,
      final Class<T> klass) {
    return assertInvalid(readJson(json, klass));
  }
}
