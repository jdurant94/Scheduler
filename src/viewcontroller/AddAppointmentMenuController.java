/* 
 * 
 * 
 * 
 */
package viewcontroller;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import static java.time.ZoneOffset.UTC;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import utils.DataProvider;
import utils.Query;

/**
 * FXML Controller class
 *
 * @author Jordan Durant
 */
public class AddAppointmentMenuController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private Button cancelBtn;
    @FXML
    private TextField customerNameTxt;
    @FXML
    private Button addBtn;
    @FXML
    private TextField meetingTypeTxt;
    @FXML
    private TextField startTimeTxt;
    @FXML
    private TextField endTimeTxt;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    
    boolean schedule = true;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO    
        
    }   
    
    public class ScheduleConflict extends Exception {
        
    }

    @FXML
    private void cancelAdd(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/viewcontroller/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
    }


    @FXML
    private void addAppointment(MouseEvent event) throws SQLException, IOException, ParseException {
        
        try
        {
        LocalTime dayStart = LocalTime.of(8, 0);
        LocalTime dayEnd = LocalTime.of(16, 0);
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        int customerId = 0;
        int appointmentId = DataProvider.getAllAppointments().size() + 1;    
        String customerName = customerNameTxt.getText();
        String meetingType = meetingTypeTxt.getText();
        String startDateString = startDatePicker.getValue().toString();
        String startTimeString = startTimeTxt.getText();         
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalTime startTime = LocalTime.parse(startTimeString);
        LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
        ZonedDateTime startDateTimeZoned = startDateTime.atZone(zoneId);
        ZonedDateTime startDateTimeUTC = startDateTimeZoned.withZoneSameInstant(UTC);
        String start = startDateTimeUTC.format(formatter);
        String endDateString = endDatePicker.getValue().toString();
        String endTimeString = endTimeTxt.getText();            
        LocalDate endDate = LocalDate.parse(endDateString);
        LocalTime endTime = LocalTime.parse(endTimeString);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
        ZonedDateTime endDateTimeZoned = endDateTime.atZone(zoneId);
        ZonedDateTime endDateTimeUTC = endDateTimeZoned.withZoneSameInstant(UTC);
        String end = endDateTimeUTC.format(formatter);

        
        if ((startTime.isBefore(dayStart) || startTime.isAfter(dayEnd))) //(endTime.isAfter(dayEnd) || endTime.isBefore(dayEnd))) //((startTime.isBefore(dayStart) || startTime.isAfter(dayEnd)) || (endTime.isAfter(dayEnd) || endTime.isBefore(dayEnd)))
            schedule = false;
 
        
        if ((endTime.isAfter(dayEnd) || endTime.isBefore(dayStart))) //((startTime.isBefore(dayStart) || startTime.isAfter(dayEnd)) || (endTime.isAfter(dayEnd) || endTime.isBefore(dayEnd)))
            schedule = false;
        
        for (Appointment appointmentToTest : DataProvider.getAllAppointments())
        {
            String startTemp = appointmentToTest.getStart();
            LocalDateTime startTempTime = LocalDateTime.parse(startTemp, formatter);
            ZonedDateTime existingStart = startTempTime.atZone(zoneId);
            String endTemp = appointmentToTest.getEnd();
            LocalDateTime endTempTime = LocalDateTime.parse(endTemp, formatter);
            ZonedDateTime existingEnd = endTempTime.atZone(zoneId);
            
            ZonedDateTime pendingStart = startDateTimeZoned;
            ZonedDateTime pendingEnd = endDateTimeZoned;
         
            
            if ((pendingStart.isAfter(existingStart) && pendingStart.isBefore(existingEnd)))  //|| (endDateTimeZoned.isBefore(endTempZoned) || endDateTimeZoned.isAfter(startTempZoned))
                throw new ScheduleConflict();
            
            if ((pendingEnd.isAfter(existingStart) && pendingEnd.isBefore(existingEnd)))  //|| (endDateTimeZoned.isBefore(endTempZoned) || endDateTimeZoned.isAfter(startTempZoned))
                throw new ScheduleConflict();
            
            if ((pendingStart.isBefore(existingStart) && pendingEnd.isAfter(existingEnd)))  //|| (endDateTimeZoned.isBefore(endTempZoned) || endDateTimeZoned.isAfter(startTempZoned))
                throw new ScheduleConflict();
}

        
        if (schedule == true)
        {
            Query.makeQuery("select customerId from customer where customerName = '" + customerName + "'");
            ResultSet result = Query.getResult();

            while (result.next())
            {
                customerId = result.getInt(1);
            }

            if (customerId == 0) 
            {

                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Entry");
                alert.setHeaderText("Customer Doesn't Exist");
                alert.setContentText("Please Enter an existing Customer");

                Optional<ButtonType> alertResult = alert.showAndWait();
                if (alertResult.get() == ButtonType.OK)
                {
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/viewcontroller/AddAppointmentMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show(); 
                }


                System.out.println("no customer");

            }  
            else
            {
                Query.makeQuery("insert into appointment(customerId,userId,title,description,location,contact,type,url,start,end,createDate,createdBy,lastUpdateby) values('" 
                        + customerId + "','" + 1 + "','not need','not needed','not needed','not needed','" + meetingType + "','not needed','" + start 
                        + "','" + end + "','" + LocalDateTime.now(UTC).toString() + "','" + LoginScreenController.currentUser.getUserName() + "','" + LoginScreenController.currentUser.getUserName() + "')");

                Query.makeQuery("SELECT LAST_INSERT_ID()");
                ResultSet nextResult = Query.getResult();
                while (nextResult.next())
                {
                    appointmentId = nextResult.getInt(1);
                    System.out.println(appointmentId);
                }

                DataProvider.addAppointment(new Appointment (appointmentId, customerName, meetingType, startDateTime.format(formatter).toString(), endDateTime.format(formatter).toString()));

            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/viewcontroller/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();

            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Times");
            alert.setHeaderText("Appointment would be outside Business Hours");
            alert.setContentText("Please reschedule appointment between 08:00:00 and 16:00:00 ");
            
            Optional<ButtonType> alertResult = alert.showAndWait();
            if (alertResult.get() == ButtonType.OK)
            {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/viewcontroller/addAppointmentMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
        }
        }
        
        
        
        
    
        
    }  catch (ScheduleConflict e){
        
        Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Times");
                alert.setHeaderText("Appointment Conflict");
                alert.setContentText("Please reschedule appointment to an open time");
            
                Optional<ButtonType> alertResult = alert.showAndWait();
                if (alertResult.get() == ButtonType.OK)
                {
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/viewcontroller/addAppointmentMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show(); 
    }  
    }
    }
}
