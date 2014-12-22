/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author hiteshkhapre
 */
@WebService(serviceName = "CustomerWebService")
public class CustomerWebService {

    /**
     * Web service operation
     */
    @WebMethod(operationName = "getNameDetails")
    public String getNameDetails(@WebParam(name = "customerID") String customerID) {
        //TODO write your implementation code here:
        
        
        
        
        return null;
    }
}
