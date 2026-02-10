package com.WOP.view.help;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Section(
    @JsonProperty(required = true) String title, @JsonProperty(required = true) String text) {}
