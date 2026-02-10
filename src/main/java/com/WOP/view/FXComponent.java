package com.WOP.view;

import javafx.scene.Parent;

public interface FXComponent {
  // Triggered on a rerender of the view (from root component down)
  Parent render();
}
