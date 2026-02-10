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
  private Model model;
  private Controller controller;
  private final Consumer<String> showErrorDialogue;

  SaveButton(Model model, Controller controller, Consumer<String> showErrorDialogue) {
    this.model = model;
    this.controller = controller;
    this.showErrorDialogue = showErrorDialogue;
  }

  @Override
  public Parent render() {
    Button saveFileButton = new Button();
    saveFileButton.setStyle(
        """
        -fx-background-radius: 3;
        -fx-background-color: #0047AB;""");
    saveFileButton.setGraphic(new ImageView(new Image("folderImage1.png", 35, 35, true, true)));
    saveFileButton.setOnAction(
        (ActionEvent _) -> {
          DirectoryChooser directoryChooser = new DirectoryChooser();
          File selectedDirectory = directoryChooser.showDialog(new Stage());
          try {
            controller.selectOutputLocation(selectedDirectory);
          } catch (IOException ex) {
            showErrorDialogue.accept("Error opening directory");
          }
        });
    return saveFileButton;
  }
}
