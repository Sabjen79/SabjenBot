package me.sabjen.discordbot.ui.views;

import javafx.stage.Stage;
import me.sabjen.discordbot.ui.FXMLView;
import me.sabjen.discordbot.ui.controllers.DebugServerController;
import me.sabjen.discordbot.ui.controllers.ResourceAlertController;

public class DebugServerView extends FXMLView {
    public DebugServerView(Stage s) {
        super("ui/DebugServer.fxml", new DebugServerController(), s);
    }
}
