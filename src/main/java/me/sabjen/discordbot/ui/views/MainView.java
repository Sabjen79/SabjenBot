package me.sabjen.discordbot.ui.views;

import javafx.stage.Stage;
import me.sabjen.discordbot.ui.FXMLController;
import me.sabjen.discordbot.ui.FXMLView;
import me.sabjen.discordbot.ui.controllers.MainController;
import me.sabjen.discordbot.ui.controllers.ResourceAlertController;

public class MainView extends FXMLView {
    public MainView(Stage s) {
        super("/ui/MainUI.fxml", new MainController(), s);
    }
}
