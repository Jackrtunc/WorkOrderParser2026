package com.WOP.view.home;

import com.WOP.view.FXComponent;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class CancelButton implements FXComponent {
    private final Runnable onClick;
    private static ImageView xImage;

    CancelButton(int size, Runnable onClick) {
        this.onClick = onClick;
        if (xImage == null) {
            xImage = new ImageView(new Image("cancel.png", size, size, true, true));
        }
    }

    @Override
    public Parent render() {
        Button saveButton = new Button();
        saveButton.setStyle(
                """
                -fx-background-color: transparent;
                -fx-border-color: transparent;
                -fx-padding: 0;
                -fx-cursor: hand;
                """);
        saveButton.setGraphic(xImage);
        saveButton.setOnAction((ActionEvent _) -> onClick.run());
        return saveButton;
    }
}
