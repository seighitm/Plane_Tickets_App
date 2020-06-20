package org.project.app.NonLoggedUser;

import javafx.fxml.FXML;
import org.project.app.abstractHome;

public class HomeNonLoggedUser extends abstractHome {

    @FXML
    void generalInformation() {
        setPage(anchorPane, "/Client/GeneralInformation.fxml");
    }

    @FXML
    void viewFlights()  {
        setPage(anchorPane, "/NonLoggedUser/ViewFlights.fxml");
    }
}
