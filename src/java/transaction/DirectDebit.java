/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transaction;

import java.util.Date;

/**
 *
 * @author hiteshkhapre
 */
public class DirectDebit {
    
    private int accountNumber;
    private int directDebitID;
    private double amount;
    private Date timestamp;
    private Date directDebitDate;

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getDirectDebitID() {
        return directDebitID;
    }

    public void setDirectDebitID(int directDebitID) {
        this.directDebitID = directDebitID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getDirectDebitDate() {
        return directDebitDate;
    }

    public void setDirectDebitDate(Date directDebitDate) {
        this.directDebitDate = directDebitDate;
    }
    
    
    
    
}
