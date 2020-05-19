package org.project.app.application;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.project.app.application.Connection.DBHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import java.io.IOException;

public class LoginController extends Window implements Initializable {

    private static String temp_username;
    private static String temp_userEmail;

    private String patch;

    private static int pers;

    public String getTempUserName() {
        return temp_username;
    }

    public String getTempUserEmail() {
        return temp_userEmail;
    }

    public void setTempUserName(String temp_username) {
        this.temp_username=temp_username;
    }

    public void setTempUserEmail(String temp_userEmail) {
        this.temp_userEmail=temp_userEmail;
    }


    @FXML
    private AnchorPane login_page;

    @FXML
    private CheckBox check_remember;

    @FXML
    private TextField username_field;
    @FXML
    private PasswordField password_field;

    @FXML
    private RadioButton worker_button;
    @FXML
    private RadioButton customer_button;

    @FXML
    private DBHandler handler;
    @FXML
    private Connection connection;
    @FXML
    private PreparedStatement pst;

    @FXML
    private ImageView min_close;


    public static int aux = 0;
    public static int automation_login=0;

    public void setAutomation_login(int automation_login) {
        this.automation_login = automation_login;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        handler = new DBHandler();
        connection = handler.getConnection();
        check_remember.setSelected(true);
        if(!username_field.getText().isEmpty() && !password_field.getText().isEmpty() && getAccount()!=0 && automation_login==0) {
            aux=1;
            login();
            automation_login++;
        }
    }

    @FXML
    void signIn(MouseEvent mouseEvent){
        aux=0;
        login();
    }

    public void login()
    {
        setAutomation_login(1);
        if(!username_field.getText().isEmpty() && !password_field.getText().isEmpty() && getAccount()!=0) {
            String q1 = "SELECT * from tab1 where email=? and pass=? and (pers=? or pers=?)";
            try {
                pst = connection.prepareStatement(q1);
                pst.setString(1, username_field.getText());
                setTempUserEmail(username_field.getText());
                pst.setString(2, password_field.getText());
                pst.setInt(3, getAccount());
                pst.setInt(4, getAccount()-2*getAccount());
                int count = 0, temp= 0;
                try(ResultSet rs = pst.executeQuery()){
                    while (rs.next()) {
                        setTempUserName(rs.getString("name"));
                        temp=rs.getInt("pers");
                        count++;
                    }
                }
                if (count == 1) {
                    if(temp>0 && temp<4)
                    {
                        try {
                            setPage(login_page, patch);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else if(aux!=1){
                        allertWindow(1);
                    }
                } else if(aux!=1){
                    allertWindow(2);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(aux!=1){
            allertWindow(3);
        }
    }

    @FXML
    void clearTextField(MouseEvent event) {
        username_field.clear();
        password_field.clear();
        worker_button.setSelected(false);
        customer_button.setSelected(false);
    }

    public int getAccount()
    {
        return pers;
    }

    @FXML
    void signUp(MouseEvent mouseEvent) throws IOException {

    }

    @FXML
    void skip(MouseEvent event) throws IOException {

    }

    public void setPage(AnchorPane page, String patch) throws IOException {

    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) min_close.getScene().getWindow();
        stage.close();
    }

    @FXML
    void minimize() {
        Stage stage = (Stage) min_close.getScene().getWindow();
        stage.setIconified(true);
    }

    public void allertWindow(int index)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alertt.png"));

        if(index==1) {
            alert.setContentText("Account is not validated.\nPlease try again later.");
        } else if(index==2) {
            alert.setContentText("Username and/or password is not correct!");
        } else if(index==3) {
            alert.setContentText("You have not entered all the data!");
        }
        alert.show();
    }
}