package org.project.app.LogIn_SignUp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import org.project.app.Connection.DBHandler;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import java.io.File;
import java.io.IOException;

public class LoginController extends Window implements Initializable {

    private String md5_code0 = "123";
    private String md5_code1 = "456";

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

    public int getPers() {
        return pers;
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

    File file = new File(System.getProperty("user.home") + File.separatorChar + "myConfig");

    public static int aux = 0;
    public static int automation_login=0;

    public void setAutomation_login(int automation_login) {
        this.automation_login = automation_login;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        handler = new DBHandler();
        connection = handler.getConnection();
        readFile();
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
                pst.setString(2, getMd5(getMd5(getMd5(md5_code0 + password_field.getText()) + getMd5(password_field.getText() + md5_code1))));
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
                        writeFile();
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
        if(worker_button.isSelected()) {
            pers = 1;
            patch = "/Worker/HomeWorker.fxml";
        } else if (customer_button.isSelected()) {
            pers = 2;
            patch = "/Client/HomeCustomer.fxml";
        } else if (!customer_button.isSelected() && !worker_button.isSelected()) {
            pers = 3;
            patch = "/Admin/HomeAdmin.fxml";
        }
        return pers;
    }

    public String getMd5(String input)
    {
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
    void signUp(MouseEvent mouseEvent) throws IOException {
        setPage(login_page, "/LogIn_SignUp/SignUp.fxml");
    }

    @FXML
    void skip(MouseEvent event) throws IOException  {
        setPage(login_page, "/NonLoggedUser/HomeNonLoggedUser.fxml");
    }

    public void setPage(AnchorPane page, String patch) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(patch));
        page.getChildren().setAll(pane);
    }

    @FXML
    void close(MouseEvent event) {
        Stage stage = (Stage) min_close.getScene().getWindow();
        writeFile();
        stage.close();
    }

    @FXML
    void minimize() {
        Stage stage = (Stage) min_close.getScene().getWindow();
        stage.setIconified(true);
    }

    public void writeFile() {
        if(check_remember.isSelected())
        {
            try {
                if(!file.exists()) file.createNewFile();
                BufferedWriter bw = new BufferedWriter(new FileWriter(file.getAbsolutePath()));
                bw.write(username_field.getText());
                bw.newLine();
                bw.write(password_field.getText());
                bw.newLine();
                bw.write(String.valueOf(getAccount()));
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            BufferedWriter bw = null;
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
                username_field.setText(scan.nextLine());
                password_field.setText(scan.nextLine());
                String u = scan.nextLine();
                if (u.equals("1")) {
                    worker_button.setSelected(true);
                } else if (u.equals("2")) {
                    customer_button.setSelected(true);
                }
                scan.close();
                return true;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void allertWindow(int index)
    {
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