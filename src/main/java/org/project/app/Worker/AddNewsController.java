package org.project.app.Worker;

import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import java.util.ResourceBundle;

public class AddNewsController implements Initializable {
    
    @FXML
    private AnchorPane field_page;

    @FXML
    private TextField field_title;

    @FXML
    private TextArea field_text;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void add_news(MouseEvent event) {

    }

    @FXML
    void view_news(MouseEvent event) {

    }
}
