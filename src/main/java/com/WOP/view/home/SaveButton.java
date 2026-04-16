package com.WOP.view.home;

import com.WOP.controller.Controller;
import com.WOP.model.Model;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import com.WOP.view.FXComponent;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class SaveButton implements FXComponent {
  private final Controller controller;
  private final Consumer<String> showErrorDialogue;
  private static Image folderImage;

  SaveButton(Controller controller, int size, Consumer<String> showErrorDialogue) {
    this.controller = controller;
    this.showErrorDialogue = showErrorDialogue;
    if (folderImage == null) {
        folderImage = new Image("folderImage1.png", size, size, true, true);
    }
  }

  @Override
  public Parent render() {
    Button saveButton = new Button();
    saveButton.setStyle(
        """
        -fx-background-radius: 3;
        -fx-background-color: #0047AB;""");
    saveButton.setGraphic(new ImageView(folderImage));
    saveButton.setOnAction(
        (ActionEvent _) -> {
          DirectoryChooser directoryChooser = new DirectoryChooser();
          File selectedDirectory = directoryChooser.showDialog(new Stage());
          try {
            controller.selectOutputLocation(selectedDirectory);
          } catch (IOException ex) {
            showErrorDialogue.accept("Error opening directory");
          }
        });
    return saveButton;
  }
}
