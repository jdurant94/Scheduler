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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.converter.LocalDateTimeStringConverter;
import model.Address;
import model.Appointment;
import model.City;
import model.Country;
import model.Customer;
import utils.DBConnector;
import utils.DataProvider;
import utils.Query;

/**
 * FXML Controller class
 *
 * @author Jordan Durant
 */
public class MainMenuController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private Button LogoutBtn;
    @FXML
    private Button newAppointmentBtn;
    @FXML
    private Button updateAppointmentBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private Button customersBtn;
    @FXML
    private Button reportsMenuBtn;
    @FXML
    private TableView<Appointment> appointmentTblView;
    @FXML
    private TableColumn<Appointment, String> appointmentIdCol;
    @FXML
    private TableColumn<Customer, String> customerCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, String> startCol;
    @FXML
    private TableColumn<Appointment, String> endCol;
    
    /*private static ObservableList<Appointment> weeklyAppointments = FXCollections.observableArrayList();
    private static ObservableList<Appointment> monthlyAppointments = FXCollections.observableArrayList();
    @FXML
    private Button weeklyBtn;
    @FXML
    private Button monthlyBtn;*/

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    //load weekly tableview
   /* weeklyAppointments.clear();
    monthlyAppointments.clear();
    
        for (Appointment appointment : DataProvider.getAllAppointments())
        {
            String startTime = appointment.getStart();
            //String replace = startTime.toString().replace("T", " ");
            System.out.println(startTime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(startTime, formatter);
            
            
            if ((dateTime.isBefore(LocalDateTime.now().plusDays(7)) && dateTime.isAfter(LocalDateTime.now().minusDays(1))))
            {
                weeklyAppointments.add(appointment);
            }
            
        }

        appointmentTblView.setItems(weeklyAppointments);
        
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));        

    //load monthly data   
    
    for (Appointment appointment : DataProvider.getAllAppointments())
        {
            String startTime = appointment.getStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(startTime, formatter);
            
            if ((dateTime.isAfter(LocalDateTime.now().minusDays(1)) && dateTime.isBefore(LocalDateTime.now().plusDays(31))))
            {
                monthlyAppointments.add(appointment);
            }
            
        }*/
   
        appointmentTblView.getItems().clear();
        
        //load all appointments
        appointmentTblView.setItems(DataProvider.getAllAppointments());
        
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));        
        
        
    
    }    

    @FXML
    private void Logout(MouseEvent event) throws SQLException {
        
        DBConnector.closeConnection();
        System.exit(0);
        
    }

    @FXML
    private void toAddAppointmentMenu(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/viewcontroller/AddAppointmentMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    private void toUpdateAppointmentMenu(MouseEvent event) throws IOException {
        
        stage=(Stage) updateAppointmentBtn.getScene().getWindow();
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
               "/viewcontroller/UpdateAppointmentMenu.fxml"));
        scene = loader.load();
        Scene newScene = new Scene(scene);
        stage.setScene(newScene);
        stage.show();
        UpdateAppointmentMenuController controller = loader.getController();
        Appointment appointment = appointmentTblView.getSelectionModel().getSelectedItem();
        controller.setAppointment(appointment);
        
    }

    @FXML
    private void deleteAppointment(MouseEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This appointment will be deleted from the database");
        alert.setContentText("Are you sure you want to continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Appointment appointmentToDelete = appointmentTblView.getSelectionModel().getSelectedItem();
            System.out.println(appointmentToDelete.getAppointmentId());
            DataProvider.getAllCustomers().remove(appointmentTblView.getSelectionModel().getSelectedItem());
            
            appointmentTblView.getItems().remove(appointmentTblView.getSelectionModel().getSelectedItem());
            Query.makeQuery("delete from appointment where appointmentId = " + appointmentToDelete.getAppointmentId());

               
        }
        
    }

    @FXML
    private void toCustomersMenu(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/viewcontroller/CustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    private void toReportsMenu(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/viewcontroller/ReportsMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    private void toWeeklyView(MouseEvent event) throws ParseException {


        /*appointmentTblView.setItems(weeklyAppointments);
        
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end")); */
        
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus7 = now.plusDays(7);
        FilteredList<Appointment> filteredData = new FilteredList<>(DataProvider.getAllAppointments());
        filteredData.setPredicate(row -> {

            String rowDateString = row.getStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime rowDate = LocalDateTime.parse(rowDateString, formatter);

            return rowDate.isAfter(now) && rowDate.isBefore(nowPlus7);
        });
        appointmentTblView.setItems(filteredData);
                                                                                                                    //using lamdas for my filters allowed me to greatly reduce the amount of code required to filter by week and month
                                                                                                                    //I have left the old code I used commented out under the initialize method and the toWeeklyView method in order to 
                                                                                                                    //show just how much simpler the code is. I was able to do away with the observable lists I had for each separate
                                                                                                                    //sort completely
    }

    @FXML
    private void toMonthlyView(MouseEvent event) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nowPlus31 = now.plusDays(31);
        FilteredList<Appointment> filteredData = new FilteredList<>(DataProvider.getAllAppointments());
        filteredData.setPredicate(row -> {

            String rowDateString = row.getStart();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime rowDate = LocalDateTime.parse(rowDateString, formatter);

            return rowDate.isAfter(now) && rowDate.isBefore(nowPlus31);
        });
        appointmentTblView.setItems(filteredData); 

    }

    
}
