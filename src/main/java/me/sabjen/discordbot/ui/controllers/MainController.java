package me.sabjen.discordbot.ui.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import me.sabjen.discordbot.database.BotDatabase;
import me.sabjen.discordbot.modules.timers.BotTimers;
import me.sabjen.discordbot.ui.FXMLController;
import me.sabjen.discordbot.ui.controllers.util.ConversationStringWrapper;
import me.sabjen.discordbot.ui.controllers.util.SettingRow;
import me.sabjen.discordbot.ui.controllers.util.TimerRow;
import me.sabjen.discordbot.utils.console.ConsoleNotifier;
import me.sabjen.discordbot.utils.console.CustomConsoleAppender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class MainController extends FXMLController implements ConsoleNotifier {
    private boolean menuOpened;
    private boolean initialized = false;

    @FXML
    private VBox leftBar;

    private HBox[] buttons;
    @FXML
    private HBox consoleButton;
    @FXML
    private HBox timersButton;
    @FXML
    private HBox databaseButton;
    @FXML
    private HBox settingsButton;

    private Pane[] panes;
    @FXML
    private Pane consolePane;
    @FXML
    private Pane timersPane;
    @FXML
    private Pane databasePane;
    @FXML
    private Pane settingsPane;

    @FXML
    private Pane sideBarBackground;



    @Override
    public void initialize() {
        if(initialized) return;

        initialized = true;
        menuOpened = false;

        buttons = new HBox[]{consoleButton, timersButton, databaseButton, settingsButton};
        panes = new Pane[]{consolePane, timersPane, databasePane, settingsPane};

        initializeConsolePane();
        initializeTimersPane();
        initializeDatabasePane();
        initializeConfigPane();

        changePane(0);

        CustomConsoleAppender.notifiers.add(this);
    }

    public void toggleMenu() {
        menuOpened = !menuOpened;

        leftBar.setPrefWidth(menuOpened ? 175 : 50);

        for(var b : buttons) b.setPrefWidth(menuOpened ? 175 : 50);

        for(var l : leftBar.lookupAll(".sideBarText")) l.setVisible(menuOpened);

        sideBarBackground.setVisible(menuOpened);
    }

    public void toggleMenuHover() {
        if(!menuOpened) toggleMenu();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // CONSOLE PANE
    ///////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private TextArea consoleArea;

    public void openConsolePane() {changePane(0);}

    private void initializeConsolePane() {

    }

    @Override
    public void notify(String s) {
        consoleArea.setText(CustomConsoleAppender.getConsole());
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // TIMERS PANE
    ///////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private VBox timerVbox;
    public VBox getTimersVbox() { return timerVbox; }

    private List<TimerRow> timerRows;

    public void openTimersPane() {
        changePane(1);
    }

    private void initializeTimersPane() {
        timerRows = new ArrayList<>(Arrays.asList(
                new TimerRow(this, "Profile Photo Timer", BotTimers.avatarTimer),
                new TimerRow(this, "Random Sound Timer", BotTimers.soundTimer),
                new TimerRow(this, "Announce Timer", BotTimers.announceTimer)
        ));

        BotTimers.newFixedRateTimer(() -> {
            for(var t : timerRows) {
                t.refresh();
            }
        }, 1, TimeUnit.SECONDS).start();
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // DATABASE PANE
    ///////////////////////////////////////////////////////////////////////////////////////

    @FXML
    private ChoiceBox<ConversationStringWrapper> databaseChoice;

    @FXML
    private TableView<String> databaseTable;

    @FXML
    private TableColumn<String, String> databaseTableColumn;

    @FXML
    private TextArea databaseTextArea;

    @FXML
    private Button addToDatabaseButton;

    public void openDatabasePane() {
        changePane(2);
        refreshDatabase();
    }

    private void initializeDatabasePane() {
        ObservableList<ConversationStringWrapper> list = FXCollections.observableArrayList();

        list.addAll(Arrays.asList(
                new ConversationStringWrapper("ANNOUNCE", "Announcements"),
                new ConversationStringWrapper("MENTION", "@Mention/name Responses"),
                new ConversationStringWrapper("IMAGE", "Image Responses"),
                new ConversationStringWrapper("PRIVATE", "Private Channel Responses"),
                new ConversationStringWrapper("VOICE_JOIN", "Voice Join Response"),
                new ConversationStringWrapper("VOICE_LEFT", "Voice Left Response")
        ));

        databaseChoice.setItems(list);
        databaseChoice.getSelectionModel().select(0);
        databaseChoice.getSelectionModel().selectedItemProperty().addListener((value, oldV, newV) -> {
            refreshDatabase();
        });

        databaseTableColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));

        databaseTextArea.setOnKeyTyped((key) -> {
            addToDatabaseButton.setDisable(databaseTextArea.getText().trim().length() < 1);
        });

        refreshDatabase();
    }

    public void addToDatabase() {
        var str = databaseTextArea.getText();
        databaseTextArea.setText("");

        String type = databaseChoice.getSelectionModel().getSelectedItem().getType();

        BotDatabase.getInstance().getConversations().add(type, str);

        refreshDatabase();
    }

    private void refreshDatabase() {
        String type = databaseChoice.getSelectionModel().getSelectedItem().getType();
        System.out.println(type);

        var list = BotDatabase.getInstance().getConversations().getAll(type);

        databaseTable.setItems(FXCollections.observableList(list));
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // CONFIG PANE
    ///////////////////////////////////////////////////////////////////////////////////////
    @FXML
    private VBox settingsVbox;
    public VBox getSettingsVbox() { return settingsVbox; }

    private List<SettingRow> settingRows;

    private void initializeConfigPane() {
        settingRows = new ArrayList<>(Arrays.asList(
                new SettingRow(this, "Token", "The unique token of the Bot (you should keep this confidential)", Pattern.compile(".{70}"), "TOKEN"),
                new SettingRow(this, "Debug Server ID", "During debug mode, the Bot can only interact with a specific server", Pattern.compile("[0-9]+"), "DEBUG_SERVER"),
                new SettingRow(this, "Unskippable Chance", "Chance of a song to be unskippable when played by the bot ( 1.0 = 100% )", Pattern.compile("[01][.][0-9]+"), "UNSKIPPABLE_CHANCE"),
                new SettingRow(this, "Loop Chance", "Chance of the loop being turned on by the bot when it plays a random sound", Pattern.compile("[01][.][0-9]+"), "LOOP_CHANCE"),
                new SettingRow(this, "Music Preference Chance", "The bot can have preferences to what songs he queues by himself. Otherwise, he select a random one.", Pattern.compile("[01][.][0-9]+"), "MUSIC_TASTE_CHANCE"),
                new SettingRow(this, "Music Preference Skip Modifier", "The amount that gets added to the unskippable chance by how much the bot likes a song. ( ~0.05 is good )", Pattern.compile("[01][.][0-9]+"), "MUSIC_TASTE_SKIP")
        ));
    }

    @FXML
    private Button saveConfigButton;

    public void openSettingsPane() {
        changePane(3);

        resetSettingsPane();
    }

    public void resetSettingsPane() {
        for(var s : settingRows) {
            s.reset();
        }

        saveConfigButton.setDisable(true);
    }

    public void saveChanges() {
        for(var s : settingRows) {
            s.save();
        }

        saveConfigButton.setDisable(true);
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    // OTHER
    ///////////////////////////////////////////////////////////////////////////////////////

    private void changePane(int index) {
        for(int i = 0; i < panes.length; i++) {
            buttons[i].getStyleClass().remove("selected");
            panes[i].setVisible(i == index);
        }
        buttons[index].getStyleClass().add("selected");
    }


    public void checkButton() {
        for(var f : settingRows) {
            if(f.getField().getStyleClass().contains("badText")) {
                saveConfigButton.setDisable(true);
                return;
            }
        }

        saveConfigButton.setDisable(false);
    }

}
