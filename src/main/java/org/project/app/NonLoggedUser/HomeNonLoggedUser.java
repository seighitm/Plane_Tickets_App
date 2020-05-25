package org.project.app.NonLoggedUser;

import javafx.fxml.FXML;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

public class HomeNonLoggedUser {

    @FXML
    private AnchorPane home_page;

    @FXML
    void view_flights(MouseEvent event) throws IOException {
        setPage(home_page, "/NonLoggedUser/ViewFlights.fxml");
    }

    @FXML
    void exit(MouseEvent event) throws IOException {
            setPage(home_page, "/LogIn_SignUp/LogIn.fxml");
    }

    public void setPage(AnchorPane page, String patch) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(patch));
        page.getChildren().setAll(pane);
    }
}
