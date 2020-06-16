package org.project.app.Worker;

import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import org.project.app.LogIn_SignUp.LoginController;

import java.util.ResourceBundle;

public class HomeWorkerController implements  Initializable{

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane secondPage;
    @FXML
    private ImageView minimizeCloseIcon;

    LoginController loginController = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    void add_flight() {
        setPage(secondPage, "/Worker/AddFlight.fxml");
    }

    @FXML
    void news() {
        setPage(secondPage, "/Worker/AddNews.fxml");
    }

    @FXML
    void view_flights() {
        setPage(anchorPane, "/Worker/ViewUpdateDeleteFlights.fxml");
    }

    @FXML
    void buy_ticket() {
        setPage(secondPage, "/Worker/ViewTickets.fxml");
    }

    @FXML
    public void exit() {
        if (alertWindows(1)){
            setPage(anchorPane, "/LogIn_SignUp/LogIn.fxml");
        }
        loginController.setAutomationLogin(2);
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

    public boolean alertWindows(int index)
    {
        if(index==1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Press the \"Ok\" button if you want to exit, otherwise press the \"Cancel\" button.");
            alert.setContentText("Do you want to go out?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("image/alert.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                return true;
        }
        return false;
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

    @FXML
    void exitIcon() {
        if (alertWindows(1)){
            setPage(anchorPane, "/LogIn_SignUp/LogIn.fxml");
        }
        loginController.setAutomationLogin(2);
    }
}

