package com.WOP.view.home;

import com.WOP.controller.Controller;
import com.WOP.model.Model;
import java.util.function.Consumer;

import com.WOP.view.FXComponent;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class SaveArea implements FXComponent {
  private static Parent saveButtonRender = null;
  private final Model model;
  private final Controller controller;
  private final Consumer<String> showErrorDialogue;
  private final Consumer<String> showSuccessDialogue;

  public SaveArea(
          Model model,
          Controller controller,
          Consumer<String> showErrorDialogue,
          Consumer<String> showSuccessDialogue) {
    this.model = model;
    this.controller = controller;
    this.showErrorDialogue = showErrorDialogue;
    this.showSuccessDialogue = showSuccessDialogue;
    if (saveButtonRender == null) {
      saveButtonRender = new SaveButton(model, controller, showErrorDialogue).render();
    }
  }

  @Override
  public Parent render() {
    VBox separatorContainer = new VBox();

    Separator separator = new Separator(Orientation.HORIZONTAL);
    separatorContainer.getChildren().add(separator);

    HBox saveArea = new HBox();
    saveArea.setStyle(
        """
      -fx-padding: 25;
      -fx-spacing: 14;
      -fx-alignment: center-left;""");
    separatorContainer.getChildren().add(saveArea);

    saveArea.getChildren().add(saveButtonRender);

    Label saveLocation =
        new Label(
            model.getOutputDirPath() != null ? model.getOutputDirPath() : "Select save location");
    saveLocation.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #5A5A5A;
      -fx-font-size: 20;""");
    saveArea.getChildren().add(saveLocation);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    saveArea.getChildren().add(spacer);

    Button runButton = new Button("Run");
    runButton.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-background-radius: 3;
      -fx-background-color: #0047AB;
      -fx-text-fill: #FFFFFF;
      -fx-font-size: 20;
      -fx-padding: 7 11 7 11;""");
    runButton.setOnAction(
        (ActionEvent _) -> {
          try {
            showSuccessDialogue.accept(controller.execute());
          } catch (Exception ex) {
            showErrorDialogue.accept(ex.getMessage());
          }
        });
    saveArea.getChildren().add(runButton);

    return separatorContainer;
  }
}
