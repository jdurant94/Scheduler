/* 
 * 
 * 
 * 
 */
package viewcontroller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import utils.DBConnector;
import utils.DataProvider;
import static utils.DataProvider.getAppointmentList;
import utils.Query;
import static viewcontroller.CustomerMenuController.getCustomerList;

/**
 * FXML Controller class
 *
 * @author Jordan Durant
 */
public class LoginScreenController implements Initializable  {
    
    Stage stage;
    Parent scene;

    public static User currentUser;
    
    @FXML
    private Button LoginBtn;
    @FXML
    private TextField userNameTxt;
    @FXML
    private TextField passwordTxt;
    private boolean appointmentInFifteen = false;
    Locale france = new Locale("fr", "FR");
    @FXML
    private Label titleTxt;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        // TODO
        
        //pull data from DB into lists
        try {
            Query.makeQuery("SELECT * FROM CustomerList");
            ResultSet customers = Query.getResult();
            while(customers.next())
            {
                DataProvider.addCustomer(getCustomerList(customers));
            }
        } catch (SQLException e) {
            System.out.println("Cannot pull customerlist " + e.getMessage());
        }
        
         try {
            Query.makeQuery("SELECT * FROM AllAppointments");
            ResultSet appointments = Query.getResult();
            while(appointments.next())
            {
                DataProvider.addAppointment(DataProvider.getAppointmentList(appointments));
            }
        } catch (Exception e) {
            System.out.println("Cannot pull appointmentlist " + e.getMessage());
        }
         
         try {
            Query.makeQuery("SELECT * FROM user");
            ResultSet users = Query.getResult();
            while(users.next())
            {
                DataProvider.addUser(DataProvider.getUserFromDB(users));
            }
        } catch (Exception e) {
            System.out.println("Cannot pull user list " + e.getMessage());
        }
         
         if (Locale.getDefault() == france)
         {
             titleTxt.setText("Planificateur");
             userNameTxt.setPromptText("Nom d\'utilisateur");
             passwordTxt.setPromptText("Mot de passe");
             LoginBtn.setText("S\'identifier");
         }
        
            
        
    }    

    @FXML
    private void Login(MouseEvent event) throws IOException, SQLException, ParseException {
        
        
        String logFile = "log.txt";
        String password = null;
        String userName = userNameTxt.getText();
        String selectUserName = "SELECT * FROM user WHERE userName ='" + userName + "'";
        
        Query.makeQuery(selectUserName);
        
        ResultSet result = Query.getResult();
        if (!result.isBeforeFirst() ) 
        {   
            try (FileWriter fw = new FileWriter(logFile, true)) {
                fw.write("Unsuccessful login from user:" + userName + " " + LocalDateTime.now() + " User does not exist\n");
            }
            
            if (Locale.getDefault() == france)
            {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Identifiants erronés");
            alert.setHeaderText("Erreur!");
            alert.setContentText("L'utilisateur n'existe pas");
            
            alert.showAndWait();
            }
            else
            {
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong Credentials");
            alert.setHeaderText("Error!");
            alert.setContentText("User does not exist");
            
            alert.showAndWait();
            }
            
        } 
        else
        {
            while(result.next())
            {
                password = result.getString(3);   
            }
        }
        
        if(password.equals(passwordTxt.getText()))
        {
            
            try {
            Query.makeQuery("SELECT * FROM user where userName = '" + userName + "'");
            ResultSet users = Query.getResult();
            while(users.next())
            {
                currentUser = DataProvider.getUserFromDB(users);
                
                System.out.println(currentUser.getUserId());
            }
            } catch (SQLException e) {
            System.out.println("Cannot pull user info " + e.getMessage());
            }
            
            try (FileWriter fw = new FileWriter(logFile, true)) {
                fw.write("Successful login from user:" + userName + " " + LocalDateTime.now() + "\n");
            }
            
            for (Appointment appointment : DataProvider.getAllAppointments())
            {
            String time = appointment.getStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime testTime = LocalDateTime.parse(time, formatter);
            
        System.out.println(testTime);
        
        if((testTime.isAfter(LocalDateTime.now()) && testTime.isBefore(LocalDateTime.now().plusMinutes(15))))
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Upcoming Appointment");
            alert.setHeaderText("You have an appointment in the next 15 minutes!");
            alert.setContentText("Please check your schedule");
            
            alert.showAndWait();
        }
            
            }
            
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/viewcontroller/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            
        }
        else
        {
            try (FileWriter fw = new FileWriter(logFile, true)) {
                fw.write("Unsuccessful login from user:" + userName + " " + LocalDateTime.now() + "\n");
            }
            
            if (Locale.getDefault() == france)
            {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Identifiants erronés");
            alert.setHeaderText("Error!");
            alert.setContentText("Mot de passe incorrect");
            
            alert.showAndWait();
            }
            else
            {
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Wrong Credentials");
            alert.setHeaderText("Error!");
            alert.setContentText("Incorrect Password");
            
            alert.showAndWait();
            }
            
              
        }
        
        
        
            
            
         
            
        
        
    }
    
}
