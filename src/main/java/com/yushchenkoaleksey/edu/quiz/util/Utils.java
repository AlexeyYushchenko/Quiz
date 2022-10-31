package com.yushchenkoaleksey.edu.quiz.util;

import com.yushchenkoaleksey.edu.quiz.AppPreferencesAndSettings;
import com.yushchenkoaleksey.edu.quiz.Main;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class Utils {

    private static final FileChooser fileChooser;

    private Utils() {
    }

    static {
        fileChooser = new FileChooser();
        FileChooser.ExtensionFilter csvFilter = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fileChooser.getExtensionFilters().add(csvFilter);
        fileChooser.getExtensionFilters().add(jsonFilter);
    }

    public static void showAlert(Alert.AlertType type, String title, String headerText, String contentText) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.getDialogPane().setContent(new Label(contentText));
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.setAlwaysOnTop(true);
        alert.show();
    }

    public static File getOpenFileLocation() {
        fileChooser.setInitialDirectory(new File(AppPreferencesAndSettings.prefs.get("defaultOpenLocation", AppPreferencesAndSettings.DEFAULT_LOAD_LOCATION)));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) AppPreferencesAndSettings.prefs.put("defaultOpenLocation", file.getParentFile().getAbsolutePath());
        return file;
    }

    public static File getSaveFileLocation() {
        fileChooser.setInitialDirectory(new File(AppPreferencesAndSettings.prefs.get("defaultSaveLocation", AppPreferencesAndSettings.DEFAULT_SAVE_LOCATION)));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) AppPreferencesAndSettings.prefs.put("defaultSaveLocation", file.getParentFile().getAbsolutePath());
        return file;
    }

    public static void showSaveSuccessfulAlert() {
        Utils.showAlert(Alert.AlertType.INFORMATION, "Success", "Questions were successfully saved.", null);
    }

    public static void showSaveFailedAlert(String exceptionMessage) {
        Utils.showAlert(Alert.AlertType.ERROR, "ERROR", "Failed to save file.", exceptionMessage);
    }

    public static void showLoadFailedAlert() {
        Utils.showAlert(Alert.AlertType.ERROR, "ERROR", "File is damaged.", "Couldn't load questions from file.");
    }

    public static void setIcon(Stage stage) {
        stage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("icon.jpg"))));
    }
}
