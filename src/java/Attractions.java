
import java.io.Serializable;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author Denny Desktop
 */
@ManagedBean
@SessionScoped

public class Attractions implements Serializable{
 
    public String attr_Id="";
    private String attr_Name;
    private String attr_Desc;
    private String attr_tag;
    private String City;
    private String State;
    private String tag_name;
    private String review;
    public int score=0;
    
    //Declare average score variable for attractions
    double avg_score =0.0;
    
    //Average Score formatted upto 1 place of Decimal
    DecimalFormat df = new DecimalFormat("#.#");
   
    private String request_status;
    public String user_id ;
    
    private String question;
    private String question_status;
    private String question_id="";
    
    private String answer;
   
    
    
    ArrayList <String> tagarr = new ArrayList<String>();    
    ArrayList<Attractions> attr1 = new ArrayList<Attractions>();    
    ArrayList<Attractions> attr2 = new ArrayList<Attractions>();
    
   
    
    //Database Object
    DatabaseConnection dbc = new DatabaseConnection();
    
    //Tag Object
    Tags tag_obj = new Tags(); 


    //create attraction   
    public String createAttraction(String user_id)
    {
       
        int tagcount = 0;
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

           dbc.rs = dbc.stat.executeQuery("Select * from nextattractionid"); 

            int nextNum = 0;
            while(dbc.rs.next()) 
            {
                attr_Id = "" + dbc.rs.getInt(1); //since accountNUm is int thats why we use getInt 
                nextNum = dbc.rs.getInt(1) + 1;

            }

            int t = dbc.stat.executeUpdate("Update nextattractionid set nextid = '" +
                    nextNum + "'"); // it resturn the integer which rownum is updated

            request_status = "Pending";
            tag_name = "";           
            
            for(String tag: tag_obj.getAddedTags())
                {
                    if(tag.equals((tag_obj.getAddedTags()[tag_obj.getAddedTags().length-1])))            
                    {
                        tag_name = tag_name + tag;
                    }
                    else
                    {
                        tag_name = tag_name + tag.concat(",");
                    }                              
                }

            int k  = dbc.stat2.executeUpdate("Insert into attractions values('" + attr_Id + "','" + attr_Name + "','" + attr_Desc + "', '" + City + "', '" 
                            + State + "', '" + user_id + "', '" + request_status + "','" + tag_name + "')");    
           
           //return "Attraction created";
            return "createAttraction_confirmation";
        }
       catch(SQLException e)
       {
           e.printStackTrace();          
          return ("internalError");
       }
                               
    }
    
    
    //rate attraction  
    public String rateAttractions(String attractionId,String user_id)
    {    
        // empty the array lists
        attr1.removeAll(attr1);        
        
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
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= '" + "Approved" + "'");
            
            if(!dbc.rs.next())
            {
                // insert code for no attractions in pending status                
            }
            else                
            {           
                //check if attraction id is already reviewed by the same user                
                dbc.rs = null;
                dbc.rs = dbc.stat.executeQuery("Select * from reviews where attraction_id='" + attractionId + "'" + " AND " + "ID='" + user_id + "'");
                
                if(dbc.rs.next())
                {            
                    return "reviewAlreadyPosted";
                }
                
                dbc.rs = null;      
                dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_ID = '" + attractionId + "' and attraction_status= '" + "Approved" + "'");
                             
                if(dbc.rs.next())
                {                
                    Attractions a = new Attractions();                   
                    a.setAttr_Id(dbc.rs.getString("attraction_ID")); 
                    a.setAttr_Name(dbc.rs.getString("attraction_name"));          

                    attr1.add(a);           
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
        
        return "RateAttraction2";
    }
    
    
    //rate confirmation  
    public String rateConfirmation(String attractionId, String user_id)
    {
        // empty the array lists
        attr2.removeAll(attr2);        
        
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
            
            dbc.rs = null;   
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_ID = '" + attractionId + "' and attraction_status= '" + "Approved" + "'");
            
            if(dbc.rs.next())
                {               
                    Attractions a1 = new Attractions();                    
                    a1.setAttr_Id(dbc.rs.getString("attraction_ID"));         
                    attr2.add(a1);           
                }            
            
            int x = dbc.stat.executeUpdate("Insert into reviews values ('" + attr2.get(0).getAttr_Id() + "', '" + score + "', '" + review + "', '" + user_id + "')");           
            return ("rateAttraction_confirmation");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return "internalError";
        }   
        finally
        {
            dbc.close_db(); 
        }         
    }
    
    
    //view attraction  
     public ArrayList<Attractions> viewAttractions()
    {    
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();         
        }              
        
        ArrayList<Attractions> attr = new ArrayList<Attractions>();
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= '" + "Approved" + "'");
            
            if(!dbc.rs.next())
            {
                // insert code for no attractions in pending status                
            }
            else                
            {
                dbc.rs = null;            
                dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= '" + "Approved" + "'");             
                
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

                    attr.add(a);           
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
        return attr;
        
    }     
     
     
    //search attractions based on city, sorted by average scores
    public ArrayList<Attractions> searchAttractions_City()
    { 
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();         
        }              
        
        ArrayList<Attractions> attr = new ArrayList<Attractions>();
        
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= 'Approved' and attraction_city = '" + City + "'");
            
            if(!dbc.rs.next())
            {
                // table will disappear if no attractions found matching input city      
            }
            else                
            {
                dbc.rs = null;
                
                //search by city sorted by average scores descending
                dbc.rs = dbc.stat.executeQuery("Select attractions.ID, attractions.attraction_id,attraction_name,attraction_city, attraction_desc,attraction_state, attraction_status,tag, "
                                + "avg(reviews.review_score) from attractions left outer join reviews on attractions.attraction_id = reviews.attraction_id"
                       + " where attraction_city ='" + City + "'" + " and attraction_status ='Approved' "
                               + "group by attractions.attraction_id order by avg(reviews.review_score) desc ");     
            
                
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
                    String final_avg = df.format(dbc.rs.getDouble("avg(reviews.review_score)"));                    
                    a.setAvg_score(Double.parseDouble(final_avg));   
                    a.setAttr_tag(dbc.rs.getString("Tag")); 
                    
                    attr.add(a);       
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
        return attr;         
        
    }   
    
    //search attractions based on tag, sorted by average scores
    public ArrayList<Attractions> searchAttractions_Tag()
    {               
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();         
        }              
        
        ArrayList<Attractions> attr = new ArrayList<Attractions>();
        
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where tag like '%" + attr_tag + "%'" + " AND " +
                            "attraction_status ='" + "Approved" + "'");
            
            if(!dbc.rs.next())
            {
                // table will disappear if no attractions found matching input tag      
            }
            else                
            {
                dbc.rs = null;
                
                //search by tag sorted by average scores descending
                dbc.rs = dbc.stat.executeQuery("Select attractions.ID, attractions.attraction_id, attraction_name,attraction_desc,attraction_city,attraction_state,attraction_status,avg(reviews.review_score),tag"
                                + " from attractions left outer join reviews on attractions.attraction_id = reviews.attraction_id"
                                + " where tag like '%" + attr_tag + "%'" + " AND " +
                            "attraction_status ='" + "Approved" + "'" + " group by attractions.attraction_id order by avg(reviews.review_score) desc");                                 
                
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
                    String final_avg = df.format(dbc.rs.getDouble("avg(reviews.review_score)"));                    
                    a.setAvg_score(Double.parseDouble(final_avg));   
                    a.setAttr_tag(dbc.rs.getString("Tag"));                   
                    
                    attr.add(a);           
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
        return attr;
        
    }    
    
    
    public ArrayList<Attractions> viewfavoritecity(String user_id)
    {    
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();         
        }              
        
        ArrayList<Attractions> attr = new ArrayList<Attractions>();
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select a.attraction_Name, a.attraction_Desc, a.attraction_City, a.attraction_State from attractions a, user_favorite_new u where a.attraction_City = u.fav_attraction_city and u.ID = '" + user_id + "'");
            
            if(!dbc.rs.next())
            {
                // insert code for no attractions in pending status                
            }
            else                
            {
                dbc.rs = null;            
                dbc.rs = dbc.stat.executeQuery("Select a.Attraction_Name, a.Attraction_Desc, a.Attraction_City, a.Attraction_State from attractions a, user_favorite_new u where a.Attraction_City = u.fav_attraction_city and u.ID = '" + user_id + "'");
                
                while(dbc.rs.next())
                {                
                    Attractions a = new Attractions();
                   // a.setUser_id(dbc.rs.getString("ID"));
                   // a.setAttr_Id(dbc.rs.getString("Attraction_ID"));
                    a.setAttr_Name(dbc.rs.getString("Attraction_Name"));
                    a.setAttr_Desc(dbc.rs.getString("Attraction_Desc"));
                    a.setCity(dbc.rs.getString("Attraction_City"));        
                    a.setState(dbc.rs.getString("Attraction_State"));                     
                    //a.setRequest_status(dbc.rs.getString("Attraction_Status"));
                   // a.setTag_name(dbc.rs.getString("Tag"));
                   
                   
                    attr.add(a);           
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
        return attr;
        
    }   
    
    
    public ArrayList<Attractions> viewfavoriteAttractionName(String user_id)
    {    
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();         
        }              
        
        ArrayList<Attractions> attr = new ArrayList<Attractions>();
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select a.attraction_Name, a.attraction_Desc, a.attraction_City, a.attraction_State from attractions a, user_favorite_new u where a.attraction_Name = u.fav_attraction_name and u.ID = '" + user_id + "'");
            
            if(!dbc.rs.next())
            {
                // insert code for no attractions in pending status                
            }
            else                
            {
                dbc.rs = null;            
            dbc.rs = dbc.stat.executeQuery("Select a.attraction_Name, a.attraction_Desc, a.attraction_City, a.attraction_State from attractions a, user_favorite_new u where a.attraction_Name = u.fav_attraction_name and u.ID = '" + user_id + "'");
                
                while(dbc.rs.next())
                {                
                    Attractions a = new Attractions();
                   // a.setUser_id(dbc.rs.getString("ID"));
                   // a.setAttr_Id(dbc.rs.getString("Attraction_ID"));
                    a.setAttr_Name(dbc.rs.getString("Attraction_Name"));
                    a.setAttr_Desc(dbc.rs.getString("Attraction_Desc"));
                    a.setCity(dbc.rs.getString("Attraction_City"));        
                    a.setState(dbc.rs.getString("Attraction_State"));                     
                    //a.setRequest_status(dbc.rs.getString("Attraction_Status"));
                   // a.setTag_name(dbc.rs.getString("Tag"));
                   
                   
                    attr.add(a);           
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
        return attr;
        
    }     
    
    
    //display average score of the atractions and all reviews for an attraction
     public String avgscoreAttractions(String attractionId)
    {    
        // empty the array lists
        attr1.removeAll(attr1);
        attr2.removeAll(attr2);
        
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
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= '" + "Approved" + "'");
            
            if(!dbc.rs.next())
            {
                // insert code for no attractions in pending status                
            }
            else                
            {               
                dbc.rs = null;            
                dbc.rs = dbc.stat.executeQuery("Select review_desc, reviews.attraction_ID, attractions.attraction_name from reviews, attractions where attractions.attraction_id = reviews.attraction_id and reviews.attraction_id='" + attractionId+ "'"
                            + " order by review_score desc");  
                
                                
                while(dbc.rs.next())
                {                
                    Attractions a = new Attractions();
                    a.setReview(dbc.rs.getString("review_desc")); 
                    a.setAttr_Id(dbc.rs.getString("attraction_ID")); 
                    a.setAttr_Name(dbc.rs.getString("attraction_name"));          

                    attr1.add(a);           
                }   
                
                
                // for average score
                dbc.rs = null;             
                dbc.rs = dbc.stat.executeQuery("Select attractions.attraction_id,attraction_name, "
                                + "avg(reviews.review_score) from attractions left outer join reviews on attractions.attraction_id = reviews.attraction_id"
                       + " where reviews.attraction_id ='" + attractionId + "'" + " and attraction_status ='Approved' "
                               + "group by attractions.attraction_id order by avg(reviews.review_score) desc ");   
                
                if(dbc.rs.next())
                {                
                    Attractions a = new Attractions();                    
                    a.setAttr_Id(dbc.rs.getString("attraction_ID")); 
                    a.setAttr_Name(dbc.rs.getString("attraction_name")); 
                    String final_avg = df.format(dbc.rs.getDouble("avg(reviews.review_score)"));                    
                    a.setAvg_score(Double.parseDouble(final_avg));    

                    attr2.add(a);           
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
        return "AvgScore_Reviews";     
    }  
     
     
    public String markFavoriteCity(String user_id)
    {
        attr2.removeAll(attr2);
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();  
            return "internalError";
        } 
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where Attraction_Status='Approved' and Attraction_City ='" + City + "'");

            if(!dbc.rs.next())
            {
                return "CityOrAttractionDoesNotExist";
            }
            else
            {
                dbc.rs = dbc.stat.executeQuery("Select * from attractions where Attraction_Status='Approved' and Attraction_City ='" + City + "'");
                if(dbc.rs.next())
                {
                    //Attractions a = new Attractions();
                   // a.setAttr_Name(dbc.rs.getString("Attraction_Name"));
                    //attr2.add(a);
                    
                    dbc.rs = dbc.stat2.executeQuery("select * from user_favorite_new where fav_attraction_city = '" + City + "' and ID = '" + user_id + "'");
                    if(!dbc.rs.next())
                    {
                        
                    int r = dbc.stat3.executeUpdate("Insert into user_favorite_new values ('" + user_id + "', '" +City+ "', '" + ""  + "')");
                    return("FavCity_Confirmation");
                    }
                    else
                    {
                        return "AlreadyMarkedFavorite";
                    }
                
                }
                 
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return "internalError";
        }
       return("FavCity_Confirmation");     
         
     }
    
    
    public String markFavoriteAttrName(String user_id)
    {
        attr2.removeAll(attr2);
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();  
            return "internalError";
        } 
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where Attraction_Status='Approved' and Attraction_Name ='" + attr_Name + "'");

            if(!dbc.rs.next())
            {
                return "CityOrAttractionDoesNotExist";
            }
            else
            {
                dbc.rs = dbc.stat.executeQuery("Select * from attractions where Attraction_Status='Approved' and Attraction_Name ='" + attr_Name + "'");
                if(dbc.rs.next())
                {
                    //Attractions a = new Attractions();
                   // a.setAttr_Name(dbc.rs.getString("Attraction_Name"));
                    //attr2.add(a);
                    dbc.rs = dbc.stat2.executeQuery("select * from user_favorite_new where fav_attraction_name = '" + attr_Name + "' and ID = '" + user_id + "'");
          
                    if(!dbc.rs.next())
                    {
                        
                    int r = dbc.stat2.executeUpdate("Insert into user_favorite_new values ('" + user_id + "', '" +""+ "', '" + attr_Name + "')");
                    return("FavAttrName_Confirmation");
                    }
                    else
                    {
                        return "AlreadyMarkedFavorite";
                    }
                }
                 
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return "internalError";
        }
       return("FavAttrName_Confirmation");     
         
     }
    
    
    //ask a question, user can ask questions even if he has already asked a question for the same attraction id  
    public String askQuestion_Attractions(String attractionId,String user_id)
    {    
        // empty the array lists
        attr1.removeAll(attr1);        
        
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
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_status= '" + "Approved" + "'");
            
            if(!dbc.rs.next())
            {
                // insert code for no attractions in pending status                
            }
            else                
            {           
                
                dbc.rs = null;      
                dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_ID = '" + attractionId + "' and attraction_status= '" + "Approved" + "'");
                             
                if(dbc.rs.next())
                {                
                    Attractions a = new Attractions();                   
                    a.setAttr_Id(dbc.rs.getString("attraction_ID")); 
                    a.setAttr_Name(dbc.rs.getString("attraction_name"));          

                    attr1.add(a);           
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
        
        return "Post_Question";
    }
    
    
    //post a question 
    public String postQuestion_Attractions(String attractionId, String user_id)
    {
        // empty the array lists
        attr2.removeAll(attr2);        
        
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
            
            dbc.rs = null;   
            dbc.rs = dbc.stat.executeQuery("Select * from attractions where attraction_ID = '" + attractionId + "' and attraction_status= '" + "Approved" + "'");
            
            if(dbc.rs.next())
            {               
                Attractions a1 = new Attractions();                    
                a1.setAttr_Id(dbc.rs.getString("attraction_ID"));         
                attr2.add(a1);           
            }    
            
            question_status="NotAnswered";
            
            dbc.rs = null;
            dbc.rs = dbc.stat.executeQuery("Select * from nextQuestionID");

            int nextID = 0;
            while(dbc.rs.next())
            {
                question_id = "" + dbc.rs.getInt(1);
                nextID = dbc.rs.getInt(1) + 1;
            } 

            //update nextQuestionID table
            int t  = dbc.stat.executeUpdate("Update nextQuestionID set nextID = '" + nextID + "'"); 
            
            //insert into question table
            int x = dbc.stat.executeUpdate("Insert into questions values ('" + question_id + "','" + attr2.get(0).getAttr_Id() + "', '" + user_id + "', '" + question + "','" + question_status + "')");           
            return ("postQuestion_Confirmation");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return "internalError";
        }   
        finally
        {
            dbc.close_db(); 
        }         
    }
    
    
    //view Questions
    public ArrayList<Attractions> viewQuestions()
    {    
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();         
        }              
        
        ArrayList<Attractions> attr = new ArrayList<Attractions>();
        
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select * from questions");
            
            if(!dbc.rs.next())
            {
                // insert code for no questions
                
            }
            else                
            {
                dbc.rs = null;            
                dbc.rs = dbc.stat.executeQuery("Select * from questions");             
                
                while(dbc.rs.next())
                {                
                    Attractions a = new Attractions();
                    a.setUser_id(dbc.rs.getString("ID"));
                    a.setAttr_Id(dbc.rs.getString("Attraction_ID"));
                    a.setQuestion_id(dbc.rs.getString("Question_ID"));
                    a.setQuestion(dbc.rs.getString("Question"));
                    a.setQuestion_status(dbc.rs.getString("Status"));

                    attr.add(a);           
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
        return attr;
        
    }
    
    
    //reply to a question 
    public String replytoQuestion_Attractions(String question_id, String user_id)
    {    
        // empty the array lists
        attr1.removeAll(attr1);        
        
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
            dbc.rs = dbc.stat.executeQuery("Select * from questions");
            
            if(!dbc.rs.next())
            {
                // no questions found!          
            }
            else                
            {                              
                //determine whether same user has posted the question who is answering it
                dbc.rs =  null;            
                dbc.rs = dbc.stat.executeQuery("Select ID from Questions where question_id ='" + question_id + "'");
                if(dbc.rs.next())
                {
                    String question_user_id = dbc.rs.getString("Questions.ID");              

                    if(user_id.equals(question_user_id))
                    {
                       return "CantReply";
                    }
                }
                
                dbc.rs = null;      
                dbc.rs = dbc.stat.executeQuery("Select * from questions where question_id='" + question_id + "'");
                             
                if(dbc.rs.next())
                {                
                    Attractions a = new Attractions(); 
                    a.setUser_id(dbc.rs.getString("ID"));  
                    a.setAttr_Id(dbc.rs.getString("attraction_ID"));                    
                    a.setQuestion_id(dbc.rs.getString("Question_ID")); 
                    a.setQuestion(dbc.rs.getString("Question")); 

                    attr1.add(a);           
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
        
        return "Post_Reply";
        
    }
    
         
    //post a reply
    public String postReply_Attractions(String question_id, String user_id)
    {
        // empty the array lists
        attr2.removeAll(attr2);        
        
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
            
            dbc.rs = null;   
            dbc.rs = dbc.stat.executeQuery("Select * from questions where question_id ='" + question_id + "'");
            
            if(dbc.rs.next())
            {               
                Attractions a1 = new Attractions();                    
                a1.setAttr_Id(dbc.rs.getString("attraction_ID")); 
                a1.setQuestion_id(dbc.rs.getString("question_ID"));       
                attr2.add(a1);           
            }                  
            
            
            //insert into answers table
            int x = dbc.stat.executeUpdate("Insert into answers values ('" + attr2.get(0).getAttr_Id() + "','" + attr2.get(0).getQuestion_id() + "', '" + answer + "','" + user_id + "')");           
            
                 
            //set question status from NotAnswered to Answered
            int k =  dbc.stat.executeUpdate("Update Questions set Status = 'Answered' where question_id ='" + question_id + "'");
            return ("postReply_Confirmation");
        }
        catch(SQLException e)
        {
            e.printStackTrace();
            return "internalError";
        }   
        finally
        {
            dbc.close_db(); 
        }   
        
    }
    
    
    // notifications for user for his posted questions
    public ArrayList<Attractions> viewNotifications(String user_id)
    {       
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (Exception e)
        {
            e.printStackTrace();         
        }              
        
        ArrayList<Attractions> attr = new ArrayList<Attractions>();
        
        try
        {
            dbc.connect_db();
            dbc.rs = dbc.stat.executeQuery("Select question_id from questions where status ='" + "Answered" + "'" + " and ID='" + user_id+ "'");
            
            if(!dbc.rs.next())
            {
                // no questions answered for this user
                
            }
            else                
            {
                dbc.rs = null;            
                dbc.rs = dbc.stat.executeQuery("Select Answers.ID, Questions.question_id, question, answer, status from Questions, Answers "
                            + "where Questions.Question_id = Answers.Question_id" + " and status = 'Answered' and Questions.ID='" + user_id +"'" + " order by Questions.question_id");           
                
                while(dbc.rs.next())
                {                
                    Attractions a = new Attractions();
                    a.setUser_id(dbc.rs.getString("Answers.ID"));
                    a.setQuestion_id(dbc.rs.getString("Question_ID"));                  
                    a.setQuestion(dbc.rs.getString("Question"));
                    a.setAnswer(dbc.rs.getString("Answer"));
                    a.setQuestion_status(dbc.rs.getString("Status"));

                    attr.add(a);           
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
        return attr;
        
    }
    
    
    // mark read: notifications
    public String markRead(String user_id,String questionId)
    {
        // clear arraylist
        attr2.removeAll(attr2);
        
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
            
            //populate array list
            dbc.rs = null;            
            dbc.rs = dbc.stat.executeQuery("Select Answers.ID, Questions.question_id, question, answer, status from Questions, Answers "
                        + "where Questions.Question_id = Answers.Question_id" + " and status = 'Answered' and Questions.ID='" + user_id +"'" + " order by Questions.question_id");           

            while(dbc.rs.next())
            {                
                Attractions a = new Attractions();
                a.setUser_id(dbc.rs.getString("Answers.ID"));
                a.setQuestion_id(dbc.rs.getString("Question_ID"));                  
                a.setQuestion(dbc.rs.getString("Question"));
                a.setAnswer(dbc.rs.getString("Answer"));
                a.setQuestion_status(dbc.rs.getString("Status"));

                attr2.add(a);           
            }   
                
            dbc.rs = null;
            dbc.rs = dbc.stat.executeQuery("Select question_id from questions where status ='" + "Answered" + "'" + " and ID='" + user_id+ "'");

            if(dbc.rs.next())
            {              
                //set question status from Answered to Read            
                int k = dbc.stat.executeUpdate("Update Questions set Status='" + "Read" 
                            + "'" + " where Status='" + "Answered" + "'" + " and ID='"+ user_id + "'" + " and Question_ID ='" + questionId + "'");
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
        for(int i=0; i<attr2.size(); i++)
        {
            if(questionId.equals(attr2.get(i).getQuestion_id()))
            {
                attr2.remove(i);
            }
        }
        return "Notifications_MainPage";
    }  
     
    
    
    //getter and setter methods     
    public String getAttr_Id() {
        return attr_Id;
    }

    public void setAttr_Id(String attr_Id) {
        this.attr_Id = attr_Id;
    }   

    public String getAttr_Name() {
        return attr_Name;
    }

    public void setAttr_Name(String attr_Name) {
        this.attr_Name = attr_Name;
    }

    public String getAttr_Desc() {
        return attr_Desc;
    }

    public void setAttr_Desc(String attr_Desc) {
        this.attr_Desc = attr_Desc;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String City) {
        this.City = City;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getTag_name() {
        return tag_name;
    }

    public void setTag_name(String tag_name) {
        this.tag_name = tag_name;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getRequest_status() {
        return request_status;
    }

    public void setRequest_status(String request_status) {
        this.request_status = request_status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public DatabaseConnection getDbc() {
        return dbc;
    }

    public void setDbc(DatabaseConnection dbc) {
        this.dbc = dbc;
    }    
    
    public ArrayList<Attractions> getAttr2() {
        return attr2;
    }

    public void setAttr2(ArrayList<Attractions> attr2) {
        this.attr2 = attr2;
    }   

    public ArrayList<Attractions> getAttr1() {
        return attr1;
    }

    public void setAttr1(ArrayList<Attractions> attr1) {
        this.attr1 = attr1;
    }   

    public String getAttr_tag() {
        return attr_tag;
    }

    public void setAttr_tag(String attr_tag) {
        this.attr_tag = attr_tag;
    }  

    public double getAvg_score() {
        return avg_score;
    }

    public void setAvg_score(double avg_score) {
        this.avg_score = avg_score;
    }
    
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    
    public Tags getTag_obj() {
        return tag_obj;
    }

    public void setTag_obj(Tags tag_obj) {
        this.tag_obj = tag_obj;
    } 
    
    public String getQuestion_status() {
        return question_status;
    }

    public void setQuestion_status(String question_status) {
        this.question_status = question_status;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }
    
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    
    
}