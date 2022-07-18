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
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Address;
import model.City;
import model.Country;
import model.Customer;
import model.User;
import utils.DataProvider;
import utils.Query;

/**
 * FXML Controller class
 *
 * @author Jordan Durant
 */
public class CustomerMenuController implements Initializable {
    
    Parent scene;
    Stage stage;

    @FXML
    private Button mainMenuBtn;
    @FXML
    private Button addCustomerBtn;
    @FXML
    private Button updateCustomerBtn;
    @FXML
    private Button deleteCusomterBtn;
    @FXML
    private TableView<Customer> customersTblView;
    @FXML
    private TableColumn<Customer, Integer> customerIdCol;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private TableColumn<City, String> cityCol;
    @FXML
    private TableColumn<Address, Integer> zipCol;
    @FXML
    private TableColumn<Address, Integer> phoneCol;
    @FXML
    private TableColumn<Customer, Boolean> activeCol;
    
    private static ObservableList<User> customerList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Country, String> countryCol;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
//        try {
//            Query.makeQuery("SELECT * FROM CustomerList");
//            ResultSet customers = Query.getResult();
//            while(customers.next())
//            {
//                DataProvider.addCustomer(getCustomerList(customers));
//            }
//        } catch (SQLException e) {
//            System.out.println("Cannot pull customerlist " + e.getMessage());
//        }
        
        
        customersTblView.setItems(DataProvider.getAllCustomers());
        
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(cellData -> Bindings.createStringBinding(() -> cellData.getValue().getAddressOne() + " " + cellData.getValue().getAddressTwo()));
        cityCol.setCellValueFactory(new PropertyValueFactory<>("city"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("country"));        
        zipCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        activeCol.setCellValueFactory(new PropertyValueFactory<>("active"));
        
        
        
        
    }    

    @FXML
    private void DeleteCustomer(MouseEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("This customer will be deleted from the database");
        alert.setContentText("Are you sure you want to continue?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK)
        {
            Customer customerToDelete = customersTblView.getSelectionModel().getSelectedItem();
            System.out.println(customerToDelete.getCustomerId());
            DataProvider.getAllCustomers().remove(customersTblView.getSelectionModel().getSelectedItem());
            
            Query.makeQuery("delete from customer where customerId = " + customerToDelete.getCustomerId());
            
            
           
        }
    }

    @FXML
    private void toMainMenu(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/viewcontroller/MainMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            
    }

    @FXML
    private void toAddCustomerMenu(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/viewcontroller/AddCustomerMenu.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
        
    }

    @FXML
    private void toUpdateCustomerMenu(MouseEvent event) throws IOException {
        
        stage=(Stage) updateCustomerBtn.getScene().getWindow();
        FXMLLoader loader=new FXMLLoader(getClass().getResource(
               "/viewcontroller/UpdateCustomerMenu.fxml"));
        scene = loader.load();
        Scene newScene = new Scene(scene);
        stage.setScene(newScene);
        stage.show();
        UpdateCustomerMenuController controller = loader.getController();
        Customer customer = customersTblView.getSelectionModel().getSelectedItem();
        controller.setCustomer(customer);
        
        
    }
    
    public static Customer getCustomerList (ResultSet r)
    {
        Customer customerToCreate = null;

        
        try
        {
            int customerId = r.getInt("customerId");
            String customerName = r.getString("customerName");
            String addressOne = r.getString("address");
            String addressTwo = r.getString("address2");
            //String address = addressOne + " " +addressTwo;
            String city = r.getString("city");
            String country = r.getString("country");
            String postalCode = r.getString("postalCode");
            String phone = r.getString("phone");
            boolean active = r.getBoolean("active");
            
            customerToCreate = new Customer(customerId, customerName, addressOne, addressTwo, city, country, postalCode, phone, active);
        }
        catch (SQLException e)
        {
            System.out.println("Cannot fetch address " + e.getMessage());
        }
        
        return customerToCreate;
        
    }
    
    
    
}
