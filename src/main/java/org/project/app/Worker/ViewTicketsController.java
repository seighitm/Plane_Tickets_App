package org.project.app.Worker;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
import javafx.util.Callback;
import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelTicket;

public class ViewTicketsController implements  Initializable{

    @FXML
    private TableView<ModelTicket> table;
    @FXML
    private TableColumn<ModelTicket, Integer> idTable;
    @FXML
    private TableColumn<ModelTicket, String> nameTable;
    @FXML
    private TableColumn<ModelTicket, String> flightTable;
    @FXML
    private TableColumn<ModelTicket, String> dateTable;
    @FXML
    private TableColumn<ModelTicket, String> actionTable;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    private int tempIdName;
    private int tempIdFlights;
    private int tempId;
    private String tempUserName;
    private String tempFlightName;
    private String tempDate;


    public String getTempDate() {
        return tempDate;
    }

    public void setTempDate(String tempDate) {
        this.tempDate = tempDate;
    }

    public String getTempUserName() {
        return tempUserName;
    }

    public void setTempUserName(String tempUserName) {
        this.tempUserName = tempUserName;
    }

    public String getTempFlightName() {
        return tempFlightName;
    }

    public void setTempFlightName(String tempFlightName) {
        this.tempFlightName = tempFlightName;
    }

    ObservableList<ModelTicket> oblist = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        createConnection();
        viewTickets();
    }

    void viewTickets() {
        table.setPlaceholder(new Label("No tickets were purchased"));
        try {
            String select = "SELECT * FROM tab3";
            pst = connection.prepareStatement(select);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    tempId = rs.getInt("id");
                    tempIdName = rs.getInt("id_user");
                    tempIdFlights = rs.getInt("id_air");
                    select = "SELECT * FROM tab2 WHERE ID=? AND Date>?";
                    pst = connection.prepareStatement(select);
                    setTempFlightName("");
                    pst.setInt(1, tempIdFlights);
                    pst.setString(2, timeZone());
                    try(ResultSet rs1 = pst.executeQuery()) {
                        while (rs1.next()) {
                            setTempFlightName(rs1.getString("Destination") + "-" + rs1.getString("Location"));
                            setTempDate(rs1.getString("Date"));
                        }
                    }
                    if(!getTempFlightName().equals("")) {
                        select = "SELECT * FROM tab1 WHERE idtab1=?";
                        pst = connection.prepareStatement(select);
                        pst.setInt(1, tempIdName);
                        try (ResultSet rs0 = pst.executeQuery()) {
                            while (rs0.next()) {
                                setTempUserName(rs0.getString("name"));
                            }
                        }

                        oblist.add(new ModelTicket(tempId, getTempUserName(), getTempFlightName(), getTempDate()));
                        idTable.setCellValueFactory(new PropertyValueFactory("id"));
                        nameTable.setCellValueFactory(new PropertyValueFactory("id_user"));
                        flightTable.setCellValueFactory(new PropertyValueFactory("id_fl"));
                        dateTable.setCellValueFactory(new PropertyValueFactory("date"));
                    }
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
                                                    cancelTicket(curse);
                                                    table.getItems().clear();
                                                    viewTickets();
                                                });
                                                setGraphic(empty ? null : btn);
                                                setText(null);
                                            }
                                        }

                                    };
                                    return cell;
                                }
                            };
                    actionTable.setCellFactory(cellFactory);
                    table.setItems(oblist);

                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createConnection(){
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    public void cancelTicket(ModelTicket curse){
        int temp_nr = 0;
        String select = "SELECT * from tab3 where ID=?";
        try {
            pst = connection.prepareStatement(select);
            pst.setInt(1, curse.getId());
            try(ResultSet rs2 = pst.executeQuery()) {
                while (rs2.next()) {
                    temp_nr = rs2.getInt("id_air");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String delete = "DELETE FROM tab3 WHERE ID=?";
        try {
            pst = connection.prepareStatement(delete);
            pst.setInt(1, curse.getId());
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String update = "UPDATE tab2 SET Seats=Seats+1 where ID=?";
        try {
            pst = connection.prepareStatement(update);
            pst.setInt(1, temp_nr);
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