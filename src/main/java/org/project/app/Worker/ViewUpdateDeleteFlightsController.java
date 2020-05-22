package org.project.app.Worker;

import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelViewFlight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.project.app.LogIn_SignUp.LoginController;

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

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;


    ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        create_viewTable();
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Page.fxml"));
        view_page.getChildren().setAll(pane);
    }

    @FXML
    void search_flights(MouseEvent event) {

    }

    void create_viewTable()
    {
        delete_label.setVisible(false);
        notFound_label.setVisible(false);
        update_label.setVisible(false);
        notID_label.setVisible(false);
        try {
            pst = connection.prepareStatement("SELECT * from tab2");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"),rs.getInt("Seats")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        id_table.setCellValueFactory(new PropertyValueFactory("ID"));
        location_table.setCellValueFactory(new PropertyValueFactory("Location"));
        destination_table.setCellValueFactory(new PropertyValueFactory("Destination"));
        date_table.setCellValueFactory(new PropertyValueFactory("Date"));
        price_table.setCellValueFactory(new PropertyValueFactory("Price"));
        hour_table.setCellValueFactory(new PropertyValueFactory("Hour"));
        seats_table.setCellValueFactory(new PropertyValueFactory("Seats"));

        table.setItems(oblist);
    }

    public void refresh_button(MouseEvent mouseEvent) {
        refresh_automatic();
    }

    public void search_byID(MouseEvent mouseEvent) {

    }

    public void delete(MouseEvent mouseEvent) {

    }

    public void update(MouseEvent mouseEvent) {

    }

    void refresh_automatic() {
        table.getItems().clear();
        try {
            pst = connection.prepareStatement("SELECT * from tab2");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"), rs.getInt("Seats")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        id_table.setCellValueFactory(new PropertyValueFactory("ID"));
        location_table.setCellValueFactory(new PropertyValueFactory("Location"));
        destination_table.setCellValueFactory(new PropertyValueFactory("Destination"));
        date_table.setCellValueFactory(new PropertyValueFactory("Date"));
        price_table.setCellValueFactory(new PropertyValueFactory("Price"));
        hour_table.setCellValueFactory(new PropertyValueFactory("Hour"));
        seats_table.setCellValueFactory(new PropertyValueFactory("Seats"));
        table.setItems(oblist);
    }
}
