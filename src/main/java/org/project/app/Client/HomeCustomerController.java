package org.project.app.Client;

import org.project.app.LogIn_SignUp.LoginController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class HomeCustomerController {

    LoginController loginController = new LoginController();

    private double xOffset = 0;
    private double yOffset = 0;

    @FXML
    private AnchorPane home_page;

    @FXML
    private AnchorPane main_page;

    @FXML
    private Button exit_button;

    @FXML
    void gen_inf(MouseEvent event) throws IOException {
        setPage(home_page, "/Page.fxml");
    }

    @FXML
    void my_tickets(MouseEvent event) throws IOException {
        setPage(main_page, "/Page.fxml");
    }

    @FXML
    void view_flights(MouseEvent event) throws IOException {
        setPage(home_page, "/Page.fxml");
    }

    @FXML
    void exit(MouseEvent event) throws IOException{
        if (allert_window(1)){
            setPage(home_page, "/FXML/Sign/LoginMain.fxml");
        }
        loginController.setAutomation_login(2);
    }

    public boolean allert_window(int index)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("image/alertt.png"));
        if(index==1)
        {
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Press the \"Ok\" button if you want to exit, otherwise press the \"Cancel\" button.");
            alert.setContentText("Do you want to go out?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                return true;
        }
        return false;
    }

    public void setPage(AnchorPane page, String patch) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(patch));
        page.getChildren().setAll(pane);
    }
}
