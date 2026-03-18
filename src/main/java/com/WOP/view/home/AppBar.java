package com.WOP.view.home;

import com.WOP.controller.Controller;
import com.WOP.view.FXComponent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;

public class AppBar implements FXComponent {
  private final Controller controller;

  public AppBar(Controller controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    HBox appBar = new HBox();
    appBar.setStyle(
        """
      -fx-background-color: #0047AB;
      -fx-pref-height: 70;
      -fx-padding: 16 10 16 22;
      -fx-alignment: center;""");

    Label WOPTitle = new Label("Work Order Parser");
    WOPTitle.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #FFFFFF;
      -fx-font-size: 25;""");
    appBar.getChildren().add(WOPTitle);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    appBar.getChildren().add(spacer);

    HBox buttonContainer = new HBox();
    buttonContainer.setStyle(
        """
      -fx-alignment: center;""");
    appBar.getChildren().add(buttonContainer);

    Button homeButton = new Button();
    homeButton.setGraphic(new ImageView(new Image("homeImage.png", 42, 42, true, true)));
    homeButton.setStyle(
        """
      -fx-background-radius: 0;
      -fx-background-color: #0047AB;
      -fx-border-width: 1px;
      -fx-pref-height: 35px;
      -fx-pref-width: 35px;""");
    homeButton.setOnAction(_ -> controller.clickHome());
    buttonContainer.getChildren().add(homeButton);

    Button helpButton = new Button();
    helpButton.setGraphic(new ImageView(new Image("questionMarkImage.png", 42, 42, true, true)));
    helpButton.setStyle(
        """
      -fx-background-radius: 0;
      -fx-background-color: #0047AB;
      -fx-border-width: 1px;
      -fx-pref-height: 35px;
      -fx-pref-width: 35px;""");
    helpButton.setOnAction(_ -> controller.clickHelp());
    buttonContainer.getChildren().add(helpButton);

    return appBar;
  }
}
