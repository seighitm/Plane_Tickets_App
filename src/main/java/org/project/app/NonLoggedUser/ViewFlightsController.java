package org.project.app.NonLoggedUser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelViewFlight;
import org.project.app.abstractGeneral;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.util.TimeZone;

public class ViewFlightsController extends abstractGeneral implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TableView<ModelViewFlight> table;
    @FXML
    private TableColumn<ModelViewFlight, Integer> idTable;
    @FXML
    private TableColumn<ModelViewFlight, String> locationTable;
    @FXML
    private TableColumn<ModelViewFlight, String> destinationTable;
    @FXML
    private TableColumn<ModelViewFlight, String> dateTable;
    @FXML
    private TableColumn<ModelViewFlight, String> priceTable;
    @FXML
    private TableColumn<ModelViewFlight, String> hourTable;
    @FXML
    private TableColumn<ModelViewFlight, String> seatsTable;
    @FXML
    private TextField dateField;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField locationField;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;
    ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        viewFlights();
    }

    @FXML
    void back() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/NonLoggedUser/HomeNonLoggedUser.fxml"));
            anchorPane.getChildren().setAll(pane);
            connection.close();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    void searchFlight(MouseEvent event) {
        try {
            table.getItems().clear();
            pst = connection.prepareStatement("SELECT * from tab2 where Destination=? and Location=? and Date=? and Seats!=? and Date>?");
            pst.setString(1, destinationField.getText());
            pst.setString(2, locationField.getText());
            pst.setString(3, dateField.getText());
            pst.setInt(4, 0);
            pst.setString(5, timeZone());
            int nrRecords = 0;
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getString("Hour"), rs.getInt("Seats")));
                    nrRecords++;
                }
            }
            viewFlights();
            if(nrRecords == 0) {
                alertWindow(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<ModelViewFlight> readFromDataBase() {
        handler = new DBHandler();
        connection = handler.getConnection();
        try {
            pst = connection.prepareStatement("SELECT * from tab2 where Seats!=? and Date>?");
            pst.setInt(1, 0);
            pst.setString(2, timeZone());
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()) {
                    oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getString("Hour"), rs.getInt("Seats")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oblist;
    }

    public void viewFlights(){
        readFromDataBase();
        idTable.setCellValueFactory(new PropertyValueFactory("ID"));
        locationTable.setCellValueFactory(new PropertyValueFactory("Location"));
        destinationTable.setCellValueFactory(new PropertyValueFactory("Destination"));
        dateTable.setCellValueFactory(new PropertyValueFactory("Date"));
        priceTable.setCellValueFactory(new PropertyValueFactory("Price"));
        hourTable.setCellValueFactory(new PropertyValueFactory("Hour"));
        seatsTable.setCellValueFactory(new PropertyValueFactory("Seats"));
        table.setItems(oblist);
    }

    @FXML
    public void refreshButton() {
        table.getItems().clear();
        viewFlights();
    }

    public String timeZone() {
        TimeZone tz = TimeZone.getTimeZone("Europe/Moscow");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        dateFormat.setTimeZone(tz);
        Calendar calendar = Calendar.getInstance();
        calendar.setLenient(false);
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        String mark = dateFormat.format(calendar.getTime());
        return mark;
    }
}