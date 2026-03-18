package com.WOP.view.home;

import com.WOP.controller.Controller;
import com.WOP.model.Model;
import com.WOP.model.ModelException;
import com.WOP.view.FXComponent;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
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
      saveButtonRender = new SaveButton(model, controller, showErrorDialogue).render();
    }
  }

  @Override
  public Parent render() {
    VBox uploadArea = new VBox();
    uploadArea.setStyle(
        """
      -fx-padding: 25;
      -fx-spacing: 20;
      -fx-background-color: #F4F4F4;""");

    HBox uploadBox = new HBox();
    uploadBox.setStyle(
        """
      -fx-spacing: 20;
      -fx-alignment: center-left;""");
    uploadArea.getChildren().add(uploadBox);

    VBox uploadInstructions = new VBox();
    uploadInstructions.setStyle("-fx-spacing: 5;");
    uploadBox.getChildren().add(uploadInstructions);

    Label instructionsTitle = new Label("Upload files");
    instructionsTitle.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #000000;
      -fx-font-size: 22;""");
    uploadInstructions.getChildren().add(instructionsTitle);

    StringBuilder supportedFileTypes = new StringBuilder("Supported types:");
    for (String fileT : model.getSupportedFileTypes()) {
      supportedFileTypes.append(" ").append(fileT);
    }
    Label instructionsSubtitle = new Label(supportedFileTypes.toString());
    instructionsSubtitle.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #5A5A5A;
      -fx-font-size: 16;""");
    uploadInstructions.getChildren().add(instructionsSubtitle);

    HBox uploadClearContainer = new HBox();
    uploadClearContainer.setStyle(
"""
-fx-spacing: 15;
-fx-alignment: center-left;""");
    uploadArea.getChildren().add(uploadClearContainer);

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
              File file = fileChooser.showOpenDialog(new Stage());
              try {
                controller.addUpload(file);
              } catch (IOException | ModelException ex) {
                showErrorDialogue.accept(ex.getMessage());
              }
            });
    uploadClearContainer.getChildren().add(uploadButton);

    Button clearButton = new Button("Clear");
    clearButton.setStyle(buttonStyle);
    clearButton.setOnAction((ActionEvent _) -> controller.clearUploads());
    uploadClearContainer.getChildren().add(clearButton);

//    Region spacer = new Region();
//    HBox.setHgrow(spacer, Priority.ALWAYS);
//    uploadClearContainer.getChildren().add(spacer);

    HBox saveArea = new HBox();
    saveArea.setStyle(
        """
      -fx-spacing: 14;
      -fx-alignment: center-left;""");
    uploadClearContainer.getChildren().add(saveArea);

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

    return uploadArea;
  }
}

// HBox saveArea = new HBox();
//    saveArea.setStyle(
//        """
//      -fx-spacing: 14;
//      -fx-alignment: center-left;""");
//    uploadArea.getChildren().add(saveArea);
//
//    saveArea.getChildren().add(saveButtonRender);
//
// Label saveLocation =
//        new Label(
//                model.getOutputDirPath() != null ? model.getOutputDirPath() : "Select save
// location");
//    saveLocation.setStyle(
//        """
//      -fx-font-family: Arial, sans-serif;
//      -fx-text-fill: #5A5A5A;
//      -fx-font-size: 20;""");
//    saveArea.getChildren().add(saveLocation);
//
// Region spacer2 = new Region();
//    HBox.setHgrow(spacer2, Priority.ALWAYS);
//    saveArea.getChildren().add(spacer2);
