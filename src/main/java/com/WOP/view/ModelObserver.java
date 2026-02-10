package com.WOP.view;

public interface ModelObserver {
  // The model calls update() when the view needs to be rerendered
  void update();
}
