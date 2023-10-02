package me.sabjen.discordbot.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.sabjen.discordbot.Bot;

public abstract class FXMLController {
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public abstract void initialize();

    //========================================================//

    private double x = 0, y = 0;

    public void topBarPress(MouseEvent mouseEvent) {
        x = mouseEvent.getSceneX();
        y = mouseEvent.getSceneY();
    }

    public void topBarDrag(MouseEvent mouseEvent) {
        getStage().setX(mouseEvent.getScreenX() - x);
        getStage().setY(mouseEvent.getScreenY() - y);
    }

    public void closeApp(MouseEvent mouseEvent) {
        if(Bot.getJDA() != null) Bot.getJDA().shutdown();

        System.exit(0);
    }
    public void minimizeApp(MouseEvent mouseEvent) {
        getStage().setIconified(true);
    }
}
