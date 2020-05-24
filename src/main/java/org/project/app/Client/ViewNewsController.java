package org.project.app.Client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ViewNewsController implements Initializable {

    LoginController log = new LoginController();

    @FXML
    private AnchorPane viewNews_page;

    @FXML
    private DBHandler handler;
    @FXML
    private PreparedStatement pst;
    @FXML
    private Connection connection;

    @FXML
    private Label id_notFound;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        handler = new DBHandler();
        connection = handler.getConnection();
        id_notFound.setVisible(false);
        ref();
    }

    void ref()
    {
        try {
            VBox contAll = new VBox();
            contAll.setPrefSize(550, 600);
            VBox contButton = new VBox();
            contButton.setAlignment(Pos.CENTER);
            contButton.setPrefHeight(50);
            Button button = new Button("Add");
            if(log.getPers()==1) {
                button.setPrefSize(100, 25);
                button.setOnAction(event -> {
                    AnchorPane pane = null;
                    try {
                        pane = FXMLLoader.load(getClass().getResource("/FXML/Worker/AddNews.fxml"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    viewNews_page.getChildren().setAll(pane);
                });
                contButton.getChildren().setAll(button);
                contAll.getChildren().addAll(contButton);
            }

            pst = connection.prepareStatement("SELECT * from news order by id desc");
            ResultSet rs1 = pst.executeQuery();
            int count = 0;
            while(rs1.next()) {
                count++;
                VBox temp = new VBox();
                TitledPane t3 = new TitledPane();
                t3.setText(rs1.getString("title"));
                temp.setAlignment(Pos.CENTER);
                TextArea text1 = new TextArea(rs1.getString("text"));
                text1.setPrefSize(550, 100);
                text1.setWrapText(true);
                text1.setEditable(false);
                temp.getChildren().add(text1);
                if(log.getPers()==1){
                    Button but = new Button("Delete");
                    but.setOnAction(event -> {
                        String insert = "DELETE FROM news WHERE title=?";
                        try {
                            pst = connection.prepareStatement(insert);
                            pst.setString(1, t3.getText());
                            pst.execute();
                            ref();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    });
                    temp.getChildren().addAll(but);
                }
                t3.setContent(temp);
                t3.setPadding(new Insets(5));
                t3.setExpanded(false);
                t3.prefWidthProperty().bind(contAll.widthProperty());
                contAll.getChildren().addAll(t3);
            }
            if(count==0)
            {
                id_notFound.setVisible(true);
            }
            else
                viewNews_page.getChildren().setAll(contAll);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}