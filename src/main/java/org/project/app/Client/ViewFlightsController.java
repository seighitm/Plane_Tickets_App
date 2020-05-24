package org.project.app.Client;

import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelViewFlight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;

public class ViewFlightsController implements Initializable {

    @FXML
    private AnchorPane view_page;

    @FXML
    private TableView<ModelViewFlight> table;
    @FXML
    private TableColumn<ModelViewFlight, Integer> id_table;
    @FXML
    private TableColumn<ModelViewFlight, String> location_table;
    @FXML
    private TableColumn<ModelViewFlight, String> destination_table;
    @FXML
    private TableColumn<ModelViewFlight, String> date_table;
    @FXML
    private TableColumn<ModelViewFlight, String> price_table;
    @FXML
    private TableColumn<ModelViewFlight, String> hour_table;
    @FXML
    private TableColumn<ModelViewFlight, String> seats_table;
    @FXML
    private TableColumn<ModelViewFlight, String> button_table;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    @FXML
    private TextField date_field;
    @FXML
    private TextField destination_field;
    @FXML
    private TextField location_field;

    ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    public void refresh_button(MouseEvent mouseEvent) {
    }

    public void back(MouseEvent mouseEvent) {
    }

    public void search_flight(MouseEvent mouseEvent) {
    }
}
