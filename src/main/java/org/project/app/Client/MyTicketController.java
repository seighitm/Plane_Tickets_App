package org.project.app.Client;

import java.net.URL;

import javafx.fxml.FXML;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ResourceBundle;
import java.sql.PreparedStatement;
import java.util.TimeZone;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;
import org.project.app.Model.ModelViewFlight;

public class MyTicketController implements Initializable{

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
    private TableColumn<ModelViewFlight, Integer> priceTable;
    @FXML
    private TableColumn<ModelViewFlight, Integer> hourTable;
    @FXML
    private TableColumn<ModelViewFlight, Integer> seatsTable;
    @FXML
    private TableColumn<ModelViewFlight, String> actionTable;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();
    LoginController loginController = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        viewMyTicket();
    }

    public void viewMyTicket() {
        int tempID=0;
        try {
            pst = connection.prepareStatement("SELECT * from tab1 where name=? and email=?");
            pst.setString(1, loginController.getTempUserName());
            pst.setString(2, loginController.getTempUserEmail());
            try(ResultSet rs2 = pst.executeQuery()){
                while(rs2.next()){
                    tempID=rs2.getInt("idtab1");
                }
            }
            pst = connection.prepareStatement("SELECT * from tab3 where id_user=?");
            pst.setInt(1, tempID);
            try(ResultSet rs1 = pst.executeQuery()) {
                while (rs1.next()) {
                    pst = connection.prepareStatement("SELECT * from tab2 where ID=?");
                    pst.setInt(1, rs1.getInt("id_air"));
                    ResultSet rs = pst.executeQuery();
                    while (rs.next()) {
                        oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Destination"), rs.getString("Location"), rs.getString("Date"), rs.getInt("Price"), rs.getString("Hour"), rs.getInt("Seats")));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        idTable.setCellValueFactory(new PropertyValueFactory("ID"));
        locationTable.setCellValueFactory(new PropertyValueFactory("Destination"));
        destinationTable.setCellValueFactory(new PropertyValueFactory("Location"));
        dateTable.setCellValueFactory(new PropertyValueFactory("Date"));
        priceTable.setCellValueFactory(new PropertyValueFactory("Price"));
        hourTable.setCellValueFactory(new PropertyValueFactory("Hour"));
        seatsTable.setCellValueFactory(new PropertyValueFactory("Seats"));


        Callback<TableColumn<ModelViewFlight, String>, TableCell<ModelViewFlight, String>> cellFactory
                = //
                new Callback<TableColumn<ModelViewFlight, String>, TableCell<ModelViewFlight, String>>() {
                    @Override
                    public TableCell call(final TableColumn<ModelViewFlight, String> param) {
                        final TableCell<ModelViewFlight, String> cell = new TableCell<ModelViewFlight, String>() {
                            ImageView selectedImage0 = new ImageView("/image/time.png");
                            ImageView selectedImage1 = new ImageView("/image/check.png");
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    ModelViewFlight curse = getTableView().getItems().get(getIndex());
                                    int var1 = curse.getDate().compareTo( testWinterTime() );
                                    if(var1>0)
                                        setGraphic(empty ? null : selectedImage0);
                                    else
                                        setGraphic(empty ? null : selectedImage1);
                                    setText(null);
                                }
                            }

                        };
                        return cell;
                    }
                };
        actionTable.setCellFactory(cellFactory);
        table.setItems(oblist);
        try {
            connection.close();
            pst.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public String testWinterTime() {
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