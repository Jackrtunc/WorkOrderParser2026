package com.WOP.view.help;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HelpPageConfig(
    @JsonProperty(required = true) Section github,
    @JsonProperty(required = true) Section usage,
    @JsonProperty(required = true) Section debugging) {

  public Section[] getSections() {
    return new Section[] { usage, debugging, github };
  }
}
