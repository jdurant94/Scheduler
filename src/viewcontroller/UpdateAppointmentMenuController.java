 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package viewcontroller;

import java.io.IOException;
import utils.DataProvider;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.Duration;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Appointment;
import utils.Query;
import java.time.Instant;

/**
 * FXML Controller class
 *
 * @author JDura
 */
public class UpdateAppointmentMenuController implements Initializable {

    Parent scene;
    Stage stage;
    
    @FXML
    private Button cancelBtn;
    @FXML
    private TextField appointmentIdTxt;
    @FXML
    private TextField customerNameTxt;
    @FXML
    private TextField meetingTypeTxt;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField startTimeTxt;
    @FXML
    private TextField endTimeTxt;
    @FXML
    private Button updateBtn;
    
    private Appointment appointment;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    } 
    
//    public class Interval {
//        private Instant begin;
//        private Instant end;
//
//        public Interval(long begin, long end) {
//            this.begin = Instant.ofEpochMilli(begin);
//            this.end = Instant.ofEpochMilli(end);
//        }
//    }
//    
//    private static boolean hasOverlap(Interval t1, Interval t2) {
//        return !t1.end.isBefore(t2.begin) && !t1.begin.isAfter(t2.end);
//    }
    
    public class ScheduleConflict extends Exception {
        
    }

    @FXML
    private void cancelAdd(MouseEvent event) throws IOException {
        
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/viewcontroller/MainMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
        
    }

    
    public void setAppointment(Appointment appointment) throws IOException {
        this.appointment = appointment;
        
        appointmentIdTxt.setText(Integer.toString(appointment.getAppointmentId()));
        customerNameTxt.setText(appointment.getCustomerName());
        meetingTypeTxt.setText(appointment.getType());
        
    }

    @FXML
    private void updateAppointment(MouseEvent event) throws SQLException, IOException, ParseException, Exception {
        
        try{
        
        boolean schedule = true;
        LocalTime dayStart = LocalTime.of(8, 0);
        LocalTime dayEnd = LocalTime.of(16, 0);    
        ZoneId zoneId = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");    
        int appointmentId = appointment.getAppointmentId();
        String customerName = customerNameTxt.getText();
        int customerId = 0;
        String type = meetingTypeTxt.getText();
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


               
         
        if (schedule == true){
            
        Query.makeQuery("select customerId from customer where customerName = '" + customerName + "'");
        ResultSet result = Query.getResult();
        while (result.next())
        {
           customerId = result.getInt(1); 
        }
        if (customerId != 0) 
        {
            Query.makeQuery("UPDATE appointment SET customerId = '" + customerId + "', userId = " + LoginScreenController.currentUser.getUserId() + ", type = '" + type + "', start = '" + start + "', end = '" 
                    + end + "', lastUpdateBy = '" + LoginScreenController.currentUser.getUserName() + "' where appointmentId = " + appointmentId);
            
            DataProvider.deleteAppointment(appointment);
            DataProvider.addAppointment(new Appointment (appointmentId, customerName, type, startDateTime.format(formatter), endDateTime.format(formatter)));
            
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/viewcontroller/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            
            
        }  
        else //if (result.next() == true)
        {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setHeaderText("Customer Does not Exist");
            alert.setContentText("Please Enter an existing customer");
            
            Optional<ButtonType> alertResult = alert.showAndWait();
            if (alertResult.get() == ButtonType.OK)
            {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getClassLoader().getResource("/viewcontroller/UpdateAppointmentMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show(); 
            }
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
                scene = FXMLLoader.load(getClass().getResource("/viewcontroller/UpdateAppointmentMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
        }
            
        }
        
        }
             
        
    }
        catch (ScheduleConflict e)
        {   
            
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Times");
                alert.setHeaderText("Appointment Conflict");
                alert.setContentText("Please reschedule appointment to an open time");
            
                Optional<ButtonType> alertResult = alert.showAndWait();
                if (alertResult.get() == ButtonType.OK)
                {
                    stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/viewcontroller/updateAppointmentMenu.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show(); 
                }
        }
    }
}
