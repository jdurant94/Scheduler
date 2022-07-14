/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.ZoneOffset.UTC;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import java.util.TimeZone;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Address;
import model.Appointment;
import model.City;
import model.Country;
import model.Customer;
import model.User;

/**
 *
 * @author JDura
 */
public class DataProvider {
    
    //holds data from DB in observable lists
    
    private static ObservableList<Address> allAddresses = FXCollections.observableArrayList();
    private static ObservableList<Appointment> allAppointments = FXCollections.observableArrayList();
    private static ObservableList<City> allCities = FXCollections.observableArrayList();
    private static ObservableList<Country> allCountries = FXCollections.observableArrayList();
    private static ObservableList<Customer> allCustomers = FXCollections.observableArrayList();
    private static ObservableList<Customer> allCustomersFull = FXCollections.observableArrayList();
    private static ObservableList<User> allUsers = FXCollections.observableArrayList();
    
    
    public static String convertToNewFormat(String dateStr) throws ParseException {
    TimeZone utc = TimeZone.getTimeZone("UTC");
    SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    SimpleDateFormat destFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    sourceFormat.setTimeZone(utc);
    Date convertedDate = (Date) sourceFormat.parse(dateStr);
    return destFormat.format(convertedDate);
    }
    
    public static ObservableList<Address> getAllAddresses() {
        return allAddresses;
    }
    
    public static void addAddress(Address newAddress)
    {
        allAddresses.add(newAddress);
    }
    
    public static void deleteAddress(Address selectedAddress)
    {
        allAddresses.remove(selectedAddress);
    }
    
    public static Address getAddressFromDB (ResultSet r)
    {
        Address addressToCreate = null;
        try
        {
            int addressId = r.getInt("addressId");
            String address = r.getString("address");
            String address2 = r.getString("address2");
            int cityId = r.getInt("cityId");
            int postalCode = r.getInt("postalCode");
            int phone = r.getInt("phone");
            
            addressToCreate = new Address(addressId, address, address2, cityId, postalCode, phone);
        }
        catch (SQLException e)
        {
            System.out.println("Cannot fetch address " + e.getMessage());
        }
        
        return addressToCreate;
        
    }
    
    //appointment methods
    
    public static ObservableList<Appointment> getAllAppointments() {
        return allAppointments;
    }
    
    public static void addAppointment(Appointment newAppointment)
    {
        allAppointments.add(newAppointment);
    }
    
    public static void deleteAppointment(Appointment selectedAppointment)
    {
        allAppointments.remove(selectedAppointment);
    }
    
    
    public static Appointment getAppointmentList (ResultSet r)
    {
        ZoneId zoneId = ZoneId.systemDefault();
        
        Appointment appointmentToCreate = null;
        try
        {
            int appointmentId = r.getInt("appointmentId");
            String customerName = r.getString("customerName");
            String type = r.getString("type");
            String start = r.getString("start");
            start = start.substring(0, start.length() - 2);
            String end = r.getString("end");
            end = end.substring(0, end.length() - 2);
            
            System.out.println(start);
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDateTime = LocalDateTime.parse(start, formatter);
            LocalDateTime endDateTime = LocalDateTime.parse(end, formatter);
            ZonedDateTime startDateTimeUTC = startDateTime.atZone(UTC);
            ZonedDateTime endDateTimeUTC = endDateTime.atZone(UTC);
            ZonedDateTime startDateTimeZoned = startDateTimeUTC.withZoneSameInstant(zoneId);
            ZonedDateTime endDateTimeZoned = endDateTimeUTC.withZoneSameInstant(zoneId);
            String startString = startDateTimeZoned.format(formatter);
            String endString = endDateTimeZoned.format(formatter);
            System.out.println(startString);
            
            
            
           
            appointmentToCreate = new Appointment(appointmentId, customerName, type, startString, endString);
        }
        catch (SQLException e)
        {
            System.out.println("Cannot fetch appointments " + e.getMessage());
        }
        
        return appointmentToCreate;
        
    }
    
    public static ObservableList<Customer> getAllCustomersFull() {
        return allCustomersFull;
    }
    
    //city methods
    
    public static ObservableList<City> getAllCities() {
        return allCities;
    }
    
    public static void addCity(City newCity)
    {
        allCities.add(newCity);
    }
    
    public static void deleteCity(City selectedCity)
    {
        allCities.remove(selectedCity);
    }
    
    public static City getCityFromDB (ResultSet r)
    {
        City cityToCreate = null;
        try
        {
            int cityId = r.getInt("cityId");
            String city = r.getString("city");
            int countryId = r.getInt("countryId");
            
            cityToCreate = new City(cityId, city, countryId);
        }
        catch (SQLException e)
        {
            System.out.println("Cannot fetch address " + e.getMessage());
        }
        
        return cityToCreate;
        
    }
    
    //country methods
    
    public static ObservableList<Country> getAllCountries() {
        return allCountries;
    }
    
    public static void addCountry(Country newCountry)
    {
        allCountries.add(newCountry);
    }
    
    public static void deleteCountry(Country selectedCountry)
    {
        allCountries.remove(selectedCountry);
    } 
    
    public static Country getCountryFromDB (ResultSet r)
    {
        Country countryToCreate = null;
        try
        {
            int countryId = r.getInt("countryId");
            String country = r.getString("country");
            
            countryToCreate = new Country(countryId, country);
        }
        catch (SQLException e)
        {
            System.out.println("Cannot fetch address " + e.getMessage());
        }
        
        return countryToCreate;
        
    }
        
    //customer methods
    
    public static ObservableList<Customer> getAllCustomers() {
        return allCustomers;
    }
    
    public static void addCustomer(Customer newCustomer)
    {
        allCustomers.add(newCustomer);
    }
    
    public static void deleteCustomer(Customer selectedCustomer)
    {
        allCustomers.remove(selectedCustomer);
    }  
    
    public static Customer getCustomerFromDB (ResultSet r)
    {
        Customer customerToCreate = null;

        
        try
        {
            int customerId = r.getInt("customerId");
            String customerName = r.getString("customerName");
            int addressId = r.getInt("addressId");
            boolean active = r.getBoolean("active");
            
            customerToCreate = new Customer(customerId, customerName, addressId, active);
        }
        catch (SQLException e)
        {
            System.out.println("Cannot fetch address " + e.getMessage());
        }
        
        return customerToCreate;
        
    }
    
    //user methods
    
    public static ObservableList<User> getAllUsers() {
        return allUsers;
    }
    
    public static void addUser(User newUser)
    {
        allUsers.add(newUser);
    }
    
    public static void deleteUser(User selectedUser)
    {
        allUsers.remove(selectedUser);
    } 
    
    public static User getUserFromDB (ResultSet r)
    {
        User userToCreate = null;
        try
        {
            int userId = r.getInt("userId");
            String userName = r.getString("userName");
            String password = r.getString("password");
            boolean active = r.getBoolean("active");
           
            userToCreate = new User(userId, userName, password, active);
        }
        catch (SQLException e)
        {
            System.out.println("Cannot fetch address " + e.getMessage());
        }
        
        return userToCreate;
        
    }
    
}
