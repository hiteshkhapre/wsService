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
        try {
            //TODO write your implementation code here:
            Connection conn = mySQLABC.getConnection();
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
        return cust_profile;
    }

    /**
     * Web service operation
     * @param custID
     * @return 
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
             account.setAccountNumber(rs.getInt("Account_Number"));
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
     * @param customerDetails
     * @return 
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

    /**
     * Web service operation
     * @param username
     * @return 
     */
    @WebMethod(operationName = "insertOperation")
    public String insertOperation(@WebParam(name = "username") String username) {
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
        return "Inserted";
        }
    }

    /**
     * Web service operation
     * @return 
     */
    @WebMethod(operationName = "getTotalNumberOfCustomers")
    public Integer getTotalNumberOfCustomers() {
        //TODO write your implementation code here:
        int totalNumberOfCustomers = 0;
        
        try {
            //TODO write your implementation code here:
            Connection conn = mySQLABC.getConnection();
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
        
        
        return null;
    }
}
