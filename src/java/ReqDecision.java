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
@Named(value = "reqDecision")
@RequestScoped

public class ReqDecision implements Serializable {
    
   //create database object
    DatabaseConnection dbc = new DatabaseConnection(); 
    
    ArrayList<Attractions> requests = new ArrayList<Attractions>();
    
    public ReqDecision()
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
            
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= '" + "Pending" + "'");
            
            if(!dbc.rs.next())
            {
                // insert code for no attractions in pending status       
                
            }
            else                
            {
                dbc.rs = null;            
                dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= '" + "Pending" + "'");             
                
                while(dbc.rs.next())
                {                
                    Attractions a = new Attractions();                    
                    a.setUser_id(dbc.rs.getString("ID"));
                    a.setAttr_Id(dbc.rs.getString("Attraction_ID"));
                    a.setAttr_Name(dbc.rs.getString("Attraction_Name"));
                    a.setAttr_Desc(dbc.rs.getString("Attraction_Desc"));
                    a.setCity(dbc.rs.getString("Attraction_City"));        
                    a.setState(dbc.rs.getString("Attraction_State"));                     
                    a.setRequest_status(dbc.rs.getString("Attraction_Status"));
                    a.setTag_name(dbc.rs.getString("Tag"));

                    requests.add(a);           
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
    
    public String accept(String attractionId)
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
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= 'Pending' and attraction_id = '" + attractionId + "'");                  

            if(dbc.rs.next())
            {              
                //set attraction status to approved
                int k = dbc.stat.executeUpdate("Update attractions set attraction_status = 'Approved' where attraction_id = '" + attractionId + "'");
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
        for(int i=0; i<requests.size(); i++)
        {
            if(attractionId.equals(requests.get(i).getAttr_Id()))
            {
                requests.remove(i);
            }
        }
        return "admin";
    }
    
    public String reject(String attractionId)
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
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= 'Pending' and attraction_id = '" + attractionId + "'");                  

            if(dbc.rs.next())
            {              
                //remove the attraction
                int k = dbc.stat.executeUpdate("Delete from attractions where attraction_id = '" + attractionId + "'");
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
        for(int i=0; i<requests.size(); i++)
        {
            if(attractionId.equals(requests.get(i).getAttr_Id()))
            {
                requests.remove(i);
            }
        }
        return "admin";
    }

    public ArrayList<Attractions> getRequests() {
        return requests;
    }
    
    
    
}
