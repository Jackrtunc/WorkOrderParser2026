package com.WOP.view;

import com.WOP.controller.Controller;
import com.WOP.model.Model;
import com.WOP.view.help.HelpPage;
import com.WOP.view.home.AppBar;
import com.WOP.view.home.HomePage;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class WOPView implements View {
  private static Parent appBarRender = null;
  private static Parent helpPageRender = null;
  private final Model model;
  private final Controller controller;
  private final Stage stage;
  private final int sceneWidth;

  // Root component of the view
  public WOPView(Model model, Controller controller, Stage stage, int sceneWidth) {
    this.model = model;
    this.controller = controller;
    this.stage = stage;
    this.sceneWidth = sceneWidth;
    if (appBarRender == null) {
      // Only render static components once (big performance gains)
      appBarRender = new AppBar(controller).render();
    }
    if (helpPageRender == null) {
      helpPageRender = new HelpPage(model).render();
    }
  }

  @Override
  public Parent render() {
    BorderPane viewContainer = new BorderPane();

    viewContainer.setTop(appBarRender);

    switch (model.getActivePage()) {
      case HOME:
        viewContainer.setCenter(
            new HomePage(
                    model, controller, this::showSuccessDialogue, this::showErrorDialogue)
                .render());
        break;
      case HELP:
        viewContainer.setCenter(helpPageRender);
        break;
    }

    return viewContainer;
  }

  @Override
  public void update() {
    stage.getScene().setRoot(render());
  }

  // Callbacks for nested components to use to display dialogues
  @Override
  public void showErrorDialogue(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setHeaderText("Error");
    alert.setContentText(message);
    alert.getDialogPane().setPrefWidth(sceneWidth - 100);
    alert.showAndWait();
  }

  @Override
  public void showSuccessDialogue(String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle("Success!");
    alert.setHeaderText("Success!");
    alert.setContentText(message);
    alert.getDialogPane().setPrefWidth(sceneWidth - 100);
    alert.showAndWait();
  }
}
