/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package customer;

import java.math.BigInteger;

/**
 *
 * @author hiteshkhapre
 */
public class CustomerProfile {
    
     private int cust_ID;
            private String cust_firstname;
            private String cust_lastname;
            private String cust_Addressline1;
            private String cust_Addressline2;
            private String cust_City;
            private String cust_Contactnumber;
            private String cust_Email; 
            private String cust_Username;
            private String cust_Password;
           

 
             private int accountNumber;
    private String accountType;
    private Double accountBalance;
    private String accountStatus;

    public int getCust_ID() {
        return cust_ID;
    }

    public void setCust_ID(int cust_ID) {
        this.cust_ID = cust_ID;
    }

    public String getCust_firstname() {
        return cust_firstname;
    }

    public void setCust_firstname(String cust_firstname) {
        this.cust_firstname = cust_firstname;
    }

    public String getCust_lastname() {
        return cust_lastname;
    }

    public void setCust_lastname(String cust_lastname) {
        this.cust_lastname = cust_lastname;
    }

    public String getCust_Addressline1() {
        return cust_Addressline1;
    }

    public void setCust_Addressline1(String cust_Addressline1) {
        this.cust_Addressline1 = cust_Addressline1;
    }

    public String getCust_Addressline2() {
        return cust_Addressline2;
    }

    public void setCust_Addressline2(String cust_Addressline2) {
        this.cust_Addressline2 = cust_Addressline2;
    }

    public String getCust_City() {
        return cust_City;
    }

    public void setCust_City(String cust_City) {
        this.cust_City = cust_City;
    }

    public String getCust_Contactnumber() {
        return cust_Contactnumber;
    }

    public void setCust_Contactnumber(String cust_Contactnumber) {
        this.cust_Contactnumber = cust_Contactnumber;
    }

    public String getCust_Email() {
        return cust_Email;
    }

    public void setCust_Email(String cust_Email) {
        this.cust_Email = cust_Email;
    }
    
     public String getCust_Username() {
        return cust_Username;
    }

    public void setCust_Username(String cust_Username) {
        this.cust_Username = cust_Username;
    }

    
    public String getCust_Password() {
        return cust_Password;
    }

    public void setCust_Password(String cust_Password) {
        this.cust_Password = cust_Password;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
    
}
