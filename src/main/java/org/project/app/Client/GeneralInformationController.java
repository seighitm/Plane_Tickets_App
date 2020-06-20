package org.project.app.Client;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.project.app.LogIn_SignUp.LoginController;
import org.project.app.abstractGeneral;

public class GeneralInformationController extends abstractGeneral {

    @FXML
    private AnchorPane secondPage;

    LoginController loginController = new LoginController();

    @FXML
    void back() {
        if(loginController.getAccountType()==2 && loginController.getNonLoggedUser()!=1)
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
}
