package org.project.app.NonLoggedUser;

import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelViewFlight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        refresh_automation();
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/LogIn_SignUp/LogIn.fxml"));
        view_page.getChildren().setAll(pane);
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void search_flight(MouseEvent event) {
        try {
            table.getItems().clear();
            pst = connection.prepareStatement("SELECT * from tab2 where Destination=? and Location=? and Date=?");
            pst.setString(1, destination_field.getText());
            pst.setString(2, location_field.getText());
            pst.setString(3, date_field.getText());
            int count = 0;
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()){
                    if(rs.getInt("Seats")>0) {
                        oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"), rs.getInt("Seats")));
                        count++;
                    }
                }
            }
            if(count == 0) {
                refresh_automation();
                allertWindow(1);
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

    public void refresh_automation() {
        table.getItems().clear();
        try {
            pst = connection.prepareStatement("SELECT * from tab2");
            try(ResultSet rs = pst.executeQuery()){
                while(rs.next()) {
                    if (rs.getInt("Seats") > 0) {
                        oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"), rs.getInt("Seats")));
                    }
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

    public void refresh_button() {
        refresh_automation();
    }

    public void allertWindow(int index)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        if(index==1) {
            alert.setContentText("No flights were found!");
        }
        alert.show();
    }
}