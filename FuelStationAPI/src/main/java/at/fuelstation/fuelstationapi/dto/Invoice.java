package at.fuelstation.fuelstationapi.dto;

public class Invoice {
    public String customerName;
    public int customerID;
    public Invoice(){

    }

    public Invoice(
            String cusName,
            int cusID
    ){
        this.customerName = cusName;
        this.customerID = cusID;
    }

    public Invoice(
            int cusID
    ){
        this.customerID = cusID;
    }
}
