package org.project.app.NonLoggedUser;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelViewFlight;

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

public class ViewFlightsController implements Initializable {

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
    void searchFlight() {
        ObservableList<ModelViewFlight> tempFlights = FXCollections.observableArrayList();
        if(!destinationField.getText().isEmpty() && !locationField.getText().isEmpty() && !dateField.getText().isEmpty()) {
            for (ModelViewFlight x : oblist) {
                if (x.getDestination().equals(destinationField.getText()) || x.getLocation().equals(locationField.getText()) || x.getDate().equals(dateField.getText()))
                    tempFlights.add(x);
            }
            idTable.setCellValueFactory(new PropertyValueFactory("ID"));
            locationTable.setCellValueFactory(new PropertyValueFactory("Location"));
            destinationTable.setCellValueFactory(new PropertyValueFactory("Destination"));
            dateTable.setCellValueFactory(new PropertyValueFactory("Date"));
            priceTable.setCellValueFactory(new PropertyValueFactory("Price"));
            hourTable.setCellValueFactory(new PropertyValueFactory("Hour"));
            seatsTable.setCellValueFactory(new PropertyValueFactory("Seats"));
            table.setItems(tempFlights);
        }else{
            alertWindows(1);
        }
    }

    public ObservableList<ModelViewFlight> viewFlights() {
        handler = new DBHandler();
        connection = handler.getConnection();
        table.getItems().clear();
        try {
            pst = connection.prepareStatement("SELECT * from tab2 where Seats!=? and Date>?");
            pst.setInt(1, 30);
            pst.setString(2, timeZone());
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()) {
                    oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getString("Hour"), rs.getInt("Seats")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idTable.setCellValueFactory(new PropertyValueFactory("ID"));
        locationTable.setCellValueFactory(new PropertyValueFactory("Location"));
        destinationTable.setCellValueFactory(new PropertyValueFactory("Destination"));
        dateTable.setCellValueFactory(new PropertyValueFactory("Date"));
        priceTable.setCellValueFactory(new PropertyValueFactory("Price"));
        hourTable.setCellValueFactory(new PropertyValueFactory("Hour"));
        seatsTable.setCellValueFactory(new PropertyValueFactory("Seats"));
        table.setItems(oblist);
        return oblist;
    }

    @FXML
    public void refreshButton() {
        viewFlights();
    }

    public void alertWindows(int index)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        if(index==1) {
            alert.setContentText("Account is not validated.\nPlease try again later.");
        }
        alert.show();
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