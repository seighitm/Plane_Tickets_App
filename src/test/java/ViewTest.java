import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import org.junit.*;
import org.project.app.Admin.HomeAdmin;
import org.project.app.Client.MyTicketController;
import org.project.app.Client.ViewFlightsController;
import org.project.app.Client.ViewNewsController;
import org.project.app.Connection.DBHandler;
import org.project.app.LogIn_SignUp.LoginController;
import org.project.app.LogIn_SignUp.SignUpController;
import org.project.app.Model.ModelAccount;
import org.project.app.Model.ModelTicket;
import org.project.app.Model.ModelViewFlight;
import org.project.app.Worker.AddFlightsController;
import org.project.app.Worker.AddNewsController;
import org.project.app.Worker.ViewTicketsController;
import org.project.app.Worker.ViewUpdateDeleteFlightsController;
import org.testfx.framework.junit.ApplicationTest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.Assert.*;

public class ViewTest extends ApplicationTest {


}