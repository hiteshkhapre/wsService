/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import customer.Account;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.naming.directory.DirContext;
import javax.sql.DataSource;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 *
 * @author hiteshkhapre
 */
@WebService(serviceName = "DirectDebitWebService")
public class DirectDebitWebService {

 @Resource(name = "mySQLABC")
    private DataSource mySQLABC;

    /**
     * Web service operation
     * @param accountNumber
     * @param amount
     * @param date
     * @return 
     */
    @WebMethod(operationName = "insertDDData")
    public String insertDDData(@WebParam(name = "accountNumber") int accountNumber, @WebParam(name = "amount") double amount, @WebParam(name = "date") XMLGregorianCalendar date) {
        //TODO write your implementation code here:
        
         Connection conn = null;
        PreparedStatement stat = null;
        
         try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
                 
          //Create a record in directdebit table with current timestamp.
           stat = conn.prepareStatement("insert into `ABC Bank`.DirectDebit (DirectDebitID,AccountNumber,Amount,DirectDebitDate,Timestamp)"
                   + " values "
                   + "(?,?,?,?,?)");
           stat.setString(1, null);
           stat.setInt(2,accountNumber);
           stat.setDouble(3,amount);
           //stat.setString(4, date);
           
           java.util.Date utilDate = date.toGregorianCalendar().getTime();
            java.sql.Date sqlDt = new java.sql.Date(utilDate.getTime()); 


           stat.setDate(4,sqlDt);
           stat.setTimestamp(5,null);
           
           stat.executeUpdate();
           conn.commit();
            //Pop up the success message 
      
        }catch(Exception ex)
        {
             System.out.println(ex.getMessage());
              try {
                  conn.rollback();
              } catch (SQLException ex1) {
                  Logger.getLogger(DirectDebitWebService.class.getName()).log(Level.SEVERE, null, ex1);
              }
        }finally
        {
            if(conn!=null)
            {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DirectDebitWebService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return "Inserted";
       
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
                Logger.getLogger(DirectDebitWebService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return account;
    }
    
    /**
     * Web service operation
     */
    @WebMethod(operationName = "directDebit")
    public String directDebit(@WebParam(name = "custID") int custID, @WebParam(name = "accountNumber") int accountNumber, @WebParam(name = "amount") double amount) {
        //TODO write your implementation code here:
         Connection conn = null;
        PreparedStatement stat = null;
        
        try
        {
            conn = mySQLABC.getConnection();
            conn.setAutoCommit(false);
             //Get account Number and get the balance.
           Account account = getAccountDetails(custID);
           
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
           stat.setString(3,"DirectDebit");
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
                  Logger.getLogger(DirectDebitWebService.class.getName()).log(Level.SEVERE, null, ex1);
              }
        }finally
        {
            if(conn!=null)
            {
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Logger.getLogger(DirectDebitWebService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        
        return "DirectDebited";
    }   

}
