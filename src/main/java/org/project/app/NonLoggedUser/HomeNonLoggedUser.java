package org.project.app.NonLoggedUser;

import javafx.fxml.FXML;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class HomeNonLoggedUser {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ImageView minimizeCloseIcon;

    @FXML
    void generalInformation() {
        setPage(anchorPane, "/Client/GeneralInformation.fxml");
    }

    @FXML
    void viewFlights()  {
        setPage(anchorPane, "/NonLoggedUser/ViewFlights.fxml");
    }

    @FXML
    void close() {
        Stage stage = (Stage) minimizeCloseIcon.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimize() {
        Stage stage = (Stage) minimizeCloseIcon.getScene().getWindow();
        stage.setIconified(true);
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
    void exitIcon() {
        setPage(anchorPane, "/LogIn_SignUp/LogIn.fxml");
    }
}
