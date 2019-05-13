/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.SQLException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author kaula
 */
@ManagedBean
@RequestScoped
public class Register {

    //Database Connection object
    DatabaseConnection dbc = new DatabaseConnection();
    
    //Tags object 
    Tags tag_obj = new Tags();
    
    private String id="";
    private String password="";
    private String user_status="";

    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(String user_status) {
        this.user_status = user_status;
    }
    
    public Tags getTag_obj() {
        return tag_obj;
    }

    public void setTag_obj(Tags tag_obj) {
        this.tag_obj = tag_obj;
    }    
    
    public void setId(String id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }   

    public String getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }
    
    public String register()
    {
        //load the driver
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            
        }
        catch (Exception e)
        {
            return ("Internal Error! Please try again later.");
        }        
        
        try
        {
            if(id.equals(password))
            {
                return("User Id can't be same as Password. Please use a different Password!");
            }
            
            dbc.connect_db();            
            dbc.rs = null;            
            dbc.rs = dbc.stat.executeQuery("Select * from user_account where id = '" + id + "'" );
            
            if(dbc.rs.next())
            {
                 return("Either you have an online account already "
                        + "or your online ID is not available to register");
            }
            else
            {
                user_status = "InActive";
                int r = dbc.stat.executeUpdate("insert into user_account "
                        + "values ('" + id + "','" 
                    + password + "','" + user_status + "')");
                
                for(String tag: tag_obj.getAddedTags())
                {
                
                    int k = dbc.stat.executeUpdate("insert into user_tags "
                            + "values ('" + id + "','" 
                        + tag + "')");
                }

                return ("Registration Successful! " + "Once Admin approves, return to login to your account." + " Tags are Added for: "+ id);
                
            }   
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return ("Internal Error! Please try again later.");
             
        }
        finally
        {
            dbc.close_db();
        }
    
         
    }
     
     
}
