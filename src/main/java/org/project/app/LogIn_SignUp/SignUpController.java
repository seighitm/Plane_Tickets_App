package org.project.app.LogIn_SignUp;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import org.project.app.Connection.DBHandler;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable{

    private String md5_code0 = "123";
    private String md5_code1 = "456";

    LoginController log = new LoginController();

    @FXML
    private AnchorPane signup_page;

    @FXML
    private TextField username_field;
    @FXML
    private TextField email_field;
    @FXML
    private TextField phone_field;
    @FXML
    private PasswordField password_field;

    @FXML
    private RadioButton worker_radioButton;
    @FXML
    private RadioButton customer_radioButton;

    @FXML
    private Label success_label;

    @FXML
    private DBHandler handler;
    @FXML
    private PreparedStatement pst;
    @FXML
    private Connection connection;

    @FXML
    private ImageView min_close;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        success_label.setVisible(false);
    }

    @FXML
    void login(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/LogIn_SignUp/LogIn.fxml"));
        signup_page.getChildren().setAll(pane);
    }

    @FXML
    public void signUp(MouseEvent event) {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern0 = Pattern.compile(regex);
        Matcher matcher0 = pattern0.matcher(email_field.getText());
        regex = "^[0-9]{1}[0-9]{3,14}$";
        Pattern pattern1 = Pattern.compile(regex);
        Matcher matcher1 = pattern1.matcher(phone_field.getText());
        if(verification() && matcher1.matches() && matcher0.matches() && getAccount()!=0)
        {
            String insert = "Insert INTO tab1(name, pass, email, pers, phone)" + "Values(?,?,?,?,?)";
            try {
                pst = connection.prepareStatement(insert);
                pst.setString(1, username_field.getText());
                pst.setString(2, log.getMd5(log.getMd5(log.getMd5(md5_code0+ password_field.getText()) + log.getMd5(password_field.getText()+md5_code1))));
                pst.setString(3, email_field.getText());
                pst.setInt(4, getAccount());
                pst.setString(5, phone_field.getText());
                pst.executeUpdate();
                PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
                visiblePause.setOnFinished(event1 -> success_label.setVisible(false));
                success_label.setVisible(true);
                visiblePause.play();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if(!matcher1.matches()){
            alertWindow(1);
        } else if(!matcher0.matches()) {
            alertWindow(2);
        } else if(!verification()) {
            alertWindow(3);
        }
    }

    public int getAccount() {
        int pers = 0;
        if(worker_radioButton.isSelected()) {
            pers = -1;
        } else if (customer_radioButton.isSelected()) {
            pers = -2;
        } else {
            alertWindow(4);
        }
        return pers;
    }

    public boolean verification()  {
        int count = 0;
        try {
            String q1 = "SELECT * from tab1 where email=? or phone=?";
            pst = connection.prepareStatement(q1);
            pst.setString(1, email_field.getText());
            pst.setString(2, phone_field.getText());
            ResultSet rs = pst.                                                                                                                 executeQuery();
            while (rs.next()) {
                count ++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if(count >= 1) {
            return false;
        }
        return true;
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

    public void alertWindow(int index) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        if(index==1){
            alert.setContentText("Phone number is incorrect!");
        }else if(index==2){
            alert.setContentText("Email is incorrect!");
        }else if(index==3){
            alert.setContentText("Email or phone number has already been registered!");
        }else if(index==4){
            alert.setContentText("You have not chosen the account type!");
        }
        alert.show();
    }
}
