package model;

import java.util.ArrayList;
import java.util.Optional;

import static util.Util.formatPrecision;

public class DepositPlan {

    private final ArrayList<Portfolio> depositPlans = new ArrayList<>();
    private TransactionType planType;
    private String referenceCode;


    public DepositPlan(String refCode, String name, Double amount, TransactionType type){
        addDeposit(refCode, name, amount, type);
    }

    public void addDeposit(String refCode, String name, Double amount, TransactionType type){
        this.referenceCode = refCode;
        this.planType = type;
        Portfolio plan = new Portfolio(refCode, name, amount);
        this.depositPlans.add(plan);
    }

    public ArrayList<Portfolio> getPlans() {
        return depositPlans;
    }

    public TransactionType getPlanType() {
        return planType;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public Double totalAmount() {
        Optional<Double> optPortfolios = this.getPlans().stream().map( Portfolio::amount).reduce(Double::sum);

        return formatPrecision(optPortfolios.orElse(0.00),2);
    }
}


