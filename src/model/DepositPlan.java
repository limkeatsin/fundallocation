package model;

import java.util.ArrayList;

public class DepositPlan {

    private Double totalAmount = 0.00D;
    private ArrayList<Deposit> fundList = new ArrayList<Deposit>();

    public DepositPlan(){}

    public DepositPlan(ArrayList<Deposit> funds){
        this.fundList = funds;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public ArrayList<Deposit> getFundList() {
        return fundList;
    }

    public void setFundList(ArrayList<Deposit> fundList) {
        this.fundList = fundList;
    }

    public void getTotalAllocation(){}
}


