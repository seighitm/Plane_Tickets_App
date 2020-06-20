package org.project.app.Worker;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import org.project.app.abstractHome;

import java.util.ResourceBundle;

public class HomeWorkerController extends abstractHome implements  Initializable{

    @FXML
    private AnchorPane secondPage;

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
}

