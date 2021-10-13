package io.svvarm.geojson;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import java.util.Map;

public interface GeoJson {
  // TODO

  @JsonAnyGetter
  Map<String, Object> getForeignMembers();
}
