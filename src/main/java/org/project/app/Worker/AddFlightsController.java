package org.project.app.Worker;

import java.net.URL;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import java.sql.PreparedStatement;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.project.app.Connection.DBHandler;

import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddFlightsController implements  Initializable{

    @FXML
    private TextField locationField;
    @FXML
    private TextField destinationField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField hoursField;
    @FXML
    private TextField minutesField;
    @FXML
    private TextField seatsField;
    @FXML
    private DatePicker dateField;
    @FXML
    private ImageView minimizeCloseIcon;
    @FXML
    private Pane succesAdded;

    private DBHandler handler;
    private PreparedStatement pst, aux;
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        succesAdded.setVisible(false);
    }

    @FXML
    void addFlights() {
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
                    visiblePause.setOnFinished(event1 -> succesAdded.setVisible(false));
                    succesAdded.setVisible(true);
                    visiblePause.play();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.getIcons().add(new Image("image/alert.png"));
                    alert.setContentText("This flight already exists!");
                    alert.show();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(!matcher0.matches()) {
            alertWindow(0);
        }else if(!matcher1.matches()){
            alertWindow(1);
        }else if(!matcher2.matches()){
            alertWindow(2);
        }else if(!matcher3.matches()){
            alertWindow(3);
        }
    }

    public void alertWindow(int index) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        if(index==0){
            alert.setContentText("Wrong price format!");
        }else if(index==1){
            alert.setContentText("Wrong hour format!");
        }else if(index==2){
            alert.setContentText("Wrong minute format!");
        }else if(index==3){
            alert.setContentText("Wrong format of the number of seats!");
        }
        alert.show();
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

    boolean verificationExistingSameFlight() {
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

    @FXML
    void close() {
        Stage stage = (Stage) minimizeCloseIcon.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimize() {
        Stage stage = (Stage) minimizeCloseIcon.getScene().getWindow();
        stage.setIconified(true);
    }
}
