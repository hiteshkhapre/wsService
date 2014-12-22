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
     * Web service operation
     */
    @WebMethod(operationName = "getPassword")
    public String getPassword(@WebParam(name = "username") String username) {
        //TODO write your implementation code here:
         try {
            //TODO write your implementation code here:
            Connection conn = mySQLABC.getConnection();
            PreparedStatement pstat = conn.prepareStatement("select password from login where username = ?");
            pstat.setString(1,username);
            ResultSet rs = pstat.executeQuery();
            rs.next();
            if(rs.getString(1) == null)
            {
                return "No USER found.";
            } else
            {
                String password = rs.getString(1);
                return password;  
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
         return "No USER found.";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getTypeofUser")
    public String getTypeofUser(@WebParam(name = "username") String username) {
        //TODO write your implementation code here:
         //TODO write your implementation code here:
         try {
            //TODO write your implementation code here:
            Connection conn = mySQLABC.getConnection();
            PreparedStatement pstat = conn.prepareStatement("SELECT login_type FROM `ABC Bank`.login where username = ?");
            pstat.setString(1,username);
            ResultSet rs1 = pstat.executeQuery();
            rs1.next();
            if(rs1.getString(1) == null)
            {
                return "No USER found.";
            } else
            {
                String typeOfUser = rs1.getString(1);
                return typeOfUser;  
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
         return "No USER found.";
    }
    


}
