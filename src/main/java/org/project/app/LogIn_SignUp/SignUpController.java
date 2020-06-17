package org.project.app.LogIn_SignUp;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController implements Initializable{

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private RadioButton workerRadioButton;
    @FXML
    private RadioButton customerRadioButton;
    @FXML
    private Label successfulRegistrationLabel;
    @FXML
    private ImageView minimizeCloseIcon;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    private final static String md5_code0 = "123";
    private final static String md5_code1 = "456";
    private String passwordShow;
    LoginController loginController = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        successfulRegistrationLabel.setVisible(false);
    }

    @FXML
    public void pressedShowPassword() {
        if(!passwordField.getText().equals("")) {
            passwordShow = passwordField.getText();
            passwordField.clear();
            passwordField.setPromptText(passwordShow);
        }
    }

    @FXML
    public void releasedShowPassword() {
        passwordField.setText(passwordShow);
        passwordField.setPromptText("Password");
    }

    @FXML
    void logIn() {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource("/LogIn_SignUp/LogIn.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        anchorPane.getChildren().setAll(pane);
    }

    @FXML
    public void signUp() {
        String regex = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern0 = Pattern.compile(regex);
        Matcher matcher0 = pattern0.matcher(emailField.getText());
        regex = "^[0-9]{1}[0-9]{3,14}$";
        Pattern pattern1 = Pattern.compile(regex);
        Matcher matcher1 = pattern1.matcher(phoneField.getText());
        if(NonDuplicationEmailPhone() && matcher1.matches() && matcher0.matches() && getTypeAccount()!=0) {
            String insert = "Insert INTO tab1(name, pass, email, pers, phone)" + "Values(?,?,?,?,?)";
            try {
                pst = connection.prepareStatement(insert);
                pst.setString(1, usernameField.getText());
                pst.setString(2, loginController.getMd5(loginController.getMd5(loginController.getMd5(md5_code0+ passwordField.getText()) + loginController.getMd5(passwordField.getText()+md5_code1))));
                pst.setString(3, emailField.getText());
                pst.setInt(4, getTypeAccount());
                pst.setString(5, phoneField.getText());
                pst.executeUpdate();
                PauseTransition visiblePause = new PauseTransition(Duration.seconds(3));
                visiblePause.setOnFinished(event1 -> successfulRegistrationLabel.setVisible(false));
                successfulRegistrationLabel.setVisible(true);
                visiblePause.play();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if(usernameField.getText().equals("") || passwordField.getText().equals("") ||
                emailField.getText().equals("") || phoneField.getText().equals("")){
            alertWindow(0);
        } else if(!matcher1.matches()){
            alertWindow(1);
        } else if(!matcher0.matches()) {
            alertWindow(2);
        } else if(!NonDuplicationEmailPhone()) {
            alertWindow(3);
        }
    }

    public int getTypeAccount() {
        int pers = 0;
        if(workerRadioButton.isSelected()) {
            pers = -1;
        } else if (customerRadioButton.isSelected()) {
            pers = -2;
        } else {
            alertWindow(4);
        }
        return pers;
    }

    public boolean NonDuplicationEmailPhone()  {
        int nrRecords = 0;
        try {
            String q1 = "SELECT * from tab1 where email=? or phone=?";
            pst = connection.prepareStatement(q1);
            pst.setString(1, emailField.getText());
            pst.setString(2, phoneField.getText());
            ResultSet rs = pst.                                                                                                                 executeQuery();
            while (rs.next()) {
                nrRecords ++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if(nrRecords >= 1) {
            return false;
        }
        return true;
    }

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

    public void alertWindow(int index) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        if(index==0){
            alert.setContentText("You have not filled in all the fields!");
        }else if(index==1){
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
