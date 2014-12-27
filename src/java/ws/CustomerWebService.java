/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import customer.Account;
import customer.CustomerProfile;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.sql.DataSource;

/**
 *
 * @author hiteshkhapre
 */
@WebService(serviceName = "CustomerWebService")
public class CustomerWebService {
 @Resource(name = "mySQLABC")
    private DataSource mySQLABC;

    /**
     * Web service operation
     * @param customer_username
     * @return 
     */
    @WebMethod(operationName = "getMyProfile")
    public CustomerProfile getMyProfile(@WebParam(name = "customer_username") String customer_username) {
        //TODO write your implementation code here:
        CustomerProfile cust_profile = null;
        Connection conn = null;
        try {
            //TODO write your implementation code here:
            conn = mySQLABC.getConnection();
            PreparedStatement pstat = conn.prepareStatement("SELECT * FROM `ABC Bank`.Customer where Cust_Username = ?");
            pstat.setString(1,customer_username);
            ResultSet rs = pstat.executeQuery();
            cust_profile = new CustomerProfile();
            
            while(rs.next())
            {
               cust_profile.setCust_ID(rs.getInt("cust_ID"));
              cust_profile.setCust_firstname(rs.getString("cust_Firstname"));
             cust_profile.setCust_lastname(rs.getString("cust_Lastname"));
             cust_profile.setCust_Addressline1(rs.getString("cust_Addressline1"));
             cust_profile.setCust_Addressline2(rs.getString("cust_Addressline2"));
             cust_profile.setCust_City(rs.getString("cust_City"));
             cust_profile.setCust_Contactnumber(rs.getString("cust_Contactnumber"));
             cust_profile.setCust_Email(rs.getString("cust_Email"));
            }
            
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
       finally
        {
            if(conn!= null)
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return cust_profile;
    }

    /**
     * Web service operation
     * @param custID
     * @return 
     */
    @WebMethod(operationName = "getAccountDetails")
    public Account getAccountDetails(@WebParam(name = "custID") int custID) {
        //TODO Change the datatype of Customer ID
        Connection conn = null;
        Account account = null;
        try {
            //TODO write your implementation code here:
            conn = mySQLABC.getConnection();
            PreparedStatement pstat = conn.prepareStatement("SELECT * FROM `ABC Bank`.Account where Cust_ID = ?");
            pstat.setInt(1,custID);
            ResultSet rs = pstat.executeQuery();
            account = new Account();
            
            while(rs.next())
            {
             account.setAccountNumber(rs.getInt("Account_Number"));
             account.setAccountType(rs.getString("Account_Type"));
             account.setAccountBalance(rs.getDouble("Account_Balance"));
             account.setAccountStatus(rs.getString("Account_Status"));
             
            }
            
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally
        {
            if(conn!= null)
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return account;
    }

    /**
     * Web service operation
     * @param customerDetails
     * @return 
     * @throws java.sql.SQLException 
     */
    @WebMethod(operationName = "addCustomer")
    public String addCustomer(@WebParam(name = "parameter") CustomerProfile customerDetails) {
        //TODO write your implementation code here:
        Connection conn = null;
        PreparedStatement stat = null;
        try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
            
            stat = conn.prepareStatement("insert into `ABC Bank`.login (username,password,login_type) values (?,?,?)");
            
            String username = customerDetails.getCust_Username();
            String password = customerDetails.getCust_Password();
            
            stat.setString(1, username);
            stat.setString(2, password);
            stat.setString(3, "customer");       
            stat.executeUpdate();
            
            
            stat = conn.prepareStatement("insert into `ABC Bank`.Customer (Cust_ID,Cust_Firstname,Cust_Lastname,Cust_Addressline1,Cust_Addressline2,Cust_City,Cust_Contactnumber,Cust_Email,"
                    + "Cust_Username) values (?,?,?,?,?,?,?,?,?)");
            
            String firstname = customerDetails.getCust_firstname();
            String lastname = customerDetails.getCust_lastname();
            String addressLine1 = customerDetails.getCust_Addressline1();
            String addressLine2 = customerDetails.getCust_Addressline2();
            String city = customerDetails.getCust_City();
            String contact_number = customerDetails.getCust_Contactnumber();
            String email = customerDetails.getCust_Email();
            
            stat.setString(1, null);
            stat.setString(2, firstname);
             stat.setString(3, lastname);
              stat.setString(4, addressLine1);
              stat.setString(5, addressLine2);
              stat.setString(6, city);
               stat.setString(7, contact_number);
               stat.setString(8, email);
               stat.setString(9, username);
            
                 stat.executeUpdate();
                 conn.commit();
                 
            //Get the new custID from DB and then insert record in Account table 
             PreparedStatement pstat = conn.prepareStatement("SELECT Cust_ID FROM `ABC Bank`.Customer where Cust_Username = ?");
            pstat.setString(1,username);
            ResultSet rs = pstat.executeQuery();          

            int newCustID = 0;
            
            while(rs.next())  
            {
                newCustID = rs.getInt("Cust_ID");
            }
            
            
            stat = conn.prepareStatement("insert into `ABC Bank`.Account (Account_Number,Cust_ID,Account_Type,Account_Balance,Account_Status) values (?,?,?,?,?)");
           
            String accounttype = customerDetails.getAccountType();
            Double accountbalance = customerDetails.getAccountBalance();
            
              stat.setString(1, null);
            stat.setInt(2, newCustID);
            stat.setString(3, accounttype);
            stat.setDouble(4, accountbalance);
            stat.setString(5, "Active");
          
            stat.executeUpdate();
            
            Account account_new = getAccountDetails(newCustID);
            int newAccount_number = account_new.getAccountNumber();
            
            stat = conn.prepareStatement("insert into `ABC Bank`.Transaction (Transaction_ID,Account_Number,Transaction_Type,"
                   + "Timestamp,Transaction_Amount) values "
                   + "(?,?,?,?,?)");
             stat.setString(1, null);
           stat.setInt(2,newAccount_number);
           stat.setString(3,"Credit");
           stat.setTimestamp(4,null);
           stat.setDouble(5, accountbalance);
           stat.executeUpdate();
            
             conn.commit();
                 
            
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(CustomerWebService.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }finally{
      //finally block used to close resources
      try{
         if(stat!=null)
         {stat.close();
            conn.close();}
      }catch(SQLException se){
      }// do nothing
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }
        
    }
        return "Inserted";
    }

    /**
     * Web service operation
     * @param username
     * @return 
     */
    @WebMethod(operationName = "insertOperation")
    public String insertOperation(@WebParam(name = "username") String username) throws SQLException {
        //TODO write your implementation code here:
        Connection conn = null;
        PreparedStatement stat = null;
        try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
            stat = conn.prepareStatement("insert into `ABC Bank`.login (username,password,login_type) values (?,?,?)");
            
//             String sql = "insert into `ABC Bank`.login (username,password,login_type) values (username,'pwd','customer')";
           
            stat.setString(1, username);
            stat.setString(2, "pwd");
            stat.setString(3, "customer");       
            stat.executeUpdate();
            
            conn.commit();
            
            
        }catch (SQLException ex) {
            System.out.println(ex.getMessage());
            conn.rollback();
        }finally{
      //finally block used to close resources
      try{
         if(stat!=null)
            conn.close();
      }catch(SQLException se){
      }// do nothing
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }
        }
        return "Inserted";
    }

    /**
     * Web service operation
     * @return 
     */
    @WebMethod(operationName = "getTotalNumberOfCustomers")
    public Integer getTotalNumberOfCustomers() {
        //TODO write your implementation code here:
        int totalNumberOfCustomers = 0;
        Connection conn = null;
        try {
            //TODO write your implementation code here:
            conn = mySQLABC.getConnection();
            PreparedStatement pstat = conn.prepareStatement("SELECT count(*) FROM `ABC Bank`.Customer");
           // pstat.setString(1,customer_username);
            ResultSet rs = pstat.executeQuery();
                  
            while(rs.next())
            {
              totalNumberOfCustomers = rs.getInt(1);
            }
            
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        finally
        {
            if(conn!= null)
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return totalNumberOfCustomers;
    }

    /**
     * Web service operation
     * @param custID
     * @return 
     */
    @WebMethod(operationName = "deleteCustomer")
    public String deleteCustomer(@WebParam(name = "custID") int custID) {
        //TODO write your implementation code here:
        //Sequence is Transaction -> Account -> Customer -> Login
        //delete from `ABC Bank`.Transaction where Account_Number = '6';
        //delete from `ABC Bank`.Account where Cust_ID = '5';
        //delete from `ABC Bank`.Customer where Cust_ID = '5';
        //delete from `ABC Bank`.login where username = 'ankit';
        
        //insert into `ABC Bank`.Transaction (Transaction_ID,Account_Number,Transaction_Type,Timestamp,Transaction_Amount) values (null,6,'Debit',null,21);
        
        Connection conn = null;
        PreparedStatement pstat = null;
        String username = null;
        int accountNumber = 0;
        ResultSet rs = null;
        ResultSet rs1 = null;
        
        
        try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
            
            pstat = conn.prepareStatement("select * from `ABC Bank`.Customer where Cust_ID = ? ");
            pstat.setInt(1,custID);
            rs = pstat.executeQuery();
            while (rs.next())
            {
                username = rs.getString("Cust_Username");
            }
            
            pstat = conn.prepareStatement("select * from `ABC Bank`.Account where Cust_ID = ?");
            pstat.setInt(1,custID);
            rs1 = pstat.executeQuery();
            while(rs1.next())
            {
                accountNumber = rs1.getInt("Account_Number");
            }
            
            pstat = conn.prepareStatement("delete from `ABC Bank`.DirectDebit where AccountNumber = ?");
            pstat.setInt(1, accountNumber);
            pstat.execute();
            
            pstat = conn.prepareStatement("delete from `ABC Bank`.Transaction where Account_Number = ?");
            pstat.setInt(1, accountNumber);
            pstat.execute();
            
             pstat = conn.prepareStatement("delete from `ABC Bank`.Account where Cust_ID = ?");
            pstat.setInt(1, custID);
            pstat.execute();
            
             pstat = conn.prepareStatement("delete from `ABC Bank`.Customer where Cust_ID = ?");
            pstat.setInt(1, custID);
            pstat.execute();
            
             pstat = conn.prepareStatement("delete from `ABC Bank`.login where username = ?");
            pstat.setString(1, username);
            pstat.execute();
            
            conn.commit();
            
        }catch(Exception ex)
        {
             System.out.println(ex.getMessage());
        }
        finally
        {
            if(conn!=null)
            {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerWebService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        
        return "Deleted";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "updateCustomer")
    public String updateCustomer(@WebParam(name = "customerDetails") CustomerProfile customerDetails) {
    
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        PreparedStatement stat2 = null;
        PreparedStatement stat3 = null;
        PreparedStatement stat4 = null;
        PreparedStatement stat5 = null;
        PreparedStatement stat6 = null;
        //PreparedStatement stat = null;
        try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
            
            
            int custID = customerDetails.getCust_ID();
            String firstname = customerDetails.getCust_firstname();
            String lastname = customerDetails.getCust_lastname();
            String addressLine1 = customerDetails.getCust_Addressline1();
            String addressLine2 = customerDetails.getCust_Addressline2();
            String city = customerDetails.getCust_City();
            String contact_number = customerDetails.getCust_Contactnumber();
            String email = customerDetails.getCust_Email();
            
                       
            if(!"".equals(firstname))
            {
                stat = conn.prepareStatement("UPDATE `ABC Bank`.`Customer` SET `Cust_Firstname`= ? WHERE `Cust_ID`= ?");
                stat.setString(1, firstname);
                stat.setInt(2, custID);
                stat.executeUpdate();
            }
            
              if(!"".equals(lastname))
            {
                stat1 = conn.prepareStatement("UPDATE `ABC Bank`.`Customer` SET `Cust_Lastname`= ? WHERE `Cust_ID`= ?");
                stat1.setString(1, lastname);
                stat1.setInt(2, custID);
                stat1.executeUpdate();
            }
              
                if(!"".equals(addressLine1))
            {
                stat2 = conn.prepareStatement("UPDATE `ABC Bank`.`Customer` SET `Cust_Addressline1`= ? WHERE `Cust_ID`= ?");
                stat2.setString(1, addressLine1);
                stat2.setInt(2, custID);
                stat2.executeUpdate();
            }
                
                if(!"".equals(addressLine2))
            {
                stat3 = conn.prepareStatement("UPDATE `ABC Bank`.`Customer` SET `Cust_Addressline2`= ? WHERE `Cust_ID`= ?");
                stat3.setString(1, addressLine2);
                stat3.setInt(2, custID);
                stat3.executeUpdate();
            }  
               
                if(!"".equals(city))
            {
                stat4 = conn.prepareStatement("UPDATE `ABC Bank`.`Customer` SET `Cust_City`= ? WHERE `Cust_ID`= ?");
                stat4.setString(1, city);
                stat4.setInt(2, custID);
                stat4.executeUpdate();
            }   
                
                 if(!"".equals(contact_number))
            {
                stat5 = conn.prepareStatement("UPDATE `ABC Bank`.`Customer` SET `Cust_Contactnumber`= ? WHERE `Cust_ID`= ?");
                stat5.setString(1, contact_number);
                stat5.setInt(2, custID);
                stat5.executeUpdate();
            }   
                 
                  if(!"".equals(email))
            {
                stat6 = conn.prepareStatement("UPDATE `ABC Bank`.`Customer` SET `Cust_Email`= ? WHERE `Cust_ID`= ?");
                stat6.setString(1, email);
                stat6.setInt(2, custID);
                stat6.executeUpdate();
            }   
            
                  conn.commit();
                  
                  
        }catch(Exception e)
        {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(CustomerWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }    //finally block used to close resources
      try{
         if(stat!=null)
         {stat.close();
            conn.close();}
      }catch(SQLException se){
      }// do nothing
      try{
         if(conn!=null)
            conn.close();
      }catch(SQLException se){
         se.printStackTrace();
      }
                
        return "Updated";
    }

}
