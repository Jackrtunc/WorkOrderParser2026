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
      -fx-alignment: center;""");

    Label WOPTitle = new Label("Work Order Parser 2.0");
    WOPTitle.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #FFFFFF;
      -fx-font-size: 25;
      -fx-padding: 15;
      -fx-alignment: center-left;""");
    appBar.getChildren().add(WOPTitle);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    appBar.getChildren().add(spacer);

    Button homeButton = new Button();
    Image home = new Image("homeImage.png", 40, 40, true, true);
    ImageView homeImage = new ImageView(home);
    homeButton.setGraphic(homeImage);
    homeButton.setStyle(
        """
      -fx-background-radius: 0;
      -fx-background-color: #0047AB;
      -fx-border-width: 1px;
      -fx-padding: 11 18 11 0;
      -fx-pref-height: 35px;
      -fx-pref-width: 35px;""");
    homeButton.setOnAction(_ -> controller.clickHome());
    appBar.getChildren().add(homeButton);

    Button helpButton = new Button();
    Image help = new Image("questionMarkImage.png", 40, 40, true, true);
    ImageView helpImage = new ImageView(help);
    helpButton.setGraphic(helpImage);
    helpButton.setStyle(
        """
      -fx-background-radius: 0;
      -fx-background-color: #0047AB;
      -fx-border-width: 1px;
      -fx-padding: 11 18 11 0;
      -fx-pref-height: 35px;
      -fx-pref-width: 35px;""");
    helpButton.setOnAction(_ -> controller.clickHelp());
    appBar.getChildren().add(helpButton);

    return appBar;
  }
}
