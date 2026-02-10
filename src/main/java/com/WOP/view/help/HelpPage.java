package com.WOP.view.help;

import com.WOP.model.Model;
import com.WOP.view.FXComponent;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class HelpPage implements FXComponent {
  private final Model model;

  public HelpPage(Model model) {
    this.model = model;
  }

  @Override
  public Parent render() {
    VBox helpPageContainer = new VBox();
    helpPageContainer.setStyle(
        """
      -fx-padding: 20;
      -fx-spacing: 30;""");

    ScrollPane scrollable = new ScrollPane();
    scrollable.setContent(helpPageContainer);
    helpPageContainer.maxWidthProperty().bind(scrollable.widthProperty().subtract(30));

    // Help page sections loaded from src/main/resources/config.json
    for (Section section : model.getHelpPageConfig().getSections()) {
      helpPageContainer
          .getChildren()
          .add(new HelpPageSection(section.title(), section.text()).render());
    }

    return scrollable;
  }
}
