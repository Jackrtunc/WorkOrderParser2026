package com.WOP.model;

public class ModelException extends RuntimeException {

  // Exceptions bubble up to the view where they are shown as alerts
  public ModelException(String message) {
    super(message);
  }
}
