package com.WOP.workorder.config;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Header(
    @JsonProperty(required = true) String name, @JsonProperty(required = true) int offset) {}
