package com.WOP.view.help;

import com.WOP.view.FXComponent;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class HelpPageSection implements FXComponent {
  String title;
  String text;

  public HelpPageSection(String title, String text) {
    this.title = title;
    this.text = text;
  }

  @Override
  public Parent render() {
    VBox sectionContainer = new VBox();
    sectionContainer.setStyle("-fx-spacing: 10;");

    Label sectionTitle = new Label(title);
    sectionTitle.setStyle(
        """
            -fx-font-size: 32;
            -fx-font-weight: 600;
            -fx-text-fill: black;""");
    sectionContainer.getChildren().add(sectionTitle);

    Text sectionText = new Text(text);
    sectionText.setStyle(
        """
            -fx-wrap-text: true;
            -fx-font-size: 18;
            -fx-text-fill: black;
            -fx-letter-spacing: 20px""");
    sectionContainer.getChildren().add(new TextFlow(sectionText));

    return sectionContainer;
  }
}
