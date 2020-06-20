package org.project.app.Client;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import org.project.app.abstractHome;

public class HomeCustomerController extends abstractHome {

    @FXML
    private AnchorPane main_page;

    @FXML
    void generalInformation() {
        setPage(anchorPane, "/Client/GeneralInformation.fxml");
    }

    @FXML
    void myTickets(){
        setPage(main_page, "/Client/MyTicket.fxml");
    }

    @FXML
    void viewFlights() {
        setPage(anchorPane, "/Client/ViewFlights.fxml");
    }
}
