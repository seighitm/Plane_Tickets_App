package org.project.app.Worker;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ViewUpdateDeleteFlightsController extends abstractGeneral implements Initializable{

    @FXML
    private AnchorPane anchorPage;
    @FXML
    public TextField locationField;
    @FXML
    public TextField destinationField;
    @FXML
    public TextField monthField;
    @FXML
    public TextField dayField;
    @FXML
    public TextField yearField;
    @FXML
    public TextField priceField;
    @FXML
    public TextField idField;
    @FXML
    public TextField hourField;
    @FXML
    public TextField minutesField;
    @FXML
    public TextField seatsField;
    @FXML
    public Button searchButton;
    @FXML
    public Button deleteButton;
    @FXML
    public Label updateLabel;
    @FXML
    public Label deleteLabel;
    @FXML
    public Label notFoundLabel;
    @FXML
    public Label notIdLabel;
    @FXML
    private TextField dateSearchField;
    @FXML
    private TextField locationSearchField;
    @FXML
    private TextField destinationSearchField;
    @FXML
    public TableView<ModelViewFlight> table;
    @FXML
    public TableColumn<ModelViewFlight, String> idTable;
    @FXML
    public TableColumn<ModelViewFlight, String> destinationTable;
    @FXML
    public TableColumn<ModelViewFlight, String> locationTable;
    @FXML
    public TableColumn<ModelViewFlight, String> dateTable;
    @FXML
    public TableColumn<ModelViewFlight, String> priceTable;
    @FXML
    public TableColumn<ModelViewFlight, String> hourTable;
    @FXML
    public TableColumn<ModelViewFlight, String> seatsTable;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    public static int Hour, Seats;

    ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        createViewTable();
    }

    @FXML
    void backButton() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Worker/HomeWorker.fxml"));
        anchorPage.getChildren().setAll(pane);
    }

    @FXML
    void searchFlightsSystem(MouseEvent event) {
        try {
            table.getItems().clear();
            pst = connection.prepareStatement("SELECT * from tab2 where Destination=? and Location=? and Date=? and Seats!=? and Date>?");
            pst.setString(1, destinationSearchField.getText());
            pst.setString(2, locationSearchField.getText());
            pst.setString(3, dateSearchField.getText());
            pst.setInt(4, 0);
            pst.setString(5, timeZone());
            int nrRecords = 0;
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getString("Hour"), rs.getInt("Seats")));
                    nrRecords++;
                }
            }
            if(nrRecords == 0) {
                createViewTable();
                alertWindow(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createViewTable() {
        offVisibleLabel();
        try {
            pst = connection.prepareStatement("SELECT * from tab2 where Seats!=? and Date>?");
            pst.setInt(1, 0);
            pst.setString(2, timeZone());
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getString("Hour"),rs.getInt("Seats")));
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
        table.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() > 1) {
                selectTableRow();
            }
        });
    }

    public void selectTableRow() {
        if (table.getSelectionModel().getSelectedItem() != null) {
            ModelViewFlight curse = table.getSelectionModel().getSelectedItem();
            idField.setText(String.valueOf(curse.getID()));
            destinationField.setText(curse.getLocation());
            locationField.setText(curse.getDestination());
            String[] temp = new String[3];
            int i = 0;
            for (String val : curse.getDate().split("-")) {
                temp[i] = val;
                i++;
            }
            yearField.setText(temp[0]);
            monthField.setText(temp[1]);
            dayField.setText(temp[2]);
            priceField.setText(String.valueOf(curse.getPrice()));
            i = 0;
            for (String val : curse.getHour().split(":")) {
                temp[i] = val;
                i++;
            }
            hourField.setText(temp[0]);
            if(i==2) minutesField.setText(temp[1]);
            else  minutesField.setText("00");
            seatsField.setText(String.valueOf(curse.getSeats()));
        }
    }

    @FXML
    public void refreshButton() {
        setCellTable();
    }

    @FXML
    public void searchByID(MouseEvent mouseEvent) {
        offVisibleLabel();
        int nrRecords = 0;
        if(!idField.getText().isEmpty()) {
            for(ModelViewFlight x : oblist) {
                if (Integer.parseInt(idField.getText())==(x.getID())) {
                    destinationField.setText(x.getDestination());
                    locationField.setText(x.getLocation());
                    String[] temp = new String[3];
                    int i = 0;
                    for (String val : (x.getDate()).split("-")) {
                        temp[i] = val;
                        i++;
                    }
                    yearField.setText(temp[0]);
                    monthField.setText(temp[1]);
                    dayField.setText(temp[2]);
                    priceField.setText(String.valueOf(x.getPrice()));
                    i = 0;
                    for (String val : x.getHour().split(":")) {
                        temp[i] = val;
                        i++;
                    }
                    hourField.setText(temp[0]);
                    if(i==2) minutesField.setText(temp[1]);
                    else  minutesField.setText("00");
                    seatsField.setText(String.valueOf(x.getSeats()));
                    nrRecords++;
                }
            }
            if(nrRecords == 0) {
                clearAllField();
            }
        }else{
            setVisibleLabel(1);
        }
    }

    public void setVisibleLabel(int index)
    {
        PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
        if(index == 1) {
            visiblePause.setOnFinished(event1 -> notIdLabel.setVisible(false));
            notIdLabel.setVisible(true);
        }else if(index == 2){
            visiblePause.setOnFinished(event1 -> notFoundLabel.setVisible(false));
            notFoundLabel.setVisible(true);
        }else if(index == 3){
            visiblePause.setOnFinished(event1 -> deleteLabel.setVisible(false));
            deleteLabel.setVisible(true);
        }else if(index == 4){
            visiblePause.setOnFinished(event1 -> updateLabel.setVisible(false));
            updateLabel.setVisible(true);
        }else if(index == 5){

        }
        visiblePause.play();
    }

    public void clearAllField(){
        destinationField.clear();
        locationField.clear();
        yearField.clear();
        monthField.clear();
        dayField.clear();
        priceField.clear();
        hourField.clear();
        minutesField.clear();
        seatsField.clear();
        notFoundLabel.setVisible(true);
        setVisibleLabel(2);
    }

    public void offVisibleLabel() {
        deleteLabel.setVisible(false);
        notFoundLabel.setVisible(false);
        updateLabel.setVisible(false);
        notIdLabel.setVisible(false);
    }

    public void setCellTable() {
        oblist = refresh_automatic();
        table.getItems().clear();
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
    public void deleteFlight() {
        deleteLabel.setVisible(false);
        notFoundLabel.setVisible(false);
        updateLabel.setVisible(false);
        notIdLabel.setVisible(false);
        if(!idField.getText().equals("")) {
            if(verificationIfExistRecord()) {
                String insert = "DELETE FROM tab2 WHERE ID=?";
                try {
                    pst = connection.prepareStatement(insert);
                    pst.setInt(1, Integer.parseInt(idField.getText()));
                    pst.execute();
                    setCellTable();
                    setVisibleLabel(3);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{
            setVisibleLabel(2);
        }
    }

    public void updateFlightInfo(MouseEvent mouseEvent) {
        String regex = "^\\d{0,8}?$";
        Pattern pattern0 = Pattern.compile(regex);
        Matcher matcher0 = pattern0.matcher(priceField.getText());
        regex = "^\\d{0,2}?$";
        Pattern pattern1 = Pattern.compile(regex);
        Matcher matcher1 = pattern1.matcher(hourField.getText());
        Pattern pattern2 = Pattern.compile(regex);
        Matcher matcher2 = pattern2.matcher(minutesField.getText());
        Pattern pattern4 = Pattern.compile(regex);
        Matcher matcher4 = pattern4.matcher(dayField.getText());
        Pattern pattern5 = Pattern.compile(regex);
        Matcher matcher5 = pattern5.matcher(monthField.getText());
        regex = "^\\d{0,8}?$";
        Pattern pattern3 = Pattern.compile(regex);
        Matcher matcher3 = pattern3.matcher(seatsField.getText());
        regex = "^\\d{0,4}?$";
        Pattern pattern6 = Pattern.compile(regex);
        Matcher matcher6 = pattern6.matcher(yearField.getText());
        if(!idField.getText().equals("")) {
            if(matcher0.matches() && matcher1.matches() && matcher2.matches() && matcher3.matches()
                    && matcher4.matches() && matcher5.matches() && matcher6.matches()) {
                if (verificationIfExistRecord()) {
                    deleteLabel.setVisible(false);
                    notFoundLabel.setVisible(false);
                    updateLabel.setVisible(false);
                    notIdLabel.setVisible(false);
                    String insert = "UPDATE tab2 SET Destination=?, Location=?, Date=?, Price=?, Hour=?, Seats=? WHERE ID=?";
                    try {
                        pst = connection.prepareStatement(insert);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        pst.setString(1, locationField.getText());
                        pst.setString(2, destinationField.getText());
                        pst.setString(3, yearField.getText() + "-" + monthField.getText() + "-" + dayField.getText());
                        pst.setInt(4, Integer.parseInt(priceField.getText()));
                        pst.setString(5, hourField.getText() + ":" + minutesField.getText());
                        pst.setInt(6, Integer.parseInt(seatsField.getText()));
                        pst.setInt(7, Integer.parseInt(idField.getText()));
                        setVisibleLabel(4);
                        pst.executeUpdate();
                        setCellTable();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }else if(!matcher0.matches()) {
                alertWindow(12);
            }else if(!matcher2.matches() || !matcher1.matches()) {
                alertWindow(13);
            }else if(!matcher4.matches() || !matcher5.matches() || !matcher6.matches()){
                alertWindow(11);
            }else if(!matcher3.matches()){
                alertWindow(14);
            }
        }else{
            setVisibleLabel(1);
        }

    }

    public ObservableList<ModelViewFlight> refresh_automatic() {
        handler = new DBHandler();
        connection = handler.getConnection();
        ObservableList<ModelViewFlight> list = FXCollections.observableArrayList();
        try {
            pst = connection.prepareStatement("SELECT * from tab2 where Seats!=? and Date>?");
            pst.setInt(1, 0);
            pst.setString(2, timeZone());
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                list.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getString("Hour"), rs.getInt("Seats")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean verificationIfExistRecord() {
        int nrRecords = 0;
        for (ModelViewFlight x : oblist) {
            if (Integer.parseInt(idField.getText())==(x.getID())) {
                nrRecords++;
            }
        }
        if(nrRecords==0){
            clearAllField();
            return false;
        }
        return true;
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
