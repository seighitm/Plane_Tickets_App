package org.project.app.Worker;

import java.net.URL;
import org.project.app.LogIn_SignUp.LoginController;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.fxml.FXML;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.input.MouseEvent;

import java.util.ResourceBundle;

public class HomeWorkerController implements  Initializable{

    LoginController loginController = new LoginController();

    @FXML
    private AnchorPane home_page;

    @FXML
    private AnchorPane main_page;

    @FXML
    private Button exit_button;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    @FXML
    void add_flight(MouseEvent event) throws IOException {
        setPage(main_page, "/Worker/AddFlight.fxml");
    }

    @FXML
    void news(MouseEvent event) throws IOException {
        setPage(main_page, "/Worker/AddNews.fxml");
    }

    @FXML
    void view_flights(MouseEvent event) throws IOException {
        setPage(home_page, "/Worker/ViewUpdateDeleteFlights.fxml");
    }

    @FXML
    void cancel_ticket(MouseEvent event) throws IOException {
        setPage(main_page, "/Page.fxml");
    }

    @FXML
    public void exit(MouseEvent mouseEvent) throws IOException {
        if (allert_window(1)){
            setPage(home_page, "/LogIn_SignUp/LogIn.fxml");
        }
        loginController.setAutomationLogin(2);
    }

    public void setPage(AnchorPane page, String patch) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource(patch));
        page.getChildren().setAll(pane);
    }

    public boolean allert_window(int index)
    {
        if(index==1)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Press the \"Ok\" button if you want to exit, otherwise press the \"Cancel\" button.");
            alert.setContentText("Do you want to go out?");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("image/alert.png"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                return true;
        }
        return false;
    }
}

