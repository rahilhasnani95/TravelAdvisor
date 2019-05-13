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
@Named(value = "reqUserApproval")
@RequestScoped

public class Req_User_Approval implements Serializable {
    
   //create database object
    DatabaseConnection dbc = new DatabaseConnection(); 
    
    ArrayList<Register> requests_users = new ArrayList<Register>();   
    
        
    public Req_User_Approval()
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
            
            dbc.rs = dbc.stat.executeQuery("Select * from user_account where status= '" + "InActive" + "'");
            
            if(!dbc.rs.next())
            {
                // insert code for no users in InActive status       
                
            }
            else                
            {
                dbc.rs = null;            
                dbc.rs = dbc.stat.executeQuery("Select * from user_account where status= '" + "InActive" + "'");             
                
                while(dbc.rs.next())
                {                
                    Register a = new Register();                    
                    a.setId(dbc.rs.getString("ID"));
                    a.setPassword(dbc.rs.getString("Password"));
                    a.setUser_status(dbc.rs.getString("Status"));
                    
                    requests_users.add(a);           
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
    }
    
    public String accept(String userId)
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
            dbc.rs = dbc.stat.executeQuery("Select * from user_account where status= 'InActive' and id = '" + userId + "'");                  

            if(dbc.rs.next())
            {              
                //set user status to active
                int k = dbc.stat.executeUpdate("Update user_account set status = 'Active' where id = '" + userId + "'");
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
        for(int i=0; i<requests_users.size(); i++)
        {
            if(userId.equals(requests_users.get(i).getId()))
            {
                requests_users.remove(i);
            }
        }
        return "admin";
    }
    
    public String reject(String userId)
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
            dbc.rs = dbc.stat.executeQuery("Select * from user_account where status= 'InActive' and id = '" + userId + "'");                  

            if(dbc.rs.next())
            {              
                //remove the user
                int k = dbc.stat.executeUpdate("Delete from user_account where id = '" + userId + "'");
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
        for(int i=0; i<requests_users.size(); i++)
        {
            if(userId.equals(requests_users.get(i).getId()))
            {
                requests_users.remove(i);
            }
        }
        return "admin";
    }    

    public ArrayList<Register> getRequests_users() {
        return requests_users;
    }

    public void setRequests_users(ArrayList<Register> requests_users) {
        this.requests_users = requests_users;
    }    
    
}
