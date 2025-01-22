package model;

public class Portfolio {

    private String portfolioName;
    private Double amount = 0.00;
    private final String referenceCode;

    public Portfolio(String refCode, String fundName, Double investAmount){
        this.portfolioName = fundName;
        this.amount = investAmount;
        this.referenceCode = refCode;
    }

    public String portfolioName() {
        return portfolioName;
    }

    public Double amount() {
        return amount;
    }

    public String referenceCode() {
        return referenceCode;
    }
}
