package me.sabjen.discordbot.ui;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import me.sabjen.discordbot.utils.resource.ResourceManager;
import me.sabjen.discordbot.ui.views.DebugServerView;
import me.sabjen.discordbot.ui.views.MainView;
import me.sabjen.discordbot.ui.views.ResourceAlertView;
import me.sabjen.discordbot.ui.views.StartView;

public class UI extends Application {
    private static Stage stage;
    private static FXMLView currentView;

    public static void startUI() {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        UI.stage = stage;
        stage.setResizable(false);
        stage.initStyle(StageStyle.TRANSPARENT);

        var status = ResourceManager.getInstance().initialize();

        if(status) currentView = new StartView(stage);
        else currentView = new ResourceAlertView(stage);
    }

    public static void startView() {
        stage.close();

        currentView = new StartView(stage);
    }

    public static void debugServerView() {
        stage.close();

        currentView = new DebugServerView(stage);
    }

    public static void mainView() {
        stage.close();

        currentView = new MainView(stage);
    }
}
