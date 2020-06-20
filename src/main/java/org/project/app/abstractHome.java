package org.project.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.project.app.LogIn_SignUp.LoginController;

import java.io.IOException;
import java.util.Optional;

public abstract class abstractHome extends abstractGeneral{

    LoginController loginController = new LoginController();

    public boolean alertWindows(int index)
    {
        if(index==1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you want to go out?");
            alert.setContentText("Press the \"Ok\" button if you want to exit, otherwise press the \"Cancel\" button.");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("image/alert.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                return true;
        }
        return false;
    }

    public void setPage(AnchorPane page, String patch) {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource(patch));
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.getChildren().setAll(pane);
    }

    @FXML
    public void exit() {
        if (alertWindows(1)){
            setPage(anchorPane, "/LogIn_SignUp/LogIn.fxml");
        }
        loginController.setAutomationLogin(2);
    }
}
