package com.WOP.workorder.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DepartmentConfig(
    @JsonProperty(required = true) Header referenceNumber,
    @JsonProperty(required = true) Header workOrderNumber,
    @JsonProperty(required = true) Header reportingParty,
    @JsonProperty(required = true) Header dateRequested,
    @JsonProperty(required = true) Header status,
    @JsonProperty(required = true) Header building,
    @JsonProperty(required = true) Header location,
    @JsonProperty(required = true) Header description,
    @JsonProperty(required = true) Header notes)
    implements SpreadsheetConfig {
  public String[] getHeaderNames() {
    return new String[] {
      referenceNumber.name(),
      workOrderNumber.name(),
      reportingParty.name(),
      dateRequested.name(),
      status.name(),
      building.name(),
      location.name(),
      description.name(),
      notes.name()
    };
  }
}
