package org.project.app.Worker;

import java.net.URL;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.PreparedStatement;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.project.app.Connection.DBHandler;
import org.project.app.abstractClass;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddFlightsController extends abstractClass implements  Initializable{

    @FXML
    public TextField locationField;
    @FXML
    public TextField destinationField;
    @FXML
    public TextField priceField;
    @FXML
    public TextField hoursField;
    @FXML
    public TextField minutesField;
    @FXML
    public TextField seatsField;
    @FXML
    public DatePicker dateField;
    @FXML
    public Pane successAdded;

    private DBHandler handler;
    private PreparedStatement pst, aux;
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createConnection();
        successAdded.setVisible(false);
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public void addFlights() {
        String regex = "^\\d{0,8}?$";
        Pattern pattern0 = Pattern.compile(regex);
        Matcher matcher0 = pattern0.matcher(priceField.getText());
        regex = "^\\d{0,2}?$";
        Pattern pattern1 = Pattern.compile(regex);
        Matcher matcher1 = pattern1.matcher(hoursField.getText());
        Pattern pattern2 = Pattern.compile(regex);
        Matcher matcher2 = pattern2.matcher(minutesField.getText());
        regex = "^\\d{0,8}?$";
        Pattern pattern3 = Pattern.compile(regex);
        Matcher matcher3 = pattern3.matcher(seatsField.getText());
        if(matcher0.matches() && matcher1.matches() && matcher2.matches() && matcher3.matches()) {
            try {
                if (verificationExistingSameFlight()) {
                    String insert = "Insert INTO tab2(Destination, Location, Date, Price, Hour, Seats)" + "Values(?,?,?,?,?,?)";
                    pst = connection.prepareStatement(insert);
                    String date = dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    pst.setString(1, destinationField.getText());
                    pst.setString(2, locationField.getText());
                    pst.setString(3, date);
                    pst.setInt(4, Integer.parseInt(priceField.getText()));
                    pst.setString(5, hoursField.getText() + ":" + minutesField.getText());
                    pst.setInt(6, Integer.parseInt(seatsField.getText()));
                    pst.executeUpdate();
                    PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
                    visiblePause.setOnFinished(event1 -> successAdded.setVisible(false));
                    successAdded.setVisible(true);
                    visiblePause.play();
                } else {
                    alertWindow(17);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(!matcher0.matches()) {
            alertWindow(12);
        }else if(!matcher1.matches()){
            alertWindow(13);
        }else if(!matcher2.matches()){
            alertWindow(10);
        }else if(!matcher3.matches()){
            alertWindow(14);
        }
    }

    public void createConnection(){
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    @FXML
    void clearTextField() {
        destinationField.clear();
        locationField.clear();
        dateField.setValue(null);
        priceField.clear();
        hoursField.clear();
        seatsField.clear();
    }

    public boolean verificationExistingSameFlight() {
        String select = "SELECT * FROM tab2 where Destination=? and Location=? and Date=? and Price=? and Hour=? and Seats=?";
        int nrRecords = 0;
        try {
            aux = connection.prepareStatement(select);
            String date = dateField.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            aux.setString(1, destinationField.getText());
            aux.setString(2, locationField.getText());
            aux.setString(3, date);
            aux.setInt(4, Integer.parseInt(priceField.getText()));
            aux.setInt(5, Integer.parseInt(hoursField.getText()));
            aux.setInt(6, Integer.parseInt(seatsField.getText()));

            try(ResultSet rs = aux.executeQuery()) {
                while (rs.next()) {
                    nrRecords++;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(nrRecords>0)
            return false;
        else
            return true;
    }
}
