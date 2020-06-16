package org.project.app.Client;


import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelViewFlight;
import org.project.app.LogIn_SignUp.LoginController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MyTicketController implements Initializable{

    @FXML
    private TableView<ModelViewFlight> table;
    @FXML
    private TableColumn<ModelViewFlight, Integer> id_table;
    @FXML
    private TableColumn<ModelViewFlight, String> location_table;
    @FXML
    private TableColumn<ModelViewFlight, String> destination_table;
    @FXML
    private TableColumn<ModelViewFlight, String> date_table;
    @FXML
    private TableColumn<ModelViewFlight, Integer> price_table;
    @FXML
    private TableColumn<ModelViewFlight, Integer> hour_table;
    @FXML
    private TableColumn<ModelViewFlight, Integer> seats_table;

    @FXML
    private DBHandler handler;
    @FXML
    private PreparedStatement pst;
    @FXML
    private Connection connection;

    ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();

    LoginController log = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        refresh();
    }

    public void refresh() {
        int aux=0;
        try {
            pst = connection.prepareStatement("SELECT * from tab1 where name=? and email=?");
            pst.setString(1, log.getTempUserName());
            pst.setString(2, log.getTempUserEmail());
            try(ResultSet rs2 = pst.executeQuery()){
                while(rs2.next()){
                    aux=rs2.getInt("idtab1");
                }
            }
            pst = connection.prepareStatement("SELECT * from tab3 where id_user=?");
            pst.setInt(1, aux);
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
        id_table.setCellValueFactory(new PropertyValueFactory("ID"));
        location_table.setCellValueFactory(new PropertyValueFactory("Destination"));
        destination_table.setCellValueFactory(new PropertyValueFactory("Location"));
        date_table.setCellValueFactory(new PropertyValueFactory("Date"));
        price_table.setCellValueFactory(new PropertyValueFactory("Price"));
        hour_table.setCellValueFactory(new PropertyValueFactory("Hour"));
        seats_table.setCellValueFactory(new PropertyValueFactory("Seats"));

        table.setItems(oblist);
        try {
            connection.close();
            pst.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}