package org.project.app.Client;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import java.sql.PreparedStatement;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;

public class ViewNewsController implements Initializable {

    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Label idNotFound;
    @FXML
    private VBox contAllVbox;
    @FXML
    private ImageView minimizeCloseIcon;

    private DBHandler handler;
    private PreparedStatement pst;
    private Connection connection;

    LoginController logincontroller = new LoginController();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        idNotFound.setVisible(false);
        viewNews();
    }

    void viewNews() {
        addDeleteButton();
        displayAllNews();
    }

    void addDeleteButton(){
        if(logincontroller.getPers()==1 && logincontroller.getNonLoggedUser()!=1) {
            VBox contButton = new VBox();
            contButton.setAlignment(Pos.CENTER);
            contButton.setPrefHeight(50);
            Button button = new Button("Add News");
            button.setPrefSize(100, 25);
            button.setOnAction(event -> {
                logincontroller.setPage(anchorPane,"/Worker/AddNews.fxml");
            });
            contButton.getChildren().setAll(button);
            contAllVbox.getChildren().addAll(contButton);
        }
    }

    void displayAllNews() {
        int nrNews = 0;
        try {
            String select = "SELECT * FROM news ORDER BY id DESC";
            pst = connection.prepareStatement(select);
            try(ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    HBox titlePaneContent = new HBox();
                    titlePaneContent.setPrefSize(540, 100);
                    TitledPane titledpane = new TitledPane();
                    titledpane.setPrefWidth(540);
                    titledpane.setText(rs.getString("title"));
                    titlePaneContent.setAlignment(Pos.CENTER);
                    TextArea textArea = new TextArea(rs.getString("text"));
                    textArea.setPrefSize(540, 150);
                    textArea.setWrapText(true);
                    textArea.setEditable(false);
                    titledpane.setPadding(new Insets(0));
                    titlePaneContent.getChildren().add(textArea);
                    nrNews = nrNews + 1;
                    if (logincontroller.getPers() == 1 && logincontroller.getNonLoggedUser() != 1) {
                        ImageView selectedImage = new ImageView("/image/tras.png");
                        titlePaneContent.getChildren().addAll(selectedImage);

                        selectedImage.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                            String delete = "DELETE FROM news WHERE title=?";
                            try {
                                pst = connection.prepareStatement(delete);
                                pst.setString(1, titledpane.getText());
                                pst.execute();
                                logincontroller.setPage(anchorPane, "/Client/GenInfo/News.fxml");
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        });

                    }
                    titledpane.setContent(titlePaneContent);
                    titledpane.setPadding(new Insets(5));
                    titledpane.setExpanded(false);
                    titledpane.prefWidthProperty().bind(contAllVbox.widthProperty());
                    contAllVbox.getChildren().addAll(titledpane);
                }
            }
            if(nrNews==0)
                idNotFound.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
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