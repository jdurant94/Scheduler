/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scheduler;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DBConnector;
import static utils.DBConnector.conn;
import utils.Query;

/**
 *
 * @author JDura
 */
public class Scheduler extends Application {
    
//    private static String query;
//        private static Statement stmt;
//        private static ResultSet result;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/viewcontroller/LoginScreen.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SQLException {
        
        
        try {
            DBConnector.makeConnection();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            
        }  
           
           
           
        launch(args);
        
        
        
        
    }
    
}
