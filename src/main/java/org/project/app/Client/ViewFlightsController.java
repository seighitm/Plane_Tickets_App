package org.project.app.Client;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;
import org.project.app.Model.ModelViewFlight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.input.MouseEvent;

public class ViewFlightsController implements Initializable {

    @FXML
    private AnchorPane view_page;

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
    private TableColumn<ModelViewFlight, String> price_table;
    @FXML
    private TableColumn<ModelViewFlight, String> hour_table;
    @FXML
    private TableColumn<ModelViewFlight, String> seats_table;
    @FXML
    private TableColumn<ModelViewFlight, String> button_table;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    @FXML
    private TextField date_field;
    @FXML
    private TextField destination_field;
    @FXML
    private TextField location_field;

    ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();
    LoginController log = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        view_flight();
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/FXML/User/HomeCustomer.fxml"));
        view_page.getChildren().setAll(pane);
        try {
            connection.close();
            pst.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @FXML
    void search_flight(MouseEvent event) {
        try {
            table.getItems().clear();
            pst = connection.prepareStatement("SELECT * from tab2 where Destination=? and Location=? and Date=? and Seats>0");
            pst.setString(1, destination_field.getText());
            pst.setString(2, location_field.getText());
            pst.setString(3, date_field.getText());
            int count = 0;
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"), rs.getInt("Seats")));
                    count++;
                }
            }
            if(count == 0) {
                allertWindow(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void refresh_button(MouseEvent mouseEvent) {
        refresh_automation();
    }

    public void refresh_automation() {
        table.getItems().clear();
        try {
            pst = connection.prepareStatement("SELECT * from tab2");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                if (rs.getInt("Seats") > 0) {
                    oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"), rs.getInt("Seats")));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        id_table.setCellValueFactory(new PropertyValueFactory("ID"));
        location_table.setCellValueFactory(new PropertyValueFactory("Location"));
        destination_table.setCellValueFactory(new PropertyValueFactory("Destination"));
        date_table.setCellValueFactory(new PropertyValueFactory("Date"));
        price_table.setCellValueFactory(new PropertyValueFactory("Price"));
        hour_table.setCellValueFactory(new PropertyValueFactory("Hour"));
        seats_table.setCellValueFactory(new PropertyValueFactory("Seats"));

        table.setItems(oblist);
    }

    public void view_flight()
    {
        refresh_automation();

        Callback<TableColumn<ModelViewFlight, String>, TableCell<ModelViewFlight, String>> cellFactory
                = //
                new Callback<TableColumn<ModelViewFlight, String>, TableCell<ModelViewFlight, String>>() {
                    @Override
                    public TableCell call(final TableColumn<ModelViewFlight, String> param) {
                        final TableCell<ModelViewFlight, String> cell = new TableCell<ModelViewFlight, String>() {

                            final Button btn = new Button("BUY");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        ModelViewFlight curse = getTableView().getItems().get(getIndex());
                                        String insert = "UPDATE tab2 SET Seats=? WHERE ID=?";
                                        try {
                                            pst = connection.prepareStatement(insert);
                                            int aux = curse.getSeats()-1;
                                            curse.setSeats(aux);
                                            pst.setInt(1, curse.getSeats());
                                            pst.setInt(2, curse.getID());

                                            if(allert_ConfirmationWindow(curse)){
                                                pst.executeUpdate();
                                                add_myFlight(curse.getID());
                                                refresh_automation();
                                            }

                                        } catch (SQLException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        button_table.setCellFactory(cellFactory);

        table.setItems(oblist);
    }

    private String tempEmailSend;

    void add_myFlight(int x)
    {
        int temp_userID=0;
        try {
            pst = connection.prepareStatement("SELECT * from tab1");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                if ((rs.getString("name").equals(log.getTempUserName())) && (rs.getString("email").equals(log.getTempUserEmail()))) {
                    temp_userID = rs.getInt("idtab1");
                    tempEmailSend = rs.getString("email");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insert = "Insert INTO tab3(id_air, id_user)" + "Values(?,?)";
        try {
            pst = connection.prepareStatement(insert);
            pst.setInt(1, x);
            pst.setInt(2, temp_userID);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void allertWindow(int index)
    {
        if(index==1) {
            refresh_automation();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("No flights were found!");
            alert.show();
        }
    }

    public boolean allert_ConfirmationWindow(ModelViewFlight curse) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Location: " + curse.getLocation() + "\nDestinatia: " + curse.getDestination() +
                "\nData: " + curse.getDate() + "\nOra: " + curse.getHour() + "\nPretul: " + curse.getPrice());
        alert.setContentText("Press the ok button to confirm the purchase of the ticket.");
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alertt.png"));
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        return false;
    }
}
