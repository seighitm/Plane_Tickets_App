package main.java.org.project.app.Workers;

import Connection.DBHandler;
import Controllers.Sign.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class AddFlightsController implements  Initializable{

    @FXML
    private TextField field_location;

    @FXML
    private TextField field_destination;

    @FXML
    private TextField field_price;

    @FXML
    private TextField field_hours;

    @FXML
    private TextField field_seats;

    @FXML
    private DatePicker field_date;

    @FXML
    private DBHandler handler;

    @FXML
    private PreparedStatement pst, aux;

    @FXML
    private Connection connection;

    LoginController log = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    @FXML
    void add_flight(MouseEvent event) {
        try {
            if(verification()==true) {
                String insert = "Insert INTO tab2(Destination, Location, Date, Price, Hour, Seats)" + "Values(?,?,?,?,?,?)";
                pst = connection.prepareStatement(insert);
                String date = field_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                pst.setString(1, field_destination.getText());
                pst.setString(2, field_location.getText());
                pst.setString(3, date);
                pst.setInt(4, Integer.parseInt(field_price.getText()));
                pst.setInt(5, Integer.parseInt(field_hours.getText()));
                pst.setInt(6, Integer.parseInt(field_seats.getText()));
                pst.executeUpdate();
            }else
            {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Acest zbot exista deja!!");
                alert.show();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void clear_textField(MouseEvent event) {
        field_destination.clear();
        field_location.clear();
        field_date.setValue(null);
        field_price.clear();
        field_hours.clear();
        field_seats.clear();
    }

    boolean verification() throws SQLException {
        String insert = "SELECT * FROM tab2 where Destination=? and Location=? and Date=? and Price=? and Hour=? and Seats=?";
        int count = 0;
        try {
            aux = connection.prepareStatement(insert);
            String date = field_date.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            aux.setString(1, field_destination.getText());
            aux.setString(2, field_location.getText());
            aux.setString(3, date);
            aux.setInt(4, Integer.parseInt(field_price.getText()));
            aux.setInt(5, Integer.parseInt(field_hours.getText()));
            aux.setInt(6, Integer.parseInt(field_seats.getText()));
            ResultSet rs = aux.executeQuery();

            while (rs.next()) {
                count++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(count>0)
            return false;
        else
            return true;
    }
}
