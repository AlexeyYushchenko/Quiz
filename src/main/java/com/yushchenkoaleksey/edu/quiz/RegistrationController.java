package com.yushchenkoaleksey.edu.quiz;

import com.yushchenkoaleksey.edu.quiz.db.Database;
import com.yushchenkoaleksey.edu.quiz.model.FXML_FILES;
import com.yushchenkoaleksey.edu.quiz.util.Utils;
import javafx.beans.binding.BooleanBinding;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import static javafx.beans.binding.Bindings.createBooleanBinding;

public class RegistrationController implements Initializable {

    @FXML private TextField loginField;
    @FXML private TextField passwordField;
    @FXML private Button createAccountButton;
    @FXML private Label infoPasswordLabel;
    @FXML private Label infoLoginLabel;
    @FXML private ImageView infoLogoPassword;
    @FXML private ImageView infoLogoLogin;
    @FXML private ImageView loginOk;
    @FXML private ImageView passwordOk;
    @FXML private Button signInButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image okSymbol = new Image(Objects.requireNonNull(getClass().getResourceAsStream("ok_symbol.png")));
        loginOk.setImage(okSymbol);
        loginOk.setVisible(false);
        passwordOk.setImage(okSymbol);
        passwordOk.setVisible(false);

        BooleanBinding loginFieldValid = createBooleanBinding(() -> loginField.textProperty().getValue().length() >= 4, loginField.textProperty());
        BooleanBinding passwordFieldValid = createBooleanBinding(() -> passwordField.textProperty().getValue().length() >= 6, passwordField.textProperty());
        loginOk.visibleProperty().bind(loginFieldValid);
        passwordOk.visibleProperty().bind(passwordFieldValid);
        createAccountButton.disableProperty().bind(loginFieldValid.not().or(passwordFieldValid.not()));

        EventHandler<KeyEvent> eventHandler = keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER && !createAccountButton.isDisabled()){
                createAccountButton.fire();
                keyEvent.consume();
            }};
        loginField.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        passwordField.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
    }

    public void signIn() {
        SceneController.switchTo(FXML_FILES.AUTHORIZATION.filename, loginField);
    }

    public void signUp() {
        if (!Database.checkUser(loginField.getText())) {
            Database.addUser(loginField.getText(), passwordField.getText());
            Utils.showAlert(Alert.AlertType.INFORMATION, "Congratulations!", "You've successfully created QUIZ account", null);
            SceneController.switchTo(FXML_FILES.AUTHORIZATION.filename, loginField);
        } else {
            Utils.showAlert(Alert.AlertType.ERROR, "ERROR", "Such user already exists", null);
        }
    }
}
