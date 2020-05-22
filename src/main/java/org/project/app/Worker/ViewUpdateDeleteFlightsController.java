package org.project.app.Worker;

import javafx.animation.PauseTransition;
import javafx.scene.control.*;
import javafx.util.Callback;
import javafx.util.Duration;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;
import org.project.app.Model.ModelViewFlight;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewUpdateDeleteFlightsController implements Initializable{

    @FXML
    private AnchorPane view_page;

    @FXML
    private TextField location_field;
    @FXML
    private TextField destination_field;
    @FXML
    private TextField month_field;
    @FXML
    private TextField day_field;
    @FXML
    private TextField year_field;
    @FXML
    private TextField price_field;
    @FXML
    private TextField id_field;
    @FXML
    private TextField hour_field;
    @FXML
    private TextField seats_field;

    @FXML
    public Button search_button;
    @FXML
    public Button delete_button;

    @FXML
    private Label update_label;
    @FXML
    private Label delete_label;
    @FXML
    private Label notFound_label;
    @FXML
    private Label notID_label;

    @FXML
    private TextField dateSearch_field;
    @FXML
    private TextField locationSearch_field;
    @FXML
    private TextField destinationSearch_field;

    @FXML
    private TableView<ModelViewFlight> table;
    @FXML
    private TableColumn<ModelViewFlight, String> id_table;
    @FXML
    private TableColumn<ModelViewFlight, String> destination_table;
    @FXML
    private TableColumn<ModelViewFlight, String> location_table;
    @FXML
    private TableColumn<ModelViewFlight, String> date_table;
    @FXML
    private TableColumn<ModelViewFlight, String> price_table;
    @FXML
    private TableColumn<ModelViewFlight, String> hour_table;
    @FXML
    private TableColumn<ModelViewFlight, String> seats_table;
    @FXML
    private TableColumn<ModelViewFlight, String> action_table;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;


    public static int idd, pre, Hour, Seats;
    public static String loc, des, dat;

    ObservableList<ModelViewFlight> oblist = FXCollections.observableArrayList();
    LoginController log = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        create_viewTable();
    }

    @FXML
    void back(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Worker/HomeWorker.fxml"));
        view_page.getChildren().setAll(pane);
    }

    @FXML
    void search_flights(MouseEvent event) {
        try {
            table.getItems().clear();
            pst = connection.prepareStatement("SELECT * from tab2 where Destination=? and Location=? and Date=?");
            pst.setString(1, destinationSearch_field.getText());
            pst.setString(2, locationSearch_field.getText());
            pst.setString(3, dateSearch_field.getText());
            ResultSet rs = pst.executeQuery();
            int count = 0;
            while(rs.next()){
                oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"),rs.getInt("Seats")));
                count=count+1;
            }
            if(count == 0)
            {
                try {
                    pst = connection.prepareStatement("SELECT * from tab2");
                    rs = pst.executeQuery();
                    while(rs.next()){
                        oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"),rs.getInt("Seats")));
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

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setContentText("Nu a fost gasit nimic!");
                alert.show();
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

    void create_viewTable()
    {
        delete_label.setVisible(false);
        notFound_label.setVisible(false);
        update_label.setVisible(false);
        notID_label.setVisible(false);
        try {
            pst = connection.prepareStatement("SELECT * from tab2");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"),rs.getInt("Seats")));
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

        Callback<TableColumn<ModelViewFlight, String>, TableCell<ModelViewFlight, String>> cellFactory
                = //
                new Callback<TableColumn<ModelViewFlight, String>, TableCell<ModelViewFlight, String>>() {

                    public TableCell call(final TableColumn<ModelViewFlight, String> param) {
                        final TableCell<ModelViewFlight, String> cell = new TableCell<ModelViewFlight, String>() {
                            final Button btn = new Button("Select");
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                btn.setStyle("-fx-background-radius: 5em; -jfx-background-color: #9ebedd");
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        ModelViewFlight curse = getTableView().getItems().get(getIndex());
                                        idd = curse.getID();
                                        loc = curse.getLocation();
                                        des = curse.getDestination();
                                        dat = curse.getDate();
                                        pre = curse.getPrice();
                                        Hour = curse.getHour();
                                        Seats = curse.getSeats();
                                        //////////////////////
                                        id_field.setText(String.valueOf(curse.getID()));
                                        destination_field.setText(curse.getDestination());
                                        location_field.setText(curse.getLocation());
                                        String[] temp = new String[3];
                                        int i = 0;
                                        for (String val : curse.getDate().split("-")) {
                                            temp[i] = val;
                                            i++;
                                        }
                                        year_field.setText(temp[0]);
                                        month_field.setText(temp[1]);
                                        day_field.setText(temp[2]);
                                        price_field.setText(String.valueOf(curse.getPrice()));
                                        hour_field.setText(String.valueOf(curse.getHour()));
                                        seats_field.setText(String.valueOf(curse.getSeats()));
                                        ///////////////////////
                                    });
                                    setGraphic(empty ? null : btn);
                                    setText(null);
                                }
                            }

                        };
                        return cell;
                    }
                };
        action_table.setCellFactory(cellFactory);
        table.setItems(oblist);
    }

    public void refresh_button(MouseEvent mouseEvent) {
        refresh_automatic();
    }

    public void search_byID(MouseEvent mouseEvent) {
        delete_label.setVisible(false);
        notFound_label.setVisible(false);
        update_label.setVisible(false);
        notID_label.setVisible(false);

        if(!id_field.getText().isEmpty())
        {
            String insert = "SELECT * FROM tab2 WHERE ID=?";
            try {
                pst = connection.prepareStatement(insert);
                pst.setInt(1, Integer.parseInt(id_field.getText()));
                ResultSet rst = pst.executeQuery();
                if(rst.next()){
                    destination_field.setText(rst.getString(2));
                    location_field.setText(rst.getString(3));
                    String[] temp = new String[3];
                    int i = 0;
                    for (String val : (rst.getString(4)).split("-")) {
                        temp[i] = val;
                        i++;
                    }
                    year_field.setText(temp[0]);
                    month_field.setText(temp[1]);
                    day_field.setText(temp[2]);
                    price_field.setText(String.valueOf(rst.getInt(5)));
                    hour_field.setText(String.valueOf(rst.getInt(6)));
                    seats_field.setText(String.valueOf(rst.getInt(7)));
                } else {
                    destination_field.clear();
                    location_field.clear();
                    year_field.clear();
                    month_field.clear();
                    day_field.clear();
                    price_field.clear();
                    hour_field.clear();
                    seats_field.clear();
                    notFound_label.setVisible(true);
                    PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
                    visiblePause.setOnFinished(event1 -> notFound_label.setVisible(false));
                    notFound_label.setVisible(true);
                    visiblePause.play();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else{
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
            visiblePause.setOnFinished(event1 -> notID_label.setVisible(false));
            notID_label.setVisible(true);
            visiblePause.play();
        }
    }

    public void delete(MouseEvent mouseEvent) {
        delete_label.setVisible(false);
        notFound_label.setVisible(false);
        update_label.setVisible(false);
        notID_label.setVisible(false);
        if(!id_field.getText().equals(""))
        {
            if(verification())
            {
                String insert = "DELETE FROM tab2 WHERE ID=?";
                try {
                    pst = connection.prepareStatement(insert);
                    pst.setInt(1, Integer.parseInt(id_field.getText()));
                    pst.execute();
                    refresh_automatic();

                    PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
                    visiblePause.setOnFinished(event1 -> delete_label.setVisible(false));
                    delete_label.setVisible(true);
                    visiblePause.play();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }else{
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
            visiblePause.setOnFinished(event1 -> notID_label.setVisible(false));
            notID_label.setVisible(true);
            visiblePause.play();
        }
    }

    public void update(MouseEvent mouseEvent) {
        if(!id_field.getText().equals(""))
        {
            if (verification())
            {
                if(id_field.getText().equals("")){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setContentText("You did not enter ID!");
                    alert.show();
                }else{
                    delete_label.setVisible(false);
                    notFound_label.setVisible(false);
                    update_label.setVisible(false);
                    notID_label.setVisible(false);
                    String insert = "UPDATE tab2 SET Destination=?, Location=?, Date=?, Price=?, Hour=?, Seats=? WHERE ID=?";
                    try {
                        pst = connection.prepareStatement(insert);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    try {
                        pst.setString(1, location_field.getText());
                        pst.setString(2, destination_field.getText());
                        pst.setString(3, year_field.getText()+"-"+ month_field.getText()+"-"+ day_field.getText());
                        pst.setInt(4, Integer.parseInt(price_field.getText()));
                        pst.setInt(5, Integer.parseInt(hour_field.getText()));
                        pst.setInt(6, Integer.parseInt(seats_field.getText()));
                        pst.setInt(7, Integer.parseInt(id_field.getText()));
                        if(Integer.parseInt(year_field.getText())<0 || Integer.parseInt(month_field.getText())<0 || Integer.parseInt(month_field.getText())>12 ||  Integer.parseInt(day_field.getText())<0 || Integer.parseInt(day_field.getText())>31) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setHeaderText(null);
                            alert.setContentText("IncHourct format of DATE!");
                            alert.show();
                        }else {
                            PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
                            visiblePause.setOnFinished(event1 -> update_label.setVisible(false));
                            update_label.setVisible(true);
                            visiblePause.play();
                            pst.executeUpdate();
                        }
                        refresh_automatic();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }}
            }
        }else{
            PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
            visiblePause.setOnFinished(event1 -> notID_label.setVisible(false));
            notID_label.setVisible(true);
            visiblePause.play();
        }

    }

    void refresh_automatic() {
        table.getItems().clear();
        try {
            pst = connection.prepareStatement("SELECT * from tab2");
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                oblist.add(new ModelViewFlight(rs.getInt("ID"), rs.getString("Location"), rs.getString("Destination"), rs.getString("Date"), rs.getInt("Price"), rs.getInt("Hour"), rs.getInt("Seats")));
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

    boolean verification()
    {
        String insert = "SELECT * FROM tab2 WHERE ID=?";
        int count = 0;
        try {
            pst = connection.prepareStatement(insert);
            pst.setInt(1, Integer.parseInt(id_field.getText()));
            ResultSet rst = pst.executeQuery();
            if(rst.next()){
                count++;
            }
            if(count==0){
                destination_field.clear();
                location_field.clear();
                year_field.clear();
                month_field.clear();
                day_field.clear();
                price_field.clear();
                hour_field.clear();
                seats_field.clear();
                PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
                visiblePause.setOnFinished(event1 -> notFound_label.setVisible(false));
                notFound_label.setVisible(true);
                visiblePause.play();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}