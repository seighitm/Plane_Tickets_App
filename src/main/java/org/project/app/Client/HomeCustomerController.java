package org.project.app.Client;

import javafx.fxml.FXML;
import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.project.app.LogIn_SignUp.LoginController;

public class HomeCustomerController {

    @FXML
    private AnchorPane home_page;
    @FXML
    private AnchorPane main_page;
    @FXML
    private ImageView minimizeCloseIcon;

    LoginController loginController = new LoginController();

    @FXML
    void generalInformation() {
        setPage(home_page, "/Client/GeneralInformation.fxml");
    }

    @FXML
    void myTickets(){
        setPage(main_page, "/Client/MyTicket.fxml");
    }

    @FXML
    void viewFlights() {
        setPage(home_page, "/Client/ViewFlights.fxml");
    }

    @FXML
    void exit(){
        if (alertWindows(1)){
            setPage(home_page, "/LogIn_SignUp/LogIn.fxml");
        }
        loginController.setAutomationLogin(2);
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

    public boolean alertWindows(int index)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        if(index==1) {
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you want to go out?");
            alert.setContentText("Press the \"Ok\" button if you want to exit, otherwise press the \"Cancel\" button.");
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
    void exitIcon() {
        if (alertWindows(1)){
            setPage(home_page, "/LogIn_SignUp/LogIn.fxml");
        }
        loginController.setAutomationLogin(2);
    }
}
