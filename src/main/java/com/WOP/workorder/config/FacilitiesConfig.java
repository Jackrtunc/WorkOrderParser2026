package com.WOP.workorder.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record FacilitiesConfig(
    @JsonProperty(required = true) Header referenceNumber,
    @JsonProperty(required = true) Header workOrderNumber,
    @JsonProperty(required = true) Header dateRequestEntered,
    @JsonProperty(required = true) Header workOrderStatus,
    @JsonProperty(required = true) Header building,
    @JsonProperty(required = true) Header description,
    @JsonProperty(required = true) Header contactEmailAddress)
    implements SpreadsheetConfig {
  public String[] getHeaderNames() {
    return new String[] {
      referenceNumber.name(),
      workOrderNumber.name(),
      dateRequestEntered.name(),
      workOrderStatus.name(),
      building.name(),
      description.name(),
      contactEmailAddress.name()
    };
  }
}
