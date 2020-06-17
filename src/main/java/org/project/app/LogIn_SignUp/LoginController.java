package org.project.app.LogIn_SignUp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.image.ImageView;
import org.project.app.Connection.DBHandler;

import java.io.FileWriter;
import java.io.BufferedWriter;
import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;

public class LoginController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private CheckBox checkRememberPassword;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ImageView minimizeCloseIcon;

    private final static String md5Code0 = "123";
    private final static String md5Code1 = "456";
    private static String tempUsername;
    private static String tempUserEmail;
    public static int nonLoggedUser;
    private static int automaticLoginAlertMessage;
    private static int automaticLogin;
    private static int tempAccountType;
    private String patchFromHomeScreen;
    private int tempNrAccount;

    public String getMD50()
    {
        return md5Code0;
    }

    public String getMD51()
    {
        return md5Code1;
    }

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

    public int getPers() {
        return tempAccountType;
    }

    public int getNonLoggedUser() {
        return nonLoggedUser;
    }

    private DBHandler handler;
    private Connection connection;
    private PreparedStatement pst;

    File file = new File(System.getProperty("user.home") + File.separatorChar + "myConfig");

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        readFile();
        nonLoggedUser=0;
        checkRememberPassword.setSelected(true);
        if(!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty() &&  automaticLogin == 0) {
            automaticLoginAlertMessage = 1;
            logging();
            automaticLogin++;
        }
    }

    //creating connections with the database
    public boolean createConnection() {
        handler = new DBHandler();
        connection = handler.getConnection();
        if(connection!=null)
            return true;
        else
            return false;
    }

    @FXML
    void loginButton(){
        automaticLoginAlertMessage =0;
        logging();
    }

    public void logging() {
        setAutomationLogin(1);
        if(!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            verificationCredentials(usernameField.getText(), passwordField.getText());
            if (tempNrAccount == 1) {
                if(tempAccountType >0 && tempAccountType <4) {
                    writeFile();
                    setPage(anchorPane, patchFromHomeScreen);
                }else if(automaticLoginAlertMessage !=1){
                    alertWindows(1);
                }
            } else if(automaticLoginAlertMessage !=1){
                alertWindows(2);
            }
        }else if(automaticLoginAlertMessage !=1){
            alertWindows(3);
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

    public String getMd5(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
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

    public void setPage(AnchorPane page, String patch) {
        AnchorPane pane = null;
        try {
            pane = FXMLLoader.load(getClass().getResource(patch));
        } catch (IOException e) {
            e.printStackTrace();
        }
        page.getChildren().setAll(pane);
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

    public void writeFile() {
        if(checkRememberPassword.isSelected()) {
            try {
                if(!file.exists()) file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
                bw.write(usernameField.getText());
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
                usernameField.setText(scan.nextLine());
                passwordField.setText(scan.nextLine());
                scan.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void alertWindows(int index) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
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