/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author kaula
 */
@Named(value = "reqUserBlock")
@RequestScoped

public class Req_User_Block implements Serializable {
    
   //create database object
    DatabaseConnection dbc = new DatabaseConnection();     
    
    ArrayList<Register> requests_block = new ArrayList<Register>();
    
    
    //view users
     public ArrayList<Register> viewUsers()
    {    
        requests_block.removeAll(requests_block);
        
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();         
        }                   
        
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select * from user_account where status= 'Active'");
            
            if(!dbc.rs.next())
            {
                // insert code for no users in active status              
            }
            else                
            {
                dbc.rs = null;            
                dbc.rs = dbc.stat.executeQuery("Select * from user_account where status= 'Active' and id <> 'admin'");             
                
                while(dbc.rs.next())
                {                
                    Register a = new Register();
                    a.setId(dbc.rs.getString("ID")); 
                    a.setPassword(dbc.rs.getString("Password"));    
                    a.setUser_status(dbc.rs.getString("Status"));
                    
                    requests_block.add(a);           
                }   
            
            }
        }    
        catch(SQLException e)
        {
            e.printStackTrace();
        }   
        
        finally
        {
            dbc.close_db();
        }        
        return requests_block;
        
    }     
    
    //block the user from the active users view
    public String block(String userId)
    {
        //load the Driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();   
            
        }          
        
        try
        {
            //connect to database
            dbc.connect_db();              
            dbc.rs = dbc.stat.executeQuery("Select * from user_account where status= 'Active' and id = '" + userId + "'");                  

            if(dbc.rs.next())
            {              
                //block the user
                int k = dbc.stat.executeUpdate("Update user_account set status='InActive' where id = '" + userId + "'");
            }  
            else
            {
                return "internalError";
            }
        }    
        catch(SQLException e)
        {
            e.printStackTrace();
        }   
        
        finally
        {
            dbc.close_db();
        }
        
        //for update in front end table
        for(int i=0; i<requests_block.size(); i++)
        {
            if(userId.equals(requests_block.get(i).getId()))
            {
                requests_block.remove(i);
            }
        }
        return "BlockUsers";
    }

    public ArrayList<Register> getRequests_block() {
        return requests_block;
    }

    public void setRequests_block(ArrayList<Register> requests_block) {
        this.requests_block = requests_block;
    }
         
}
