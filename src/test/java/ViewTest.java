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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static org.junit.Assert.*;

public class ViewTest extends ApplicationTest {
/*
    ViewNewsController viewNewsController = new ViewNewsController();
    AddFlightsController addFlightsController = new AddFlightsController();
    AddNewsController addNewsController = new AddNewsController();
    LoginController loginController = new LoginController();
    ViewFlightsController viewFlightsController = new ViewFlightsController();
    HomeAdmin homeAdminPage = new HomeAdmin();
    ViewUpdateDeleteFlightsController viewUpdateDeleteFlightsController = new ViewUpdateDeleteFlightsController();

    ModelViewFlight modelViewFlight = new ModelViewFlight(1, "B", "C", "2021-07-07", 2, "2:26", 8);

    @Before
    public void BeforeTest() {
        //DBHandler handler = new DBHandler();
        //String connectionString = "jdbc:mysql://" +"localhost" + ":" + "3306" + "/" + "test"+"?useUnicode=true&serverTimezone=UTC&useSSL=false&autoReconnect=true";
        //handler.setConnectionString(connectionString);
        System.out.println("<Before testing>");
    }

    @Test
    public void testConnection() {
        assertNotNull(loginController.createConnection());
    }

    @Test
    public void testExistFlight(){
        int nrRecords = 0;
        viewFlightsController.createConnection();
        ObservableList<ModelViewFlight> flights = viewFlightsController.readFromDataBase();
        for(ModelViewFlight x : flights){
            if(x.getDestination().equals(modelViewFlight.getDestination()) &&
                    x.getLocation().equals(modelViewFlight.getLocation()) &&
                    x.getDate().equals(modelViewFlight.getDate()) &&
                    x.getID()==modelViewFlight.getID() &&
                    x.getHour().equals(modelViewFlight.getHour()) &&
                    x.getSeats()==modelViewFlight.getSeats()){
                nrRecords++;
            }
        }
        if(flights.size()!=0)
            assertEquals(nrRecords, 1);
    }

    @Test
    public void testViewMyTicket(){
        MyTicketController myTicketController = new MyTicketController();
        myTicketController.createConnection();
        ObservableList<ModelViewFlight> tickets = myTicketController.readFromDataBase("8", "8@m.ru");
        int count = 0;
        for(ModelViewFlight x : tickets)
            if(x.getID()==1) {
                count++;
            }

        if(tickets.size()!=0)
            assertEquals(true, count>0);
    }

    @Test
    public void testAccountDuplicate() {
        ObservableList<ModelAccount> accounts = homeAdminPage.readFromDatabase();
        homeAdminPage.closeConnection();
        int count = 0;
        int size = accounts.size();
        for(ModelAccount x : accounts)
            for(ModelAccount y : accounts)
                if(x.getEmail().equals(y.getEmail()) || x.getPhone()==y.getPhone())
                    count++;
        homeAdminPage.closeConnection();
        assertEquals(size, count);
    }
    @Test
    public void testNullFlights() {
        ObservableList<ModelViewFlight> flights = viewUpdateDeleteFlightsController.refresh_automatic();
        assertNotNull(flights);
    }

    @Test
    public void testValidAccount() {
        String email = "8@m.ru";
        String password = "8";
        int account = 1;
        String type="";
        type = loginController.verificationCredentials(email, password);
        if(type!=null)
            assertEquals(type, email+"_"+password+"_"+account);
    }

    @Test
    public void testIfPasswordEncrypted(){
        String message = "test";
        String stringEncoder1 = loginController.getMd5(message);
        assertNotEquals(message, stringEncoder1);
    }

    @Test
    public void testBuyTickets(){
        String email = "8@m.ru";
        String username = "8";
        int flightID = 62;
        loginController.setTempUserEmail(email);
        loginController.setTempUserName(username);
        viewFlightsController.addMyFlight(flightID);
        viewFlightsController.closeConnection();
    }

    @Test
    public void testCancelTicket(){
        int transactionID = 5;
        String userID = "0";
        String flightID = "62";
        String date = "2020-10-10";
        ViewTicketsController viewTicketsController = new ViewTicketsController();
        viewTicketsController.createConnection();
        ModelTicket modelTicket = new ModelTicket(transactionID, userID, flightID, date);
        viewTicketsController.cancelTicket(modelTicket);
        viewTicketsController.closeConnection();
    }

    String title = "1223";
    String text = "4556";

    @Test
    public void testAddNews(){
        Platform.runLater(
                () -> {
                    addNewsController.createConnection();
                    addNewsController.successAdded = new Pane();
                    addNewsController.textField = new TextArea();
                    addNewsController.titleField = new TextField();

                    addNewsController.titleField.setText(title);
                    addNewsController.textField.setText(text);
                    boolean Empty = addNewsController.verificationNewsDuplication(title, text);
                    addNewsController.addNews();
                    boolean notEmpty = addNewsController.verificationNewsDuplication(title, text);
                    addNewsController.closeConnection();
                    assertNotEquals(notEmpty, Empty);
                }
        );
    }

    @Test
    public void testAddDeleteNews(){
        Platform.runLater(
                () -> {
                    addNewsController.successAdded = new Pane();
                    addNewsController.textField = new TextArea();
                    addNewsController.titleField = new TextField();

                    String title = "colea";
                    String text = "colea";
                    addNewsController.titleField.setText(title);
                    addNewsController.textField.setText(text);
                    addNewsController.createConnection();
                    boolean temp1 = addNewsController.verificationNewsDuplication(title, text);
                    addNewsController.addNews();
                    boolean temp2 = addNewsController.verificationNewsDuplication(title, text);
                    viewNewsController.deleteNews(title);
                    viewNewsController.closeConnection();
                    boolean temp3 = addNewsController.verificationNewsDuplication(title, text);
                    boolean temp4 = temp1==temp2;
                    addNewsController.closeConnection();
                    assertNotEquals(temp4, temp3);
                }
        );
    }


    @Test
    public void testNewsDelete(){
        Platform.runLater(
                () -> {
                    addNewsController.createConnection();
                    boolean F1 = addNewsController.verificationNewsDuplication(title, text);
                    viewNewsController.deleteNews(title);
                    boolean temp2 = addNewsController.verificationNewsDuplication(title, text);
                    assertNotEquals(F1, temp2);
                    addNewsController.closeConnection();
                }
        );
    }

    @Test
    public void testRegistrationAccount(){
        Platform.runLater(
                () -> {
                    SignUpController signUpController = new SignUpController();
                    signUpController.successfulRegistrationLabel = new Label();
                    signUpController.workerRadioButton = new RadioButton();
                    signUpController.customerRadioButton = new RadioButton();
                    signUpController.usernameField = new TextField();
                    signUpController.passwordField = new PasswordField();
                    signUpController.emailField = new TextField();
                    signUpController.phoneField = new TextField();

                    homeAdminPage.table = new TableView<>();
                    homeAdminPage.idtab1 = new TableColumn<>();
                    homeAdminPage.name = new TableColumn<>();
                    homeAdminPage.email = new TableColumn<>();
                    homeAdminPage.phone = new TableColumn<>();
                    homeAdminPage.pers = new TableColumn<>();
                    homeAdminPage.action = new TableColumn<>();

                    String username = "cent";
                    String password = "clint";
                    String email = "and4t@m.ru";
                    String phone = "104004";

                    signUpController.createConnection();
                    signUpController.workerRadioButton.setSelected(true);
                    signUpController.customerRadioButton.setSelected(false);
                    signUpController.usernameField.setText(username);
                    signUpController.passwordField.setText(password);
                    signUpController.emailField.setText(email);
                    signUpController.phoneField.setText(phone);

                    boolean T1 = signUpController.nonDuplicationEmailPhone();
                    signUpController.signUp();
                    boolean F1 = signUpController.nonDuplicationEmailPhone();
                    boolean F2 = T1==F1;
                    homeAdminPage.createConnection();
                    homeAdminPage.deleteMethod(signUpController.emailField.getText(), signUpController.phoneField.getText());
                    boolean T2 = signUpController.nonDuplicationEmailPhone();
                    homeAdminPage.closeConnection();
                    signUpController.closeConnection();
                    assertNotEquals(F2, T2);
                }
        );
    }

    @Test
    public void testAdminValidationAccount(){
        Platform.runLater(
                () -> {
                    SignUpController signUpController = new SignUpController();
                    signUpController.successfulRegistrationLabel = new Label();
                    signUpController.workerRadioButton = new RadioButton();
                    signUpController.customerRadioButton = new RadioButton();
                    signUpController.usernameField = new TextField();
                    signUpController.passwordField = new PasswordField();
                    signUpController.emailField = new TextField();
                    signUpController.phoneField = new TextField();

                    homeAdminPage.table = new TableView<>();
                    homeAdminPage.idtab1 = new TableColumn<>();
                    homeAdminPage.name = new TableColumn<>();
                    homeAdminPage.email = new TableColumn<>();
                    homeAdminPage.phone = new TableColumn<>();
                    homeAdminPage.pers = new TableColumn<>();
                    homeAdminPage.action = new TableColumn<>();

                    String username = "cl";
                    String password = "cl";
                    String email = "c74l9@m.ru";
                    String phone = "73110";

                    String workerSelected = "WORKER";
                    signUpController.createConnection();
                    signUpController.workerRadioButton.setSelected(true);
                    signUpController.customerRadioButton.setSelected(false);
                    signUpController.usernameField.setText(username);
                    signUpController.passwordField.setText(password);
                    signUpController.emailField.setText(email);
                    signUpController.phoneField.setText(phone);

                    boolean T1 = signUpController.nonDuplicationEmailPhone();
                    signUpController.signUp();
                    boolean F1 = signUpController.nonDuplicationEmailPhone();
                    boolean F2 = T1==F1;
                    if(F2==false)
                    {
                        ObservableList<ModelAccount> accounts = homeAdminPage.readFromDatabase();
                        for(ModelAccount x : accounts)
                            if(x.getEmail().equals(email) || x.getPhone().equals(phone)){
                                    homeAdminPage.validateMethod(x.getIdtab1());
                            }
                        accounts = homeAdminPage.readFromDatabase();
                        for(ModelAccount x : accounts)
                            if(x.getEmail().equals(email) || x.getPhone().equals(phone)) {
                                assertEquals(workerSelected, x.getPers());
                            }
                    }else {
                        assertEquals("ACCOUNT EXIST!", F2, false);
                    }
                        homeAdminPage.closeConnection();
                });
    }




    @Test
    public void testAddFlight(){
        Platform.runLater(
                () -> {
                    addFlightsController.createConnection();

                    addFlightsController.successAdded = new Pane();
                    addFlightsController.destinationField = new TextField();
                    addFlightsController.locationField = new TextField();
                    addFlightsController.dateField = new DatePicker();
                    addFlightsController.hoursField = new TextField();
                    addFlightsController.minutesField = new TextField();
                    addFlightsController.seatsField = new TextField();
                    addFlightsController.priceField = new TextField();

                    addFlightsController.destinationField.setText("che4sgjva");
                    addFlightsController.locationField.setText("ce4jg47da");

                    String date = "07-07-2021";
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                    addFlightsController.dateField.setValue(LocalDate.parse(date , formatter));
                    addFlightsController.hoursField.setText("2");
                    addFlightsController.minutesField.setText("26");
                    addFlightsController.seatsField.setText("9");
                    addFlightsController.priceField.setText("2");

                    boolean temp1 = addFlightsController.verificationExistingSameFlight();
                    addFlightsController.addFlights();
                    boolean temp2 = addFlightsController.verificationExistingSameFlight();
                    assertNotEquals(temp1, temp2);
                }
        );
    }

    @Test
    public void testReadWrite(){
        loginController.emailField = new TextField();
        loginController.passwordField = new PasswordField();
        loginController.checkRememberPassword = new CheckBox();
        loginController.createConnection();
        String usernamePrime = "8@m.ru";
        String passwordSecond = "8";
        loginController.emailField.setText(usernamePrime);
        loginController.passwordField.setText(passwordSecond);
        loginController.checkRememberPassword.setSelected(true);
        loginController.writeFile();
        loginController.readFile();
        String usernameSecond = loginController.emailField.getText();
        assertEquals(usernamePrime, usernameSecond);
    }

    @After
    public void AfterTest() {
        System.out.println("<After testing>");
    }

    @AfterClass
    public static void s(){
        DBHandler handler;
        PreparedStatement pst;
        Connection connection;
        handler = new DBHandler();
        connection = handler.getConnection();
        String delete1 = "DELETE FROM tab1";
        try {
            pst = connection.prepareStatement(delete1);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String delete2 = "DELETE FROM tab2";
        try {
            pst = connection.prepareStatement(delete2);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String delete3 = "DELETE FROM tab3";
        try {
            pst = connection.prepareStatement(delete3);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String delete4 = "DELETE FROM news";
        try {
            pst = connection.prepareStatement(delete4);
            pst.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
 */
}