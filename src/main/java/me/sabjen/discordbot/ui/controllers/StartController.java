package me.sabjen.discordbot.ui.controllers;

import javafx.scene.control.ChoiceBox;
import me.sabjen.discordbot.Bot;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.ui.FXMLController;
import me.sabjen.discordbot.ui.UI;

public class StartController extends FXMLController {

    public void normalMode() {
        UI.mainView();

        new Thread(() -> {
            Bot.start(false);
        }).start();
    }

    public void debugMode() {

        var id = BotDatabase.getInstance().getConfig().get("DEBUG_SERVER");
        if(id.equals("")) {
            UI.debugServerView();

            return;
        }

        UI.mainView();

        new Thread(() -> {
            Bot.start(true);
        }).start();
    }

    @Override
    public void initialize() {

    }
}
