import model.DepositPlan;
import model.Deposit;
import model.Portfolio;
import model.TransactionType;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Math.abs;
import static util.Util.*;

public class Main {

    private static final int MAX_PLAN_COUNT = 2;

    private static final ArrayList<Deposit> deposits;
    private static final ArrayList<DepositPlan> plans;
    private static final ArrayList<DepositPlan> errorNeousDepositPlans = new ArrayList<>();


    //Naive set up plans & deposit amount
    static {
        Deposit inv1 = new Deposit("CustomerA", 10500.00);
        Deposit inv2 = new Deposit("CustomerA", 300.00);
        deposits = new ArrayList<>(Arrays.asList(inv1, inv2));

        DepositPlan depositPlan1 = new DepositPlan("CustomerA", "High Risk", 10000.00, TransactionType.ONEOFF);
        depositPlan1.addDeposit("CustomerA", "Retirement", 500.00, TransactionType.ONEOFF);
        DepositPlan depositPlan2 = new DepositPlan("CustomerA", "Retirement", 100.00, TransactionType.MONTHLY);
        plans = new ArrayList<>(Arrays.asList(depositPlan1, depositPlan2));

    }

    public static void main(String[] args) {


        //Allocate funds from bank transfer
        List<HashMap<String, Double>> finalAlloc = allocateFunds(plans, deposits);

        //List<HashMap<String, Double>> redistributed = redistributeFunds2(plans, deposits, finalAlloc);
        //Print out allocated funds
        System.out.println("=========Final allocation=============");
        finalAlloc.forEach(System.out::println);
        //redistributed.forEach(System.out::println);
    }

    /**
     * The method/function to allocate funds
     * @param depositPlans A list of deposit plans
     * @param deposits A list of deposits made by customer over bank transfer
     * @return A list of allocated funds based on deposits
     */
    public static List<HashMap<String, Double>> allocateFunds(ArrayList<DepositPlan> depositPlans, List<Deposit> deposits){
        ArrayList<HashMap<String, Double>> finalAllocation = new ArrayList<>();

        if(depositPlans != null && deposits != null ){

            if(isExceededTotalPlan(depositPlans)){
                return finalAllocation;//return early
            }
            //Get deposit reference code
            String depositRefCode = deposits.stream().findFirst()
                    .orElseGet(() -> new Deposit("N/A", 0.00)).getRefCode();

            //Get total deposits by customer
//            double totalDeposits = getTotalDeposits(deposits);
//            System.out.println("Total deposits:" + totalDeposits);
//            if(totalDeposits <= 0.00){
//                System.out.println("No deposits to be allocated.");
//                return finalAllocation;//early return if there are no deposits
//            }

            HashMap<String,Double> hMapPortfolio = new HashMap<>();//Portfolios
            for(DepositPlan depositPlan: depositPlans){
                //Check reference Code
                if (!depositRefCode.equals(depositPlan.getReferenceCode())){
                    System.out.println("Reference Code doesn't match.");
                    errorNeousDepositPlans.add(depositPlan);
                    return finalAllocation; //early return if reference code doesn't match
                }

                ArrayList<Portfolio> portfolios = depositPlan.getPlans();//List of Portfolio for each deposit plan
                for(Portfolio portfolio: portfolios){
                    if(!hMapPortfolio.containsKey(portfolio.portfolioName())) {
                        hMapPortfolio.put(portfolio.portfolioName(), portfolio.amount());
                    }
                    else{
                        hMapPortfolio.computeIfPresent(portfolio.portfolioName(), (k, currDeposit) -> portfolio.amount() + currDeposit);
                    }
                    //Check if planned deposit tallies with deposits made
//                    totalDeposits-= portfolio.amount();

                }

            }

            finalAllocation.add(hMapPortfolio); //Add Portfolio and it's allocation
            redistributeFunds2(depositPlans, deposits,  finalAllocation); //Re-distribute funds is there are extras
        }

    return finalAllocation;
    }

    public static List<HashMap<String, Double>> redistributeFunds2(List<DepositPlan> depositPlans, List<Deposit> deposits, List<HashMap<String, Double>> portfolios){

        if(portfolios.isEmpty()){
            return portfolios;//early return if there are no Portfolios
        }

        if(depositPlans != null && deposits != null ) {
            //Get total deposits by customer
            double totalDeposits = getTotalDeposits(deposits);
            double plannedDeposits =  getTotalPlannedDeposits(depositPlans);
            double allocBalance = totalDeposits - plannedDeposits;
            double fmtBalance = formatPrecision(allocBalance, DISPLAY_PRECISION);
            double decimalPart = formatPrecision(getDecimals(fmtBalance),DISPLAY_PRECISION);

            for( HashMap<String, Double> portfolio:portfolios) {
                if(fmtBalance == 0.00){
                    System.out.println("Funds are fully allocated.");
                }
                else if(fmtBalance > 0.00){
                    System.out.println("There are leftover funds of : " + fmtBalance);
                    if(fmtBalance > 1.00 ) {
                        if (decimalPart <= 0.10 && decimalPart != 0.00) {
                            //Distribute the fraction to the first found fund
                            String fundName = portfolio.keySet().stream().findFirst().orElse("N/A");
                            portfolio.computeIfPresent(fundName, (k, currAmt) -> currAmt + decimalPart);
                        }
                        double amtToBeDist = formatPrecision((fmtBalance - decimalPart)/ portfolio.size(), CALCULATE_PRECISION);
                        System.out.println("The amount to be allocate to each fund : " + amtToBeDist + " and " + decimalPart +" to the first fund.");
                        portfolio.keySet().forEach(fund -> {
                            portfolio.computeIfPresent(fund, (k, currValue) -> currValue + amtToBeDist);
                        });
                        double finalAmount = portfolio.values().stream().reduce(Double::sum).orElse(0.00);
                        System.out.println("Redistributed portfolio amount : " + formatPrecision(abs(finalAmount), DISPLAY_PRECISION)
                        + "->Portfolios: " + portfolio);

                    }
                    else{
                        double amtToBeDist = formatPrecision(fmtBalance, CALCULATE_PRECISION);
                        String fundName = portfolio.keySet().stream().findFirst().orElse("N/A");
                        portfolio.computeIfPresent(fundName, (k, currAmt) -> currAmt + amtToBeDist);
                    }

                }
                else {
                    System.out.println("Funds deposited is short of : " + abs(fmtBalance));

                }
            };

        }

        return portfolios;
    }


    public static void redistributeFunds(double allocBalance, HashMap<String, Double> hMap){
        if(hMap.isEmpty()){
            return;//early return if there are no Portfolios
        }
        double fmtBalance = formatPrecision(allocBalance, DISPLAY_PRECISION);
        double decimalPart = formatPrecision(getDecimals(fmtBalance),DISPLAY_PRECISION);

        if(fmtBalance == 0.00){
            System.out.println("Funds are fully allocated.");
        }
        else if(fmtBalance > 0.00){
            System.out.println("There are leftover funds of : " + fmtBalance);
            if(fmtBalance > 1.00 ) {
                if (decimalPart <= 0.10 && decimalPart != 0.00) {
                    //Distribute the fraction to the first found fund
                    String fundName = hMap.keySet().stream().findFirst().orElse("N/A");
                    hMap.computeIfPresent(fundName, (k, currAmt) -> currAmt + decimalPart);
                }
                double amtToBeDist = formatPrecision((fmtBalance - decimalPart)/ hMap.size(), CALCULATE_PRECISION);
                System.out.println("The amount to be allocate to each fund : " + amtToBeDist + " and " + decimalPart +" to the first fund.");
                hMap.keySet().forEach(portfolio -> {
                    hMap.computeIfPresent(portfolio, (k, currValue) -> currValue + amtToBeDist);
                });
                double finalAmount = hMap.values().stream().reduce(Double::sum).orElse(0.00);
                System.out.println("Redistributed amount : " + formatPrecision(abs(finalAmount), DISPLAY_PRECISION));

            }
            else{
                double amtToBeDist = formatPrecision(fmtBalance, CALCULATE_PRECISION);
                String fundName = hMap.keySet().stream().findFirst().orElse("N/A");
                hMap.computeIfPresent(fundName, (k, currAmt) -> currAmt + amtToBeDist);
            }

        }
        else {
            System.out.println("Funds deposited is short of : " + abs(fmtBalance));

        }

    }

    public static boolean isExceededTotalPlan(ArrayList<DepositPlan> depositPlans){
        //Check number of plans
        if(depositPlans.size() <= MAX_PLAN_COUNT){
            AtomicInteger monthPlanCount = new AtomicInteger(0);
            AtomicInteger oneOffPlanCount = new AtomicInteger(0);
            depositPlans.forEach( depositPlan -> {
                if(depositPlan.getPlanType() == TransactionType.ONEOFF){
                    oneOffPlanCount.incrementAndGet();
                }
                else if(depositPlan.getPlanType() == TransactionType.MONTHLY){
                    monthPlanCount.incrementAndGet();
                }
            });
            return oneOffPlanCount.intValue() > 1 || monthPlanCount.intValue() > 1;
        }
        else{
            errorNeousDepositPlans.addAll(depositPlans);
            return false;//return early
        }
    }
}