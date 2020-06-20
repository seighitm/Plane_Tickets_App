package org.project.app.Client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.TableCell;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TimeZone;
import javafx.stage.Stage;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;
import org.project.app.Model.ModelViewFlight;
import javafx.util.Callback;
import org.project.app.abstractGeneral;

public class ViewFlightsController extends abstractGeneral implements Initializable{

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
    private TableColumn<ModelViewFlight, String> buttonTable;
    @FXML
    public TextField dateField;
    @FXML
    public TextField destinationField;
    @FXML
    public TextField locationField;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    LoginController loginController = new LoginController();
    public  ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createConnection();
        viewFlight();
    }

    public void createConnection(){
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    @FXML
    void back(){
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Client/HomeCustomer.fxml"));
            anchorPane.getChildren().setAll(pane);
            connection.close();
            pst.close();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    public int searchFlight() {
        try {
            table.getItems().clear();
            createConnection();
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
            if(nrRecords == 0) {
                alertWindow(1);
                setCellTable();
                return 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @FXML
    public void refreshButton() {
        table.getItems().clear();
        setCellTable();
    }

    public ObservableList<ModelViewFlight> readFromDataBase() {
        try {
            pst = connection.prepareStatement("SELECT * from tab2 where Seats!=? and Date>?");
            pst.setInt(1, 0);
            pst.setString(2, timeZone());
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getString("Hour"), rs.getInt("Seats")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return oblist;
    }

    public void setCellTable(){
        table.getItems().clear();
        readFromDataBase();
        idTable.setCellValueFactory(new PropertyValueFactory("ID"));
        locationTable.setCellValueFactory(new PropertyValueFactory("Location"));
        destinationTable.setCellValueFactory(new PropertyValueFactory("Destination"));
        dateTable.setCellValueFactory(new PropertyValueFactory("Date"));
        priceTable.setCellValueFactory(new PropertyValueFactory("Price"));
        hourTable.setCellValueFactory(new PropertyValueFactory("Hour"));
        seatsTable.setCellValueFactory(new PropertyValueFactory("Seats"));
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

    public void viewFlight()
    {
        setCellTable();
        Callback<TableColumn<ModelViewFlight, String>, TableCell<ModelViewFlight, String>> cellFactory
                = //
                new Callback<TableColumn<ModelViewFlight, String>, TableCell<ModelViewFlight, String>>() {
                    @Override
                    public TableCell call(final TableColumn<ModelViewFlight, String> param) {
                        final TableCell<ModelViewFlight, String> cell = new TableCell<ModelViewFlight, String>() {
                            final Button button = new Button("BUY");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    button.setOnAction(event -> {
                                        ModelViewFlight curse = getTableView().getItems().get(getIndex());
                                        updateNrTickets(curse);
                                    });
                                    setGraphic(button);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        buttonTable.setCellFactory(cellFactory);
        table.setItems(oblist);
    }

    public void updateNrTickets(ModelViewFlight curse) {
        String update = "UPDATE tab2 SET Seats=? WHERE ID=?";
        try {
            pst = connection.prepareStatement(update);
            int aux = curse.getSeats()-1;
            curse.setSeats(aux);
            pst.setInt(1, curse.getSeats());
            pst.setInt(2, curse.getID());
            if(alertConfirmationWindow(curse))
            {
                pst.executeUpdate();
                addMyFlight(curse.getID());
                setCellTable();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMyFlight(int airID) {
        createConnection();
        int temp_userID=0;
        try {
            String select = "SELECT * from tab1";
            pst = connection.prepareStatement(select);
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                if ((rs.getString("name").equals(loginController.getTempUserName())) && (rs.getString("email").equals(loginController.getTempUserEmail()))) {
                    temp_userID = rs.getInt("idtab1");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insert = "Insert INTO tab3(id_air, id_user)" + "Values(?,?)";
        try {
            pst = connection.prepareStatement(insert);
            pst.setInt(1, airID);
            pst.setInt(2, temp_userID);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean alertConfirmationWindow(ModelViewFlight curse) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Location: " + curse.getLocation() + "\nDestination: " + curse.getDestination() +
                "\nDate: " + curse.getDate() + "\nOHourra: " + curse.getHour() + "\nPrice: " + curse.getPrice());
        alert.setContentText("Press the ok button to confirm the purchase of the ticket.");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        return false;
    }
}