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
import java.sql.Statement;
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
     */
    @WebMethod(operationName = "getMyProfile")
    public CustomerProfile getMyProfile(@WebParam(name = "customer_username") String customer_username) {
        //TODO write your implementation code here:
        CustomerProfile cust_profile = null;
        try {
            //TODO write your implementation code here:
            Connection conn = mySQLABC.getConnection();
            PreparedStatement pstat = conn.prepareStatement("SELECT * FROM `ABC Bank`.Customer where Cust_Username = ?");
            pstat.setString(1,customer_username);
            ResultSet rs = pstat.executeQuery();
            cust_profile = new CustomerProfile();
            
            while(rs.next())
            {
               cust_profile.setCust_ID(rs.getString("cust_ID"));
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
        return cust_profile;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getAccountDetails")
    public Account getAccountDetails(@WebParam(name = "custID") String custID) {
        //TODO write your implementation code here:
        
        Account account = null;
        try {
            //TODO write your implementation code here:
            Connection conn = mySQLABC.getConnection();
            PreparedStatement pstat = conn.prepareStatement("SELECT * FROM `ABC Bank`.Account where Cust_ID = ?");
            pstat.setString(1,custID);
            ResultSet rs = pstat.executeQuery();
            account = new Account();
            
            while(rs.next())
            {
             account.setAccountNumber(rs.getString("Account_Number"));
             account.setAccountType(rs.getString("Account_Type"));
             account.setAccountBalance(rs.getString("Account_Balance"));
             account.setAccountStatus(rs.getString("Account_Status"));
             
            }
            
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return account;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "addCustomer")
    public String addCustomer(@WebParam(name = "parameter") CustomerProfile customerDetails) {
        //TODO write your implementation code here:
        Connection conn = null;
        Statement stat = null;
        try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
            stat = conn.createStatement();
            
            
            String firstname = customerDetails.getCust_firstname();
            String lastname = customerDetails.getCust_lastname();
            String addressLine1 = customerDetails.getCust_Addressline1();
            String addressLine2 = customerDetails.getCust_Addressline2();
            String city = customerDetails.getCust_City();
            String contact_number = customerDetails.getCust_Contactnumber();
            String email = customerDetails.getCust_Email();
            String username = customerDetails.getCust_Username();
            
            String accountnumber = customerDetails.getAccountType();
            String accountbalance = customerDetails.getAccountBalance();
            
            
            
            
            String sql = "insert into `ABC Bank`.Customer values(null,firstname,lastname,addressLine1,addressLine2,city,contact_number,email.username)";
            //Get the new custID from DB and then insert record in Account table 
                      
                      
            stat.executeUpdate(sql);
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
        return "Inserted";
    }
    }
}
