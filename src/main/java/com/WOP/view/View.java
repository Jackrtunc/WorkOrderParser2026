package com.WOP.view;

public interface View extends FXComponent, ModelObserver {
  void showErrorDialogue(String message);

  void showSuccessDialogue(String message);
}
