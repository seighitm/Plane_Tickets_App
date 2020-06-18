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
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.project.app.Connection.DBHandler;
import org.project.app.Model.ModelViewFlight;

import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class AddNewsController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private TextField titleField;
    @FXML
    private TextArea textField;
    @FXML
    private ImageView minimizeCloseIcon;
    @FXML
    private Pane succesAdded;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        succesAdded.setVisible(false);
    }

    @FXML
    void addNews() {
        String insert = "INSERT INTO news(title, text)" + "Values(?,?)";
        if((!textField.getText().isEmpty() || !titleField.getText().isEmpty()) && verificationNewsDuplication(titleField.getText(), textField.getText())) {
            try {
                pst = connection.prepareStatement(insert);
                pst.setString(1, titleField.getText());
                pst.setString(2, textField.getText());
                pst.executeUpdate();
                PauseTransition visiblePause = new PauseTransition(Duration.seconds(2));
                visiblePause.setOnFinished(event1 -> succesAdded.setVisible(false));
                succesAdded.setVisible(true);
                visiblePause.play();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else if(verificationNewsDuplication(titleField.getText(), textField.getText())==false){
            alertWindow(2);
        }else {
            alertWindow(1);
        }
    }

    boolean verificationNewsDuplication(String title, String text) {
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
            connection.close();
        } catch (SQLException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    public void alertWindow(int index)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alert.png"));
        if(index==1) {
            alert.setContentText("You have not filled in all the fields!");
        } else if(index==2) {
            alert.setContentText("The news already exists!");
        }
        alert.show();
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
}
