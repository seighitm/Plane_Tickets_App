package org.project.app.Admin;

import java.net.URL;
import javafx.util.Callback;
import javafx.fxml.FXML;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.PreparedStatement;
import javafx.scene.control.TableView;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import org.project.app.Connection.DBHandler;
import org.project.app.abstractHome;
import org.project.app.Model.ModelAccount;

public class HomeAdmin extends abstractHome implements Initializable {

    @FXML
    public TableView<ModelAccount> table;
    @FXML
    public TableColumn<ModelAccount, Integer> idtab1;
    @FXML
    public TableColumn<ModelAccount, String> name;
    @FXML
    public TableColumn<ModelAccount, String> email;
    @FXML
    public TableColumn<ModelAccount, String> phone;
    @FXML
    public TableColumn<ModelAccount, String> pers;
    @FXML
    public TableColumn<ModelAccount, String> action;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    ObservableList<ModelAccount> oblist = FXCollections.observableArrayList();

    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        setViewTable();
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
                    temp.add(new ModelAccount(rs.getInt("idtab1"), rs.getString("name"), rs.getString("email"), rs.getString("phone"), type_account));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return temp;
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
                                                deleteMethod(accounts.getEmail(), accounts.getPhone());
                                            });
                                        }else {
                                            btn.setOnAction(event -> {
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

    public void createConnection(){
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    //method for deleting account
    public void deleteMethod(String email, String phone) {
        String insert = "DELETE FROM tab1 WHERE email=? and phone=?";
        try {
            pst = connection.prepareStatement(insert);
            pst.setString(1, email);
            pst.setString(2, phone);
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
}
