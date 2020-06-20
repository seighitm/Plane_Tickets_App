package org.project.app.Worker;

import java.net.URL;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import java.sql.Connection;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import org.project.app.Connection.DBHandler;
import org.project.app.abstractClass;

import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class AddNewsController extends abstractClass implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    public TextField titleField;
    @FXML
    public TextArea textField;
    @FXML
    public Pane successAdded;

    public DBHandler handler;
    public PreparedStatement pst;
    public Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        successAdded.setVisible(false);
    }

    public void createConnection(){
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    @FXML
    public void addNews() {
        createConnection();
        String insert = "INSERT INTO news(title, text)" + "Values(?,?)";
        if((!textField.getText().isEmpty() || !titleField.getText().isEmpty()) && verificationNewsDuplication(titleField.getText(), textField.getText())) {
            try {
                pst = connection.prepareStatement(insert);
                pst.setString(1, titleField.getText());
                pst.setString(2, textField.getText());
                pst.executeUpdate();
                PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
                visiblePause.setOnFinished(event1 -> successAdded.setVisible(false));
                successAdded.setVisible(true);
                visiblePause.play();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(verificationNewsDuplication(titleField.getText(), textField.getText())==false){
            alertWindow(16);
        }else {
            alertWindow(15);
        }
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public boolean verificationNewsDuplication(String title, String text) {
        int count = 0;
        String select = "SELECT * FROM news WHERE title=? and text=?";
            try {
                pst = connection.prepareStatement(select);
                pst.setString(1, title);
                pst.setString(2, text);
                try(ResultSet rs1 = pst.executeQuery()) {
                    while (rs1.next()) {
                        count=count+1;
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if(count>0)
                return false;
            else
                return  true;
    }

    @FXML
    void viewNews() {
        try {
            AnchorPane pane = FXMLLoader.load(getClass().getResource("/Client/GenInfo/News.fxml"));
            anchorPane.getChildren().setAll(pane);
        } catch ( IOException throwables) {
            throwables.printStackTrace();
        }
    }
}
