package org.project.app.LogIn_SignUp;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.util.Duration;
import org.project.app.Connection.DBHandler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpController extends abstractRegistration implements Initializable{

    @FXML
    public TextField usernameField;
    @FXML
    public TextField emailField;
    @FXML
    public TextField phoneField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public RadioButton workerRadioButton;
    @FXML
    public RadioButton customerRadioButton;
    @FXML
    public  Label successfulRegistrationLabel;

    private String passwordShow;

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
        if(nonDuplicationEmailPhone() && matcher1.matches() && matcher0.matches() && getTypeAccount()!=0) {
            String insert = "Insert INTO tab1(name, pass, email, pers, phone)" + "Values(?,?,?,?,?)";
            try {
                pst = connection.prepareStatement(insert);
                pst.setString(1, usernameField.getText());
                pst.setString(2, getMd5(getMd5(getMd5(getMD50()+ passwordField.getText()) + getMd5(passwordField.getText()+getMD51()))));
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
            alertWindow(5);
        } else if(!matcher1.matches()){
            alertWindow(6);
        } else if(!matcher0.matches()) {
            alertWindow(7);
        } else if(!nonDuplicationEmailPhone()) {
            alertWindow(8);
        }
    }

    public int getTypeAccount() {
        int pers = 0;
        if(workerRadioButton.isSelected()) {
            pers = -1;
        } else if (customerRadioButton.isSelected()) {
            pers = -2;
        } else {
            alertWindow(9);
        }
        return pers;
    }

    public boolean nonDuplicationEmailPhone()  {
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
}
