package org.project.app.Worker;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

import java.util.ResourceBundle;

public class HomeWorkerController implements  Initializable{

    @FXML
    private AnchorPane home_page;

    @FXML
    private AnchorPane main_page;

    @FXML
    private Button exit_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    void add_flight(MouseEvent event) {

    }

    @FXML
    void news(MouseEvent event) {

    }

    @FXML
    void view_flights(MouseEvent event){

    }

    @FXML
    void buy_ticket(MouseEvent event) {

    }

    @FXML
    public void exit(MouseEvent mouseEvent){

    }
}

