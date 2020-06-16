package org.project.app.Worker;

import java.net.URL;
import javafx.fxml.FXML;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.project.app.Connection.DBHandler;

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

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    @FXML
    void addNews() {
        String insert = "INSERT INTO news(title, text)" + "Values(?,?)";
        if(!textField.getText().isEmpty() || !titleField.getText().isEmpty()) {
            try {
                pst = connection.prepareStatement(insert);
                pst.setString(1, titleField.getText());
                pst.setString(2, textField.getText());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            alertWindow(1);
        }
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
