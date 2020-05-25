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
            log.setPage(infoGen_page, "/Client/HomeCustomer.fxml");
        else
            log.setPage(infoGen_page, "/NonLoggedUser/HomeNonLoggedUser.fxml");
    }

    @FXML
    void contact(MouseEvent event) throws IOException {
        log.setPage(page, "/Client/GenInfo/Contact.fxml");
    }

    @FXML
    void hystory(MouseEvent event) throws IOException {
        log.setPage(page, "/Client/GenInfo/Hystory.fxml");
    }

    @FXML
    void offers(MouseEvent event) throws IOException {
        log.setPage(page, "/Client/GenInfo/News.fxml");
    }
}
