package org.project.app;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public abstract class abstractGeneral extends abstractClass{

    @FXML
    protected AnchorPane anchorPane;
    @FXML
    protected ImageView minimizeCloseIcon;

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
