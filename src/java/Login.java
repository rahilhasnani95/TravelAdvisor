/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.Serializable;
import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Denny Desktop
 */
@ManagedBean
@SessionScoped
//sessionScoped need to implement the interface Serializable
public class Login implements Serializable{
    
    //Database Connection
    DatabaseConnection dbc = new DatabaseConnection();

    //attributes
    public String id="";
    public String password="";
    
    public String oldPsw="";
    public String newPsw1 = "";
    public String newPsw2 = "";
    
    //get methods and set methods
    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }    

    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    } 
    
    public void setOldPsw(String oldPsw) {
        this.oldPsw = oldPsw;
    }

    public void setNewPsw1(String newPsw1) {
        this.newPsw1 = newPsw1;
    }

    public void setNewPsw2(String newPsw2) {
        this.newPsw2 = newPsw2;
    }
      

    public String getOldPsw() {
        return oldPsw;
    }

    public String getNewPsw1() {
        return newPsw1;
    }

    public String getNewPsw2() {
        return newPsw2;
    }
    
    public String login()
    {
        //load the Driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //return to internalError.xhtml
            return ("internalError");
        }      
                
        try
        {            
            dbc.connect_db();
            dbc.rs = null;
            
            dbc.rs = dbc.stat.executeQuery("Select * from [dbo].[Employee] where EmployeeID ='" + id + "'" );
            
            if(dbc.rs.next())
            {
                //id is found                
                if(password.equals(dbc.rs.getString(2)))
                {
                    // if user is admin
                    if(id.equals("admin")&& password.equals("admin"))
                    {
                        //navigate to admin.xhtml
                        return "admin";
                    }
                    else
                    {                    
                        //password is good 
                        //navigate to welcome.xhtml  
                        return "welcome";   
                    }
                }
                else
                {
                    id = "";
                    password = "";
                    //display loginNotOK.xhtml
                    return "loginNotOK";    
                }
                    
                    
            }
            else
            {
                id = "";
                password = "";
                //id is not found, display loginNotOK.xhtml
                return "loginNotOK";
                 
            }            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return ("internalError");
        }
        finally
        {
           dbc.close_db();
        }
    }
         
        
    public String checkCredentials()
    {         
        //load the Driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //return to internalError.xhtml
            return ("internalError");
        }      
        
        try
        {            
            dbc.connect_db();
            dbc.rs = null;
            
            dbc.rs = dbc.stat.executeQuery("Select * from user_account where status= 'Active' and id ='" + id + "'" );
            
            if(dbc.rs.next())
            {
                //id is found                
                if(password.equals(dbc.rs.getString(2)))
                {
                 
                    return "resetPassword.xhtml";
                    
                }
                else
                {
                    id = "";
                    password = "";
                    //display loginNotOK.xhtml
                    return "loginNotOK";    
                }             
                    
            }
            else
            {
                id = "";
                password = "";
                //id is not found, display loginNotOK.xhtml
                return "loginNotOK";
                 
            }            
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return ("internalError");
        }
        finally
        {
           dbc.close_db();
        }       
    }      
    
    
    public String resetPassword()
    {         
        boolean newPswOK = false; 
        boolean matchOldPsw = false;
            
        if(newPsw1.equals(newPsw2))
        {
            newPswOK = true;
        }
        
        if(oldPsw.equals(password))
        {
            matchOldPsw = true;
        }
        
        if(!newPswOK)
        {
            return ("confirmationResetWrong.xhtml");
        }
        else if(!matchOldPsw)
        {
             
            return ("confirmationResetWrong2.xhtml");
            
        }
        else
        {
            //load the Driver
            try
            {
                Class.forName("com.mysql.jdbc.Driver");
            }
            catch (Exception e)
            {
                e.printStackTrace();
                //return to internalError.xhtml
                return ("internalError");
            }      
            
            try 
            {                
                password = newPsw1;
               
                //connect to the database
                dbc.connect_db();                
              
                int r = dbc.stat.executeUpdate("Update user_account set "
                        + "password = '" +  password + "' where id= '" + id + "' and status = 'Active'");
                return("confirmationResetOK");
                
            }
            catch (SQLException e)
            {
                      
                e.printStackTrace();
                return("internalError");
             }
             finally
             {
                dbc.close_db();
             }
             
        }      
        
    } 
} 
    

