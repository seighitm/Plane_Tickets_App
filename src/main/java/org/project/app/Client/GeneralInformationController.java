package org.project.app.Client;

import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.project.app.LogIn_SignUp.LoginController;

import java.io.IOException;

public class GeneralInformationController {

    LoginController log = new LoginController();

    @FXML
    private AnchorPane infoGen_page;

    @FXML
    private AnchorPane page;

    @FXML
    void back(MouseEvent event) throws IOException {
        if(log.getPers()==2)
            log.setPage(infoGen_page, "/FXML/User/HomeCustomer.fxml");
        else
            log.setPage(infoGen_page, "/FXML/Skip/HomeSkip.fxml");
    }

    @FXML
    void contact(MouseEvent event) throws IOException {
        log.setPage(page, "/FXML/User/GenInfo/Contact.fxml");
    }

    @FXML
    void hystory(MouseEvent event) throws IOException {
        log.setPage(page, "/FXML/User/GenInfo/Hystory.fxml");
    }

    @FXML
    void offers(MouseEvent event) throws IOException {
        log.setPage(page, "/FXML/User/GenInfo/News.fxml");
    }
}
