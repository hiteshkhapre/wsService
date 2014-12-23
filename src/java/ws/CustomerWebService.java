/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

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
}
