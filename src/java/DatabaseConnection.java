/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author kaula
 */
public class DatabaseConnection {
    
    Connection conn = null;
    Statement stat = null;
    Statement stat2 = null;
    Statement stat3 = null;
    ResultSet rs = null;
    
    //connect to database
    public void connect_db()
    {
        try
        {
            
              
            String hostName = "rahil.database.windows.net"; // update me
            String dbName = "rahil"; // update me
            String user = "rahil"; // update me
            String password = "D@zzlers95"; // update me
          final  String db_url = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;"
            + "hostNameInCertificate=*.database.windows.net;", hostName, dbName, user, password);
            
          conn= DriverManager.getConnection(db_url, "rahil", "D@zzlers95");
          
          
           // final String db_url = "jdbc:mysql://mis-sql.uhcl.edu/kaula8302";

            //connect to database
           // conn = DriverManager.getConnection(db_url,"kaula8302","1592203");

            //create a statement
            stat = conn.createStatement();
            stat2 = conn.createStatement();
            stat3 = conn.createStatement();
        }        
        catch(SQLException e)
        {
            // 
            e.printStackTrace();
        }
    }
    
    //close the database
    public void close_db()
    {
        
        try
        {
            if(rs!=null)
            {
                rs.close();
            }
            
            if(stat!=null){
            stat.close();}
            
            if(stat2!=null){
            stat2.close();}
            
            if(stat3!=null){
            stat3.close();}
            
            if(conn!= null){
            conn.close(); }          
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }       
               
    }
}