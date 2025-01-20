import model.DepositPlan;
import model.Deposit;

import java.util.*;

public class Main {

    public static enum TransactionType {
        ONEOFF, FREQ;
    }

    public static void main(String[] args) {
        //Set up a naive investment amount
        Deposit inv1 = new Deposit("High Risk", 300.47);
        Deposit inv2 = new Deposit("Retirement", 220.50);
        DepositPlan alloc = new DepositPlan();
        alloc.setFundList(new ArrayList<>(Arrays.asList(inv1,inv2)));

        ArrayList<DepositPlan> depositPlans = new ArrayList<DepositPlan>();
        depositPlans.add(alloc);
        ArrayList<Double> deposits = new ArrayList<Double>(Arrays.asList(300.56, 200.50));

        List<HashMap<String, Double>> finalAlloc = allocateFunds(depositPlans, deposits);

        finalAlloc.forEach(System.out::println);
    }

    public static List<HashMap<String, Double>> allocateFunds(ArrayList<DepositPlan> plans, List<Double> deposits){

    return new ArrayList<HashMap<String, Double>>();
    }

}