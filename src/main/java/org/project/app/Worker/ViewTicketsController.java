package org.project.app.Worker;

import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelTicket;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewTicketsController implements  Initializable{

    @FXML
    private TableView<ModelTicket> table_view;

    @FXML
    private TableColumn<ModelTicket, Integer> table_id;

    @FXML
    private TableColumn<ModelTicket, String> table_name;

    @FXML
    private TableColumn<ModelTicket, String> table_flight;

    @FXML
    private TableColumn<ModelTicket, Integer> table_hour;

    @FXML
    private TableColumn<ModelTicket, String> table_action;

    @FXML
    private DBHandler handler;

    @FXML
    private PreparedStatement pst;

    @FXML
    private Connection connection;

    private int temp_IDname;
    private int temp_IDflight;
    private int temp_id;
    private int temp_hour;
    private String temp_userName;

    public String getTemp_userName() {
        return temp_userName;
    }

    public void setTemp_userName(String temp_userName) {
        this.temp_userName = temp_userName;
    }

    public String getTemp_flightName() {
        return temp_flightName;
    }

    public void setTemp_flightName(String temp_flightName) {
        this.temp_flightName = temp_flightName;
    }

    private String temp_flightName;

    ObservableList<ModelTicket> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        View_tickets();
    }

    void View_tickets() {
        table_view.setPlaceholder(new Label("No tickets were purchased"));
        try {
            pst = connection.prepareStatement("SELECT * from tab3");
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    temp_id = rs.getInt("id");
                    temp_IDname = rs.getInt("id_user");
                    temp_IDflight = rs.getInt("id_air");

                    pst = connection.prepareStatement("SELECT * from tab2 where ID=?");
                    pst.setInt(1, temp_IDflight);
                    try (ResultSet rs1 = pst.executeQuery()) {
                        while (rs1.next()) {
                            temp_flightName = rs1.getString("Destination") + "-" + rs1.getString("Location");
                            temp_hour = rs1.getInt("Hour");
                        }
                    }

                    pst = connection.prepareStatement("SELECT * from tab1 where idtab1=?");
                    pst.setInt(1, temp_IDname);
                    try(ResultSet rs0 = pst.executeQuery()) {
                        while (rs0.next()) {
                            setTemp_userName(rs0.getString("name"));
                        }
                    }
                    oblist.add(new ModelTicket(temp_id, temp_userName, temp_flightName, temp_hour));

                    table_id.setCellValueFactory(new PropertyValueFactory("id"));
                    table_name.setCellValueFactory(new PropertyValueFactory("id_user"));
                    table_flight.setCellValueFactory(new PropertyValueFactory("id_fl"));
                    table_hour.setCellValueFactory(new PropertyValueFactory("Hour"));
                    Callback<TableColumn<ModelTicket, String>, TableCell<ModelTicket, String>> cellFactory
                            = //
                            new Callback<TableColumn<ModelTicket, String>, TableCell<ModelTicket, String>>() {
                                @Override
                                public TableCell call(final TableColumn<ModelTicket, String> param) {
                                    final TableCell<ModelTicket, String> cell = new TableCell<ModelTicket, String>() {
                                        final Button btn = new Button("Cancel ticket");

                                        @Override
                                        public void updateItem(String item, boolean empty) {
                                            super.updateItem(item, empty);
                                            if (empty) {
                                                setGraphic(null);
                                                setText(null);
                                            } else {
                                                btn.setOnAction(event -> {
                                                    ModelTicket curse = getTableView().getItems().get(getIndex());
                                                    int temp_nr = 0;
                                                    String insert = "SELECT * from tab3 where ID=?";
                                                    try {
                                                        pst = connection.prepareStatement(insert);
                                                        pst.setInt(1, curse.getId());
                                                        try(ResultSet rs2 = pst.executeQuery()) {
                                                            while (rs2.next()) {
                                                                temp_nr = rs2.getInt("id_air");
                                                            }
                                                        }
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }

                                                    insert = "DELETE FROM tab3 WHERE ID=?";
                                                    try {
                                                        pst = connection.prepareStatement(insert);
                                                        pst.setInt(1, curse.getId());
                                                        pst.execute();
                                                        refresh();
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }

                                                    insert = "UPDATE tab2 SET Seats=Seats+1 where ID=?";
                                                    try {
                                                        pst = connection.prepareStatement(insert);
                                                        pst.setInt(1, temp_nr);
                                                        pst.executeUpdate();
                                                    } catch (SQLException e) {
                                                        e.printStackTrace();
                                                    }
                                                });
                                                setGraphic(empty ? null : btn);
                                                setText(null);
                                            }
                                        }

                                    };
                                    return cell;
                                }
                            };
                    table_action.setCellFactory(cellFactory);

                    table_view.setItems(oblist);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void refresh()
    {
        table_view.getItems().clear();
        try {
            pst = connection.prepareStatement("SELECT * from tab3");
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    temp_id = rs.getInt("id");
                    temp_IDname = rs.getInt("id_user");
                    temp_IDflight = rs.getInt("id_air");

                    pst = connection.prepareStatement("SELECT * from tab2 where ID=?");
                    pst.setInt(1, temp_IDflight);
                    try(ResultSet rs1 = pst.executeQuery()) {
                        while (rs1.next()) {
                            temp_flightName = rs1.getString("Destination") + "-" + rs1.getString("Location");
                            temp_hour = rs1.getInt("Hour");
                        }
                    }
                    pst = connection.prepareStatement("SELECT * from tab1 where idtab1=?");
                    pst.setInt(1, temp_IDname);
                    try(ResultSet rs0 = pst.executeQuery()) {
                        while (rs0.next()) {
                            temp_userName = rs0.getString("name");
                        }
                    }
                    oblist.add(new ModelTicket(temp_id, temp_userName, temp_flightName, temp_hour));

                    table_id.setCellValueFactory(new PropertyValueFactory("id"));
                    table_name.setCellValueFactory(new PropertyValueFactory("id_user"));
                    table_flight.setCellValueFactory(new PropertyValueFactory("id_fl"));
                    table_hour.setCellValueFactory(new PropertyValueFactory("Hour"));
                    table_view.setItems(oblist);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}