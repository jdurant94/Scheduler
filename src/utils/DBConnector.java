/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.mysql.jdbc.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author JDura
 */
public class DBConnector {
    
    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String ip = "//x.xxx.xxx.xxx/xxxxx";
    
    //JDBC URL
    private static final String jdbcURL = protocol + vendor + ip;
    
    //driver and connection interface reference
    private static final String MYSQLJDBCDriver = "com.mysql.jdbc.Driver";
    public static Connection conn;
    
    //credentials
    private static final String username = "xxxxx";
    private static final String password = "xxxxxxxxxx";
    
    public static void makeConnection() throws ClassNotFoundException, SQLException, Exception
    {

            Class.forName(MYSQLJDBCDriver);
            conn = (Connection) DriverManager.getConnection(jdbcURL, username, password);
            System.out.println("Connection Successful");
        
    }     
    
    public static void closeConnection() throws SQLException
    {        
            conn.close();
            System.out.println("Connection Closed");     
    }
    
}
