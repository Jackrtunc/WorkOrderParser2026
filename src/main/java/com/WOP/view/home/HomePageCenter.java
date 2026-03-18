package com.WOP.view.home;

import com.WOP.controller.Controller;
import com.WOP.model.Model;
import java.util.function.Consumer;

import com.WOP.upload.Upload;
import com.WOP.view.FXComponent;
import javafx.geometry.Orientation;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class HomePageCenter implements FXComponent {
  private final Model model;
  private final Controller controller;
  private final Consumer<String> showErrorDialogue;

  public HomePageCenter(Model model, Controller controller, Consumer<String> showErrorDialogue) {
    this.model = model;
    this.controller = controller;
    this.showErrorDialogue = showErrorDialogue;
  }

  @Override
  public Parent render() {
    VBox homePageCenter = new VBox();
    // I can't be bothered anymore to hunt for styles in a .css file
    homePageCenter.setStyle("-fx-background-color: #F4F4F4;");

    FXComponent uploadArea = new UploadArea(model, controller, showErrorDialogue);
    homePageCenter.getChildren().add(uploadArea.render());

    Separator separator = new Separator(Orientation.HORIZONTAL);
    homePageCenter.getChildren().add(separator);

    ScrollPane scrollable = new ScrollPane();
    scrollable.setStyle("""
      -fx-padding: 15 0 0 0;
      -fx-background-color: transparent;""");
    homePageCenter.getChildren().add(scrollable);

    VBox uploadList = new VBox();
    uploadList.setStyle(
            """
          -fx-spacing: 18;
          -fx-padding: 10 10 25 25;""");

    for(Upload upload : model.getUploads()) {
      uploadList.getChildren().add(new UploadCard(controller, upload).render());
    }
    scrollable.setContent(uploadList);

    Region spacer = new Region();
    VBox.setVgrow(spacer, Priority.ALWAYS);
    homePageCenter.getChildren().add(spacer);

    Separator separator2 = new Separator(Orientation.HORIZONTAL);
    homePageCenter.getChildren().add(separator2);

    return homePageCenter;
  }
}
