package me.sabjen.discordbot.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public abstract class FXMLView {
    private final String path;
    private final FXMLController controller;
    private Stage stage;
    private Scene scene;

    public FXMLView(String p, FXMLController c, Stage s) {
        stage = s;
        controller = c;
        path = p;

        try {
            var loader = new FXMLLoader(getClass().getClassLoader().getResource(path));
            loader.setController(controller);
            controller.setStage(stage);

            scene = loader.load();
            scene.getStylesheets().add("https://fonts.googleapis.com/css2?family=Roboto&display=swap");
            scene.setFill(Color.TRANSPARENT);

            stage.setScene(scene);
            stage.show();
            stage.setAlwaysOnTop(true);
            stage.setAlwaysOnTop(false);

        } catch (Exception e) {
            e.printStackTrace();
        }

        controller.initialize();
    }
}
