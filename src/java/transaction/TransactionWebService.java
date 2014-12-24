/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import customer.Account;
import static java.lang.Double.sum;
import java.math.BigInteger;
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
@WebService(serviceName = "TransactionWebService")
public class TransactionWebService {
 @Resource(name = "mySQLABC")
    private DataSource mySQLABC;
   
    /**
     * Web service operation
     * @param amount
     * @param custID
     * @return 
     */
    @WebMethod(operationName = "depositMoney")
    public String depositMoney(@WebParam(name = "amount") Double amount, @WebParam(name = "custID") int custID) {
        //TODO write your implementation code here:
        
          Connection conn = null;
        PreparedStatement stat = null;
        
        try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
             //Get account Number and get the balance.
           Account account = getAccountDetails(custID);
           
           int accountNumber = account.getAccountNumber();
           Double oldAccountBalance = account.getAccountBalance();
           
           //Add the deposit amount to the balance 
           Double newAccountBalance = sum(oldAccountBalance, amount);
           
          
            //Create a Credit record in transaction table with current timestamp.
           stat = conn.prepareStatement("insert into `ABC Bank`.Transaction (Transaction_ID,Account_Number,Transaction_Type,"
                   + "Timestamp,Transaction_Amount) values "
                   + "(?,?,?,?,?)");
           stat.setString(1, null);
           stat.setInt(2,accountNumber);
           stat.setString(3,"Credit");
           stat.setTimestamp(4,null);
           stat.setDouble(5, amount);
           stat.executeUpdate();
            
            //Store the new balance in database
           stat = conn.prepareStatement("UPDATE `ABC Bank`.`Account` SET `Account_Balance`= ? WHERE `Account_Number`= ? ");
           stat.setDouble(1,newAccountBalance);
           stat.setDouble(2, accountNumber);
            stat.executeUpdate();
           conn.commit();
            //Pop up the success message 
      
        }catch(Exception ex)
        {
             System.out.println(ex.getMessage());
              try {
                  conn.rollback();
              } catch (SQLException ex1) {
                  Logger.getLogger(TransactionWebService.class.getName()).log(Level.SEVERE, null, ex1);
              }
        }finally
        {
            if(conn!=null)
            {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TransactionWebService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return "Deposited";
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
                Logger.getLogger(TransactionWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return account;
    }

    /**
     * Web service operation
     * @param custID
     * @param amount
     * @return 
     */
    @WebMethod(operationName = "withdrawMoney")
    public String withdrawMoney(@WebParam(name = "custID") int custID, @WebParam(name = "amount") double amount) {
       
           Connection conn = null;
        PreparedStatement stat = null;
        
        try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
             //Get account Number and get the balance.
           Account account = getAccountDetails(custID);
           
           int accountNumber = account.getAccountNumber();
           Double oldAccountBalance = account.getAccountBalance();
           Double newAccountBalance;
           
           if(oldAccountBalance.compareTo(amount) > 0 || oldAccountBalance.compareTo(amount) == 0)
           {
               //compute new balance
               newAccountBalance = oldAccountBalance-amount;
           }
           else
           {
               return "Not Sufficient Balance.";
           }
           
           //Create a Debit record in transaction table with current timestamp.
           stat = conn.prepareStatement("insert into `ABC Bank`.Transaction (Transaction_ID,Account_Number,Transaction_Type,"
                   + "Timestamp,Transaction_Amount) values "
                   + "(?,?,?,?,?)");
           stat.setString(1, null);
           stat.setInt(2,accountNumber);
           stat.setString(3,"Debit");
           stat.setTimestamp(4,null);
           stat.setDouble(5, amount);
           stat.executeUpdate();
            
            //Store the new balance in database
           stat = conn.prepareStatement("UPDATE `ABC Bank`.`Account` SET `Account_Balance`= ? WHERE `Account_Number`= ? ");
           stat.setDouble(1,newAccountBalance);
           stat.setDouble(2, accountNumber);
            stat.executeUpdate();
           conn.commit();
            //Pop up the success message 
           
        }catch(Exception ex)
        {
             System.out.println(ex.getMessage());
              try {
                  conn.rollback();
              } catch (SQLException ex1) {
                  Logger.getLogger(TransactionWebService.class.getName()).log(Level.SEVERE, null, ex1);
              }
        }finally
        {
            if(conn!=null)
            {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(TransactionWebService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return "Withdrawn";
    }

}
