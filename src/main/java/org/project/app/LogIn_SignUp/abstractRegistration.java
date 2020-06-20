package org.project.app.LogIn_SignUp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.project.app.Connection.DBHandler;
import org.project.app.abstractGeneral;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

abstract class abstractRegistration extends abstractGeneral {

    @FXML
    protected AnchorPane anchorPane;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passwordField;

    private final static String md5Code0 = "123";
    private final static String md5Code1 = "456";

    public String getMD50()
    {
        return md5Code0;
    }
    public String getMD51()
    {
        return md5Code1;
    }

    protected DBHandler handler;
    protected Connection connection;
    protected PreparedStatement pst;

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

    public boolean createConnection() {
        handler = new DBHandler();
        connection = handler.getConnection();
        if(connection!=null)
            return true;
        else
            return false;
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
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
}
