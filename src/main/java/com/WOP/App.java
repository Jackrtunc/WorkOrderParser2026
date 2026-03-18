package com.WOP;

import com.WOP.controller.Controller;
import com.WOP.controller.WOPController;
import com.WOP.fileconverters.CSVConverter;
import com.WOP.fileconverters.XLSXConverter;
import com.WOP.model.Model;
import com.WOP.model.Page;
import com.WOP.model.WOP;
import com.WOP.view.View;
import com.WOP.view.WOPView;
import com.WOP.workorder.storage.WorkOrderTree;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
  @Override
  public void start(Stage stage) throws IOException {
    Model model =
        new WOP(
            Page.HOME, // Default page
            new ArrayList<>(), // Stores uploads
            new WorkOrderTree(), // Stores work orders
            20, // Max number of facilities uploads allowed
            1, // Max number of department uploads allowed
            new ArrayList<>(), // Stores observers
            Map.of(
                "csv",
                new CSVConverter(),
                "xlsx",
                new XLSXConverter())); // Supported file extensions and converters (to .csv) for
                                       // them
    Controller controller = new WOPController(model);
    View view = new WOPView(model, controller, stage, 700);
    model.addObserver(view);

    Scene window = new Scene(view.render(), 700, 700);
    stage.setScene(window);
    stage.show();
  }
}
