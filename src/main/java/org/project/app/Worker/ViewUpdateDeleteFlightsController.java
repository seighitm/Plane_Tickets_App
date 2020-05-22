package org.project.app.Worker;

import org.project.app.Model.ModelViewFlight;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.util.ResourceBundle;

public class ViewUpdateDeleteFlightsController implements Initializable{

    @FXML
    private AnchorPane view_page;

    @FXML
    private TextField location_field;
    @FXML
    private TextField destination_field;
    @FXML
    private TextField month_field;
    @FXML
    private TextField day_field;
    @FXML
    private TextField year_field;
    @FXML
    private TextField price_field;
    @FXML
    private TextField id_field;
    @FXML
    private TextField hour_field;
    @FXML
    private TextField seats_field;

    @FXML
    public Button search_button;
    @FXML
    public Button delete_button;

    @FXML
    private Label update_label;
    @FXML
    private Label delete_label;
    @FXML
    private Label notFound_label;
    @FXML
    private Label notID_label;

    @FXML
    private TextField dateSearch_field;
    @FXML
    private TextField locationSearch_field;
    @FXML
    private TextField destinationSearch_field;

    @FXML
    private TableView<ModelViewFlight> table;
    @FXML
    private TableColumn<ModelViewFlight, String> id_table;
    @FXML
    private TableColumn<ModelViewFlight, String> destination_table;
    @FXML
    private TableColumn<ModelViewFlight, String> location_table;
    @FXML
    private TableColumn<ModelViewFlight, String> date_table;
    @FXML
    private TableColumn<ModelViewFlight, String> price_table;
    @FXML
    private TableColumn<ModelViewFlight, String> hour_table;
    @FXML
    private TableColumn<ModelViewFlight, String> seats_table;
    @FXML
    private TableColumn<ModelViewFlight, String> action_table;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void back(MouseEvent event) {

    }

    @FXML
    void search_flights(MouseEvent event) {

    }

    @FXML
    public void refresh_button(MouseEvent mouseEvent) {

    }

    public void search_byID(MouseEvent mouseEvent) {

    }

    public void delete(MouseEvent mouseEvent) {

    }

    public void update(MouseEvent mouseEvent) {
    }
}
