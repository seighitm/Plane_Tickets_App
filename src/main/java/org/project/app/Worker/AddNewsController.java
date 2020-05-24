package org.project.app.Worker;

import java.net.URL;
import javafx.fxml.FXML;
import java.sql.Connection;
import java.io.IOException;
import java.sql.SQLException;
import org.project.app.Connection.DBHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.PreparedStatement;
import java.util.ResourceBundle;

public class AddNewsController implements Initializable {

    @FXML
    private AnchorPane field_page;

    @FXML
    private TextField field_title;

    @FXML
    private TextArea field_text;

    @FXML
    private DBHandler handler;

    @FXML
    private PreparedStatement pst;

    @FXML
    private Connection connection;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
    }

    @FXML
    void add_news(MouseEvent event) {
        String insert = "Insert INTO news(title, text)" + "Values(?,?)";
        if(!field_text.getText().isEmpty() && !field_title.getText().isEmpty()) {
            try {
                pst = connection.prepareStatement(insert);
                pst.setString(1, field_title.getText());
                pst.setString(2, field_text.getText());
                pst.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }else {
            allertWindow(1);
        }
    }

    @FXML
    void view_news(MouseEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("/Client/GenInfo/News.fxml"));
        field_page.getChildren().setAll(pane);
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void allertWindow(int index)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(null);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alertt.png"));
        if(index==1) {
            alert.setContentText("You have not filled in all the fields!");
        }
        alert.show();
    }
}
