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
import java.time.LocalDateTime;
import static java.time.ZoneOffset.UTC;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Customer;
import utils.DataProvider;
import utils.Query;

/**
 * FXML Controller class
 *
 * @author Jordan Durant
 */
public class UpdateCustomerMenuController implements Initializable {
    
    Parent scene;
    Stage stage;

    @FXML
    private Button cancelBtn;
    @FXML
    private TextField customerIDTxt;
    @FXML
    private TextField customerNameTxt;
    @FXML
    private TextField addressOneTxt;
    @FXML
    private TextField addressTwoTxt;
    @FXML
    private TextField cityTxt;
    @FXML
    private TextField zipTxt;
    @FXML
    private TextField countryTxt;
    @FXML
    private TextField phoneTxt;
    @FXML
    private RadioButton yesBtn;
    @FXML
    private ToggleGroup active;
    @FXML
    private RadioButton noBtn;
    @FXML
    private Button updateBtn;
    private Customer customer;
    boolean validEntry = true;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }    

    public void setCustomer(Customer customer) throws IOException {
        this.customer = customer;
        
        customerIDTxt.setText(Integer.toString(customer.getCustomerId()));
        customerNameTxt.setText(customer.getCustomerName());
        addressOneTxt.setText(customer.getAddressOne());
        addressTwoTxt.setText(customer.getAddressTwo());
        cityTxt.setText(customer.getCity());
        countryTxt.setText(customer.getCountry());
        zipTxt.setText(customer.getPostalCode());
        phoneTxt.setText(customer.getPhone());
        
        System.out.println(customer.getCountry());
        System.out.println(customer.getPostalCode());
        System.out.println(customer.getAddressOne());
        System.out.println(customer.getAddressTwo());
        
        if(customer.isActive())
            yesBtn.fire();
        if(!customer.isActive())
            noBtn.fire();
        
    }    
    
    @FXML
    private void cancelAdd(MouseEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/viewcontroller/MainMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    private void updateCustomer(MouseEvent event) throws IOException, SQLException, InterruptedException {
        

        
        int cityId = 0;
        int addressId = 0;
        int countryId = 0;
        int customerId = Integer.parseInt(customerIDTxt.getText());
        String customerName = customerNameTxt.getText();
        String addressOne = addressOneTxt.getText();
        String addressTwo = addressTwoTxt.getText();
        String address = addressOne + " " + addressTwo;       
        String city = cityTxt.getText();
        String country = countryTxt.getText();
        String postalCode = zipTxt.getText();
        String phone = phoneTxt.getText();
        Boolean active = null;
        int activeInt = 0;
        
        if (customerName.isEmpty() || addressOne.isEmpty() || city.isEmpty() || country.isEmpty() || phone.isEmpty() || postalCode.isEmpty())
        {
            validEntry = false;
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Entry");
            alert.setHeaderText("Please make sure all fields are filled in");
            alert.setContentText("Press \"OK\" to continue");
            
            Optional<ButtonType> alertResult = alert.showAndWait();
            if (alertResult.get() == ButtonType.OK)
            {
                stage = (Stage)((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/UpdateCustomerMenu.fxml"));
                stage.setScene(new Scene(scene));
                stage.show(); 
            }
        }
        
        if (yesBtn.isSelected())
        {
           active = true;
           activeInt = 1;
        }
            
        if (noBtn.isSelected())
        {
            active = false;
            activeInt = 0;
        }
         
        if (validEntry == true)
        {
        Customer newCustomer = new Customer (customerId, customerName, addressOne, addressTwo, city, country, postalCode, phone, active);
        DataProvider.addCustomer(newCustomer);
        DataProvider.deleteCustomer(customer);
        
        
        //update country table
        Query.makeQuery("select * from country where country = '" + country + "'");
        ResultSet result = Query.getResult();
        if (result.next() == false) 
        {
            Query.makeQuery("insert into country(country,createDate,createdBy,lastUpdateBy) values('" + country + "','" + LocalDateTime.now().toString() + "','" 
                    + LoginScreenController.currentUser.getUserName() + "','" + LoginScreenController.currentUser.getUserName() + "')");
            Query.makeQuery("select * from country where country = '" + country + "'");
            ResultSet nextResult = Query.getResult();
            while (nextResult.next())
            {
                countryId = nextResult.getInt(1);
                System.out.println(countryId);
            }
        }  
        else //if (result.next() == true)
        {
            Query.makeQuery("select * from country where country = '" + country + "'");
            result = Query.getResult();
            while (result.next())
            {
                countryId = result.getInt(1);
                System.out.println(countryId);
                
            }
        }
        System.out.println("country finished");
        Thread.sleep(1000);
        //update city table
        Query.makeQuery("select * from city where city = '" + city + "'");
        result = Query.getResult();
        if (result.next() == false) 
        {
            Query.makeQuery("insert into city(city,countryId,createDate,createdBy,lastUpdateBy) values('" + city + "','" + countryId + "','" + LocalDateTime.now().toString() + "','" 
                    + LoginScreenController.currentUser.getUserName() + "','" + LoginScreenController.currentUser.getUserName() + "')");
            Query.makeQuery("select * from city where city = '" + city + "'");
            ResultSet nextResult = Query.getResult();
            while (nextResult.next())
            {
                cityId = nextResult.getInt(1);
                System.out.println(cityId);
            }
        }  
        else //if (result.next() == true)
        {
            Query.makeQuery("select * from city where city = '" + city + "'");
            result = Query.getResult();
            while (result.next())
            {
                cityId = result.getInt(1);
                System.out.println(cityId);
                
            }
        }
        System.out.println("city finished");
        Thread.sleep(1000);
        //update address
        Query.makeQuery("select * from address where address = '" + addressOne + "' AND address2 = '" + addressTwo + "'");
        result = Query.getResult();
        if (result.next() == false) 
        {
            Query.makeQuery("insert into address(address,address2,cityId,postalCode,phone,createDate,createdBy,lastUpdateBy) "
            + "values('" + addressOne + "','" + addressTwo + "','" + cityId + "','" + postalCode + "','" + phone + "','" + LocalDateTime.now(UTC).toString() + "','" 
                    + LoginScreenController.currentUser.getUserName() + "','" + LoginScreenController.currentUser.getUserName() + "')");
            Query.makeQuery("select * from address where address = '" + addressOne + "' AND address2 = '" + addressTwo + "'");
            ResultSet nextResult = Query.getResult();
            while (nextResult.next())
            {
                addressId = nextResult.getInt(1);
                System.out.println(addressId);
            }
        }  
        else //if (result.next() == true)
        {
            Query.makeQuery("select * from address where address = '" + addressOne + "' AND address2 = '" + addressTwo + "'");
            result = Query.getResult();
            while (result.next())
            {
                addressId = result.getInt(1);
                System.out.println(addressId);
                
            }
        }
        System.out.println("address finished");
        Thread.sleep(1000);
        //update customer
        Query.makeQuery("select * from customer where customerName = '" + customerName + "'");
        result = Query.getResult();
        if (result.next() == false) 
        {
            Query.makeQuery("UPDATE customer SET customerName = '" + customerName + "', addressId = " + addressId + ", active = " + activeInt + " where customerId = " + customerId +"");
            ResultSet nextResult = Query.getResult();
            while (nextResult.next())
            {
                customerId = nextResult.getInt(1);
                System.out.println(customerId);
            }
        }  
        else //if (result.next() == true)
        {
            Query.makeQuery("select * from customer where customerName = '" + customerName + "'");
            result = Query.getResult();
            while (result.next())
            {                
                customerId = result.getInt(1);
                System.out.println(customerId);
                
            }
        }
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/viewcontroller/CustomerMenu.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
        }
    }
   }

