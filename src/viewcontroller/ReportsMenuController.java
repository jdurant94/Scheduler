/* 
 * 
 * 
 * 
 */
package viewcontroller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import static java.util.Calendar.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import utils.DataProvider;
import utils.Query;


/**
 * FXML Controller class
 *
 * @author Jordan Durant
 */
public class ReportsMenuController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private Button mainMenuBtn;
    @FXML
    private Button byMonthBtn;
    @FXML
    private Button scheduleBtn;
    @FXML
    private TextArea reportTxt;
    private PrintStream ps;
    
    ArrayList types = new ArrayList();
    
    private static ObservableList<User> appointmentsByMonth = FXCollections.observableArrayList();
    @FXML
    private Button activeConsultantsBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ps = new PrintStream(new Console(reportTxt));
    }    

    @FXML
    private void toMainMenu(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/viewcontroller/MainMenuWeekly.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        
    }

    @FXML
    private void byMonthReport(MouseEvent event) throws ParseException {

        System.setOut(ps);
        System.setErr(ps);
                
        System.out.println("-----January-----");
        printMonth(Month.JANUARY);
        System.out.println("-----Feburary-----");
        printMonth(Month.FEBRUARY);
        System.out.println("-----March-----");
        printMonth(Month.MARCH);
        System.out.println("-----April-----");
        printMonth(Month.APRIL);
        System.out.println("-----May-----");
        printMonth(Month.MAY);
        System.out.println("-----June-----");
        printMonth(Month.JUNE);
        System.out.println("-----July-----");
        printMonth(Month.JULY);
        System.out.println("-----August-----");
        printMonth(Month.AUGUST);
        System.out.println("-----September-----");
        printMonth(Month.SEPTEMBER);
        System.out.println("-----October-----");
        printMonth(Month.OCTOBER);
        System.out.println("-----November-----");
        printMonth(Month.NOVEMBER);
        System.out.println("-----December-----");
        printMonth(Month.DECEMBER);

        
    }

    @FXML
    private void scheduleReport(MouseEvent event) throws SQLException {
        
        System.setOut(ps);
        System.setErr(ps);
        
        Query.makeQuery("Select * from UserSchedule ORDER BY userId ASC, start ASC");
        ResultSet result = Query.getResult();
        
        while (result.next())
        {
            System.out.println("ID:  " + result.getString(1) + "  Name:  " + result.getString(2) + "  Meeting Type:  " + result.getString(3) + "  From:  " + result.getString(4) + "  To:  " + result.getString(5));
        }
        
    }

    @FXML
    private void activeConsultantReport(MouseEvent event) {
        
        System.setOut(ps);
        System.setErr(ps);
        
        System.out.println("-------All Active Users-------");
        
        for (User user: DataProvider.getAllUsers())
        {
            if (user.isActive())
                System.out.println(user.getUserName());
        }
        
    }
    
    

    private void printMonth (Month month){
        
        
        for (Appointment appointment : DataProvider.getAllAppointments())
        {
            String startTime = appointment.getStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDate date = LocalDate.parse(startTime, formatter);     

            if (date.getMonth() == month && (date.isAfter(LocalDate.now()) && date.isBefore(LocalDate.now().plusYears(1))))
            {
                types.add(appointment.getType());
            }
            
           
            
        }

            Set<String> typeCount = new HashSet<String>(types);
            for (String temp : typeCount) {
		System.out.println(temp + ": " + Collections.frequency(types, temp));
	}
        
        types.clear();
    }
    
    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }
}
 

