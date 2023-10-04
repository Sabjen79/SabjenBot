package me.sabjen.discordbot.ui.views;

import javafx.stage.Stage;
import me.sabjen.discordbot.ui.FXMLController;
import me.sabjen.discordbot.ui.FXMLView;
import me.sabjen.discordbot.ui.controllers.ResourceAlertController;
import me.sabjen.discordbot.ui.controllers.StartController;

public class ResourceAlertView extends FXMLView {
    public ResourceAlertView(Stage s) {
        super("ui/ResourceAlert.fxml", new ResourceAlertController(), s);
    }
}
