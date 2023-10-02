package me.sabjen.discordbot.ui.views;

import javafx.stage.Stage;
import me.sabjen.discordbot.ui.FXMLController;
import me.sabjen.discordbot.ui.FXMLView;
import me.sabjen.discordbot.ui.controllers.StartController;

public class StartView extends FXMLView {
    public StartView(Stage s) {
        super("/ui/StartUI.fxml", new StartController(), s);
    }
}
