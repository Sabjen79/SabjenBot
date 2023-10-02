package me.sabjen.discordbot.ui.controllers.util;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.timers.timer.AbstractTimer;
import me.sabjen.discordbot.ui.controllers.MainController;

public class TimerRow {
    private Label name;
    private Label time;
    private Button button;
    private AbstractTimer timer;

    public TimerRow(MainController controller, String sLabel, AbstractTimer timer) {
        try {
            this.timer = timer;
            Pane pane = FXMLLoader.load(getClass().getResource("/ui/TimerRow.fxml"));
            controller.getTimersVbox().getChildren().add(pane);

            name = (Label) pane.lookup("#name");
            name.setText(sLabel);

            time = (Label) pane.lookup("#time");

            button = (Button) pane.lookup("#button");
            button.setOnMouseClicked(keyEvent -> {
                timer.runForced();
            });

        } catch (Exception ignored) {

        }
    }

    public void refresh() {
        Platform.runLater(() -> {
            time.setText(timer.getTimeLeft());
        });

    }
}
