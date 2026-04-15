package com.WOP.view.home;

import com.WOP.controller.Controller;
import com.WOP.upload.Upload;
import com.WOP.upload.UploadType;
import com.WOP.view.FXComponent;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class UploadCard implements FXComponent {
  private final Controller controller;
  private final Upload upload;

  public UploadCard(Controller controller, Upload upload) {
    this.controller = controller;
    this.upload = upload;
  }

  @Override
  public Parent render() {
    HBox uploadCard = new HBox();
    uploadCard.setStyle(
        """
      -fx-background-color: #F4F4F4;
      -fx-background-radius: 15;
      -fx-padding: 18;
      -fx-pref-height: 100;
      -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 25, 0, 0, 2);
      -fx-pref-width: 450;
      -fx-spacing: 50""");

    VBox uploadInfoContainer = new VBox();
    uploadInfoContainer.setStyle("-fx-spacing: 12;");
    uploadCard.getChildren().add(uploadInfoContainer);

    VBox nameContainer = new VBox();
    nameContainer.setStyle("-fx-spacing: 3");
    uploadInfoContainer.getChildren().add(nameContainer);

    Label uploadName = new Label(upload.getName());
    uploadName.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #000000;
      -fx-font-size: 20;""");
    nameContainer.getChildren().add(uploadName);

    Label selectCategory = new Label("Select category");
    selectCategory.setStyle(
        """
      -fx-font-family: Arial, sans-serif;
      -fx-text-fill: #5A5A5A;
      -fx-font-size: 18;""");
    nameContainer.getChildren().add(selectCategory);

    HBox buttonContainer = new HBox();
    buttonContainer.setStyle(
        """
      -fx-spacing: 5;""");
    uploadInfoContainer.getChildren().add(buttonContainer);

    String base =
        """
      -fx-font-family: Arial, sans-serif;
      -fx-background-color: transparent;
      -fx-border-width: 2;
      -fx-border-radius: 10;
      -fx-pref-width: 125;
      -fx-font-size: 15;""";

    String inactive =
        base
            + """
        -fx-text-fill: #5A5A5A;
        -fx-border-color: #5A5A5A;""";

    String active =
        base
            + """
        -fx-text-fill: #0047AB;
        -fx-border-color: #0047AB;
        -fx-font-weight: bold;""";

    Button departmentButton = new Button("Department");
    departmentButton.setStyle(upload.getUploadType() == UploadType.DEPARTMENT ? active : inactive);
    departmentButton.setOnAction(
        (ActionEvent _) ->
            controller.setUploadType(
                upload,
                upload.getUploadType() == UploadType.DEPARTMENT
                    ? UploadType.UNDEFINED // If department is currently selected, clicking should change it to undefined
                    : UploadType.DEPARTMENT)); // Vice versa
    buttonContainer.getChildren().add(departmentButton);

    Button facilitiesButton = new Button("Facilities");
    facilitiesButton.setStyle(upload.getUploadType() == UploadType.FACILITIES ? active : inactive);
    facilitiesButton.setOnAction(
        (ActionEvent _) ->
            controller.setUploadType(
                upload,
                upload.getUploadType() == UploadType.FACILITIES
                    ? UploadType.UNDEFINED
                    : UploadType.FACILITIES));
    buttonContainer.getChildren().add(facilitiesButton);

    Region spacer = new Region();
    HBox.setHgrow(spacer, Priority.ALWAYS);
    uploadCard.getChildren().add(spacer);

    FXComponent removeUploadButton = new CancelButton(12, () -> controller.removeUpload(upload));
    uploadCard.getChildren().add(removeUploadButton.render());

    return uploadCard;
  }
}
