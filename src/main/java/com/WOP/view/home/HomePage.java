package com.WOP.view.home;

import com.WOP.controller.Controller;
import com.WOP.model.Model;
import java.util.function.Consumer;

import com.WOP.upload.Upload;
import com.WOP.view.FXComponent;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HomePage implements FXComponent {
  private final Model model;
  private final Controller controller;
  private final Consumer<String> showSuccessDialogue;
  private final Consumer<String> showErrorDialogue;

  public HomePage(Model model, Controller controller, Consumer<String> showSuccessDialogue, Consumer<String> showErrorDialogue) {
    this.model = model;
    this.controller = controller;
    this.showSuccessDialogue = showSuccessDialogue;
    this.showErrorDialogue = showErrorDialogue;
  }

  @Override
  public Parent render() {
    VBox homePage = new VBox();
    // I can't be bothered anymore to hunt for styles in a .css file
    homePage.setStyle("-fx-background-color: #F4F4F4;");

    FXComponent uploadArea = new UploadArea(model, controller, showErrorDialogue);
    homePage.getChildren().add(uploadArea.render());

    Separator topSeparator = new Separator(Orientation.HORIZONTAL);
    homePage.getChildren().add(topSeparator);

    ScrollPane scrollPane = new ScrollPane();
    scrollPane.setStyle("""
      -fx-padding: 15 0 0 0;
      -fx-background-color: transparent;""");
    homePage.getChildren().add(scrollPane);

    VBox uploadTray = new VBox();
    uploadTray.setStyle(
            """
          -fx-spacing: 18;
          -fx-padding: 10 10 25 25;""");

    for(Upload upload : model.getUploads()) {
      uploadTray.getChildren().add(new UploadCard(controller, upload).render());
    }
    scrollPane.setContent(uploadTray);

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    homePage.getChildren().add(spacer);

    Separator bottomSeparator = new Separator(Orientation.HORIZONTAL);
    homePage.getChildren().add(bottomSeparator);

    VBox runContainer = new VBox();
    runContainer.setStyle(
            """
          -fx-alignment: center;
          -fx-padding: 12 0 15 0;""");
    homePage.getChildren().add(runContainer);

    Button runButton = new Button("Run");
    runButton.setStyle(
            """
          -fx-font-family: Arial, sans-serif;
          -fx-background-radius: 3;
          -fx-background-color: #0047AB;
          -fx-text-fill: #FFFFFF;
          -fx-font-size: 20;
          -fx-padding: 8 30 8 30;""");
    runButton.setOnAction(
            (ActionEvent _) -> {
              try {
                showSuccessDialogue.accept(controller.execute());
              } catch (Exception ex) {
                showErrorDialogue.accept(ex.getMessage());
              }
            });
    runContainer.getChildren().add(runButton);

    return homePage;
  }
}
