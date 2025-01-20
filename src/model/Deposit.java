package model;

public class Deposit {

    private String refCode;
    private Double amount;

    public Deposit(String refId, Double amountToInvest){
        refCode = refId;
        amount = amountToInvest;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
