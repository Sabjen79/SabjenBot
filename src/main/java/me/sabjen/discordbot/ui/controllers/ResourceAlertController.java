package me.sabjen.discordbot.ui.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.ui.FXMLController;
import me.sabjen.discordbot.ui.UI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ResourceAlertController extends FXMLController {

    public void redirectBrowser() throws URISyntaxException, IOException {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("https://discord.com/developers/applications"));
        }
    }

    public void onInputChanged() {
        var scene = getStage().getScene();
        var textField = (TextField) scene.lookup("#tokenField");
        var button = (Button) scene.lookup("#submitToken");

        button.setDisable(textField.getText().length() < 70);
    }

    public void onSubmit() {
        var scene = getStage().getScene();
        var textField = (TextField) scene.lookup("#tokenField");

        if(!Bot.checkToken(textField.getText())) {
            textField.setText("Incorrect Token...");
            return;
        }

        BotDatabase.getInstance().reset();
        BotDatabase.getInstance().getConfig().set("TOKEN", textField.getText());

        UI.startView();

        new Thread(() -> Bot.start(true)).start();
    }

    @Override
    public void initialize() {

    }
}
