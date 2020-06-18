package org.project.app.Client;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.project.app.LogIn_SignUp.LoginController;

public class GeneralInformationController {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane secondPage;
    @FXML
    private ImageView minimizeCloseIcon;

    LoginController loginController = new LoginController();

    @FXML
    void back() {
        if(loginController.getPers()==2 && loginController.getNonLoggedUser()!=1)
            loginController.setPage(anchorPane, "/Client/HomeCustomer.fxml");
        else
            loginController.setPage(anchorPane, "/NonLoggedUser/HomeNonLoggedUser.fxml");
    }

    @FXML
    void contact() {
        loginController.setPage(secondPage, "/Client/GenInfo/Contact.fxml");
    }

    @FXML
    void history() {
        loginController.setPage(secondPage, "/Client/GenInfo/Hystory.fxml");
    }

    @FXML
    void offers(){
        loginController.setPage(secondPage, "/Client/GenInfo/News.fxml");
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
}
