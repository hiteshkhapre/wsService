/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

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
@WebService(serviceName = "RateWebService")
public class RateWebService {
    @Resource(name = "mySQLABC")
    private DataSource mySQLABC;
   
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "exchange")
    public String exchange(@WebParam(name = "surname") String surname) {
      try {
            //TODO write your implementation code here:
            Connection conn = mySQLABC.getConnection();
            PreparedStatement pstat = conn.prepareStatement("select CustName from Customer where Cust_Surname = ?");
            pstat.setString(1,surname);
            ResultSet rs = pstat.executeQuery();
            rs.next();
            String name = rs.getString(1);
            return name;                    
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        
        
        return "default String";
    }
    


}
