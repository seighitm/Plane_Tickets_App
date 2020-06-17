package org.project.app.Admin;

import java.net.URL;

import org.project.app.LogIn_SignUp.LoginController;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.ResultSet;
import java.sql.Connection;
import org.project.app.Connection.DBHandler;
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
import org.project.app.Model.ModelAccount;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;

public class HomeAdminPage implements Initializable {

    private double xOffset = 0;
    private double yOffset = 0;

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
    private DBHandler handler;
    @FXML
    private PreparedStatement pst;
    @FXML
    private Connection connection;

    @FXML
    private Button id_exit;

    ObservableList<ModelAccount> oblist = FXCollections.observableArrayList();
    LoginController loginController = new LoginController();

    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        refresh();
    }

    @FXML
    public void exit(MouseEvent mouseEvent) {

        if (allert_window(1)){
            id_exit.getScene().getWindow().hide();
            final Stage id_login = new Stage();
            id_login.initStyle(StageStyle.UNDECORATED);
            Parent root = null;

            openNewPage(root, id_login);

        }
        loginController.setAutomationLogin(2);
    }

    void refresh()
    {
        try {
            String type_account;
            pst = connection.prepareStatement("SELECT * from tab1");
            ResultSet rs = pst.executeQuery();
            while(rs.next()) {
                if (rs.getInt("pers") != 3) {
                    if(rs.getInt("pers")==1)
                        type_account = "WORKER";
                    else if(rs.getInt("pers")==2)
                        type_account = "CUSTOMER";
                    else if(rs.getInt("pers")==-1)
                        type_account = "worker";
                    else if(rs.getInt("pers")==-2)
                        type_account = "customer";
                    else
                        type_account = "ADMIN";
                    oblist.add(new ModelAccount(rs.getInt("idtab1"), rs.getString("name"), rs.getString("email"), rs.getString("phone"), type_account));
                }
            }
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
                                        if(accounts.getPers().equals("ADMIN") || accounts.getPers().equals("CUSTOMER") || accounts.getPers().equals("WORKER")) {
                                            btn = new Button("Cancel");
                                            btn.setOnAction(event -> {
                                                String insert = "DELETE FROM tab1 WHERE idtab1=?";
                                                try {
                                                    pst = connection.prepareStatement(insert);
                                                    pst.setInt(1, accounts.getIdtab1());
                                                    pst.execute();
                                                    table.getItems().clear();
                                                    refresh();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                        }else {
                                            btn.setOnAction(event -> {
                                                String insert = "UPDATE tab1 SET pers=abs(pers) WHERE idtab1=?";
                                                try {
                                                    pst = connection.prepareStatement(insert);
                                                    pst.setInt(1, accounts.getIdtab1());
                                                    pst.executeUpdate();
                                                    table.getItems().clear();
                                                    refresh();
                                                } catch (SQLException e) {
                                                    e.printStackTrace();
                                                }
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

            action.setCellFactory(cellFactory);
            table.setItems(oblist);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean allert_window(int index)
    {
        if(index == 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Press the \"Ok\" button if you want to exit, otherwise press the \"Cancel\" button.");
            alert.setContentText("Do you want to go out?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("image/alert.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                return true;
        }
        return false;
    }

    public void openNewPage(Parent root, Stage stage)
    {
        try {
            root = FXMLLoader.load(getClass().getResource("/LogIn_SignUp/LogIn.fxml"));
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
}
