package com.WOP.view.home;

import com.WOP.controller.Controller;
import com.WOP.model.Model;
import com.WOP.model.ModelException;
import com.WOP.view.FXComponent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class UploadArea implements FXComponent {
  private final Model model;
  private final Controller controller;
  private final Consumer<String> showErrorDialogue;
  private static Parent saveButtonRender = null;

  UploadArea(Model model, Controller controller, Consumer<String> showErrorDialogue) {
    this.model = model;
    this.controller = controller;
    this.showErrorDialogue = showErrorDialogue;
    if (saveButtonRender == null) {
      saveButtonRender = new SaveButton(controller, 29, showErrorDialogue).render();
    }
  }

  @Override
  public Parent render() {
    VBox uploadArea = new VBox();
    uploadArea.setStyle(
        """
      -fx-padding: 22 25 22 25;
      -fx-spacing: 15;
      -fx-background-color: #F4F4F4;""");

    VBox instructionsContainer = new VBox();
    instructionsContainer.setStyle("-fx-spacing: 5;");
    uploadArea.getChildren().add(instructionsContainer);

    Label instructionsTitle = new Label("Upload files");
    instructionsTitle.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #000000;
      -fx-font-size: 24;""");
    instructionsContainer.getChildren().add(instructionsTitle);

    StringBuilder supportedFileTypes = new StringBuilder("Supported types:");
    for (String fileT : model.getSupportedFileTypes()) {
      supportedFileTypes.append(" ").append(fileT);
    }
    Label instructionsSubtitle = new Label(supportedFileTypes.toString());
    instructionsSubtitle.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #5A5A5A;
      -fx-font-size: 18;""");
    instructionsContainer.getChildren().add(instructionsSubtitle);

    HBox buttonContainer = new HBox();
    buttonContainer.setStyle(
        """
      -fx-spacing: 11;
      -fx-alignment: center-left;""");
    uploadArea.getChildren().add(buttonContainer);

    String buttonStyle =
        """
          -fx-font-family: Arial, sans-serif;
          -fx-background-radius: 3;
          -fx-background-color: #0047AB;
          -fx-text-fill: #FFFFFF;
          -fx-font-size: 18;
          -fx-padding: 7 12 7 12;""";

    Button uploadButton = new Button("Upload");
    uploadButton.setStyle(buttonStyle);
    uploadButton.setOnAction(
        (ActionEvent _) -> {
          FileChooser fileChooser = new FileChooser();
          List<File> files = fileChooser.showOpenMultipleDialog(new Stage());
          try {
            for (File file : files) {
              controller.addUpload(file);
            }
          } catch (IOException | ModelException ex) {
            showErrorDialogue.accept(ex.getMessage());
          }
        });
    buttonContainer.getChildren().add(uploadButton);

    Button clearButton = new Button("Clear");
    clearButton.setStyle(buttonStyle);
    clearButton.setOnAction((ActionEvent _) -> controller.clearUploads());
    buttonContainer.getChildren().add(clearButton);

    buttonContainer.getChildren().add(saveButtonRender);

    Label saveLocation =
        new Label(
            model.getOutputDirPath() != null ? model.getOutputDirPath() : "Select save location");
    saveLocation.setStyle(
        """
              -fx-font-family: Arial, sans-serif;
              -fx-text-fill: #5A5A5A;
              -fx-font-size: 18;""");
    buttonContainer.getChildren().add(saveLocation);

    return uploadArea;
  }
}
