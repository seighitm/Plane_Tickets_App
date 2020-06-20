package org.project.app.LogIn_SignUp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;

public class LoginController extends abstractRegistration implements Initializable {

    @FXML
    public CheckBox checkRememberPassword;

    private static String tempUsername;
    private static String tempUserEmail;
    public static int nonLoggedUser;
    private static int automaticLoginAlertMessage;
    private static int automaticLogin;
    private static int tempAccountType;
    private String patchFromHomeScreen;
    private int tempNrAccount;

    public String getTempUserName() {
        return tempUsername;
    }

    public String getTempUserEmail() {
        return tempUserEmail;
    }

    public void setAutomationLogin(int automationLogin) {
        this.automaticLogin = automationLogin;
    }

    public void setTempUserName(String tempUsername) {
        this.tempUsername =tempUsername;
    }

    public void setTempUserEmail(String tempUserEmail) {
        this.tempUserEmail=tempUserEmail;
    }

    public int getAccountType() {
        return tempAccountType;
    }

    public int getNonLoggedUser() {
        return nonLoggedUser;
    }

    File file = new File(System.getProperty("user.home") + File.separatorChar + "myConfig");

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        readFile();
        nonLoggedUser=0;
        checkRememberPassword.setSelected(true);
        if(!emailField.getText().isEmpty() && !passwordField.getText().isEmpty() &&  automaticLogin == 0) {
            automaticLoginAlertMessage = 1;
            logging();
            automaticLogin++;
        }
    }

    @FXML
    void loginButton(){
        automaticLoginAlertMessage =0;
        logging();
    }

    public void logging() {
        setAutomationLogin(1);
        if(!emailField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            verificationCredentials(emailField.getText(), passwordField.getText());
            if (tempNrAccount == 1) {
                if(tempAccountType >0 && tempAccountType <4) {
                    writeFile();
                    setPage(anchorPane, patchFromHomeScreen);
                }else if(automaticLoginAlertMessage !=1){
                    alertWindow(2);
                }
            } else if(automaticLoginAlertMessage !=1){
                alertWindow(3);
            }
        }else if(automaticLoginAlertMessage !=1){
            alertWindow(4);
        }
    }

    public String verificationCredentials(String email, String password) {
        createConnection();
        try {
            String q1 = "SELECT * from tab1 where email=? and pass=?";
            pst = connection.prepareStatement(q1);
            pst.setString(1, email);
            setTempUserEmail(email);
            pst.setString(2, getMd5(getMd5(getMd5(getMD50() + password) + getMd5(password + getMD51()))));
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    setTempUserName(rs.getString("name"));
                    tempAccountType = rs.getInt("pers");
                    getPatchFromHomeScreen(tempAccountType);
                    tempNrAccount++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if(tempNrAccount >0)
            return email+"_"+password+"_" +tempAccountType;
        else
            return null;
    }

    public void getPatchFromHomeScreen(int type) {
        if(type==1) {
            patchFromHomeScreen = "/Worker/HomeWorker.fxml";
        } else if (type==2) {
            patchFromHomeScreen = "/Client/HomeCustomer.fxml";
        } else if (type==3) {
            patchFromHomeScreen = "/Admin/HomeAdmin.fxml";
        }
    }

    @FXML
    void signUp() {
        setPage(anchorPane, "/LogIn_SignUp/SignUp.fxml");
    }

    @FXML
    void skip() {
        setPage(anchorPane, "/NonLoggedUser/HomeNonLoggedUser.fxml");
        nonLoggedUser = 1;
    }

    public void writeFile() {
        if(checkRememberPassword.isSelected()) {
            try {
                if(!file.exists()) file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
                bw.write(emailField.getText());
                bw.newLine();
                bw.write(passwordField.getText());
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            BufferedWriter bw;
            try {
                bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
                bw.newLine();
                bw.newLine();
                bw.newLine();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean readFile() {
        try {
            if (file.exists() && file.length() != 0) {
                Scanner scan = new Scanner(file);
                emailField.setText(scan.nextLine());
                passwordField.setText(scan.nextLine());
                scan.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}