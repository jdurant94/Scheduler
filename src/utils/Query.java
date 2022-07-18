/* 
 * 
 * 
 * 
 */
package utils;

import java.sql.ResultSet;
import java.sql.Statement;
import static utils.DBConnector.conn;

/**
 *
 * @author Jordan Durant
 */
public class Query {
    
    private static String query;
    private static Statement stmt;
    private static ResultSet result;
    
    public static void makeQuery(String q)
    {
       query = q; 
       
       try
       {
           //create statement object
           stmt = conn.createStatement();
           
           //determine query execution
           if(query.toLowerCase().startsWith("select"))
           {
               result = stmt.executeQuery(query);
           }
           
           if(query.toLowerCase().startsWith("delete") || query.toLowerCase().startsWith("insert") || query.toLowerCase().startsWith("update"))
           {
               stmt.executeUpdate(query);
           }
       }
       catch(Exception e)
       {
           System.out.println("Error: " + e.getMessage());        
       }
               
    }
    
    public static ResultSet getResult()
    {
        return result;
    }
    
}
