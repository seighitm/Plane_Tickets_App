package org.project.app.Admin;

import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.fxml.FXML;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import java.sql.PreparedStatement;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.input.MouseEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;
import org.project.app.Model.ModelAccount;

public class HomeAdminPage implements Initializable {

    @FXML
    private TableView<ModelAccount> table;
    @FXML
    private TableColumn<ModelAccount, Integer> idtab1;
    @FXML
    private TableColumn<ModelAccount, String> name;
    @FXML
    private TableColumn<ModelAccount, String> email;
    @FXML
    private TableColumn<ModelAccount, String> phone;
    @FXML
    private TableColumn<ModelAccount, String> pers;
    @FXML
    private TableColumn<ModelAccount, String> action;
    @FXML
    private AnchorPane Home;
    @FXML
    private ImageView id_exit;
    @FXML
    private ImageView minimizeCloseIcon;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    ObservableList<ModelAccount> oblist = FXCollections.observableArrayList();
    LoginController loginController = new LoginController();

    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        setViewTable();
    }

    //exit button
    @FXML
    public void exit(MouseEvent mouseEvent) {
        if (alertWindows(1)){
            try {
                setPage(Home, "/LogIn_SignUp/LogIn.fxml");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        loginController.setAutomationLogin(2);
    }

    //database reading
    public ObservableList<ModelAccount> readFromDatabase() {
        ObservableList<ModelAccount> temp = FXCollections.observableArrayList();
        handler = new DBHandler();
        connection = handler.getConnection();
        try {
            String type_account;
            pst = connection.prepareStatement("SELECT * from tab1");
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("pers") != 3) {
                    if (rs.getInt("pers") == 1)
                        type_account = "WORKER";
                    else if (rs.getInt("pers") == 2)
                        type_account = "CLIENT";
                    else if (rs.getInt("pers") == -1)
                        type_account = "worker";
                    else if (rs.getInt("pers") == -2)
                        type_account = "customer";
                    else
                        type_account = "ADMIN";
                    temp.add(new ModelAccount(rs.getInt("idtab1"), rs.getString("name"), rs.getString("email"), rs.getInt("phone"), type_account));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    //sets the cells in the table with the information read from the database
    void setViewTable() {
        try {
            oblist= readFromDatabase();
            idtab1.setCellValueFactory(new PropertyValueFactory("idtab1"));
            name.setCellValueFactory(new PropertyValueFactory("name"));
            email.setCellValueFactory(new PropertyValueFactory("email"));
            phone.setCellValueFactory(new PropertyValueFactory("phone"));
            pers.setCellValueFactory(new PropertyValueFactory("pers"));
            Callback<TableColumn<ModelAccount, String>, TableCell<ModelAccount, String>> cellFactory
                    = //
                    new Callback<TableColumn<ModelAccount, String>, TableCell<ModelAccount, String>>() {
                        @Override
                        public TableCell call(final TableColumn<ModelAccount, String> param) {
                            final TableCell<ModelAccount, String> cell = new TableCell<ModelAccount, String>() {

                                @Override
                                public void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (empty) {
                                        setGraphic(null);
                                        setText(null);
                                    } else {

                                        ModelAccount accounts = getTableView().getItems().get(getIndex());
                                        Button btn = new Button("Validate");
                                        btn.setPrefWidth(67);
                                        if(accounts.getPers().equals("ADMIN") || accounts.getPers().equals("CLIENT") || accounts.getPers().equals("WORKER")) {
                                            btn = new Button("Cancel");
                                            btn.setPrefWidth(67);
                                            btn.setOnAction(event -> {
                                                /*String insert = "DELETE FROM tab1 WHERE idtab1=?";
                                                try {
                                                    pst = connection.prepareStatement(insert);
                                                    pst.setInt(1, accounts.getIdtab1());
                                                    pst.execute();
                                                    table.getItems().clear();
                                                    setCellTable();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }*/
                                                deleteMethod(accounts.getIdtab1());
                                            });
                                        }else {
                                            btn.setOnAction(event -> {
                                                /*String insert = "UPDATE tab1 SET pers=abs(pers) WHERE idtab1=?";
                                                try {
                                                    pst = connection.prepareStatement(insert);
                                                    pst.setInt(1, accounts.getIdtab1());
                                                    pst.executeUpdate();
                                                    table.getItems().clear();
                                                    setCellTable();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }*/
                                                validateMethod(accounts.getIdtab1());
                                            });
                                        }
                                        setGraphic(btn);
                                        setText(null);
                                    }
                                }
                            };
                            return cell;
                        }
                    };
            action.setStyle("-fx-alignment: CENTER");
            action.setCellFactory(cellFactory);
            table.setItems(oblist);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //method for deleting account
    public void deleteMethod(int ID) {
        String insert = "DELETE FROM tab1 WHERE idtab1=?";
        try {
            pst = connection.prepareStatement(insert);
            pst.setInt(1, ID);
            pst.execute();
            table.getItems().clear();
            setViewTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //method for validation account
    public void validateMethod(int ID) {
        String insert = "UPDATE tab1 SET pers=abs(pers) WHERE idtab1=?";
        try {
            pst = connection.prepareStatement(insert);
            pst.setInt(1, ID);
            pst.executeUpdate();
            table.getItems().clear();
            setViewTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //alert windows messages
    public boolean alertWindows(int index) {
        if(index == 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Do you want to go out?");
            alert.setContentText("Press the \"Ok\" button if you want to exit, otherwise press the \"Cancel\" button.");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("image/alert.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                return true;
        }
        return false;
    }

    //set new page
    public void setPage(AnchorPane page, String patch) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(patch));
        page.getChildren().setAll(pane);
    }
/*
    public void openNewPage(Parent root, Stage stage)
    {
        try {
            root = FXMLLoader.load(getClass().getResource("/FXML/Sign/LoginMain.fxml"));
            root.setStyle("-fx-effect: innershadow(gaussian, #039ed3, 2, 1.0, 0, 0);");
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            root.setOnMousePressed(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    xOffset = event.getSceneX();
                    yOffset = event.getSceneY();
                }
            });
            root.setOnMouseDragged(new EventHandler<MouseEvent>() {
                public void handle(MouseEvent event) {
                    stage.setX(event.getScreenX() - xOffset);
                    stage.setY(event.getScreenY() - yOffset);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
 */

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
