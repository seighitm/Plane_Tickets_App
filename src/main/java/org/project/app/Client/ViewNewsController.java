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
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;
import org.project.app.abstractGeneral;

public class ViewNewsController extends abstractGeneral implements Initializable {

    @FXML
    private Label idNotFound;
    @FXML
    private VBox contAllVbox;

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
        if(logincontroller.getAccountType()==1 && logincontroller.getNonLoggedUser()!=1) {
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
                    if (logincontroller.getAccountType() == 1 && logincontroller.getNonLoggedUser() != 1) {
                        ImageView selectedImage = new ImageView("/image/trass.png");
                        titlePaneContent.getChildren().addAll(selectedImage);

                        selectedImage.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_CLICKED, event -> {
                            deleteNews(titledpane.getText());
                            logincontroller.setPage(anchorPane, "/Client/GenInfo/News.fxml");
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

    public void deleteNews(String titlepaneText){
        handler = new DBHandler();
        connection = handler.getConnection();
        String delete = "DELETE FROM news WHERE title=?";
        try {
            pst = connection.prepareStatement(delete);
            pst.setString(1, titlepaneText);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection(){
        try {
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}