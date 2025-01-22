import model.Deposit;
import model.DepositPlan;
import model.TransactionType;
import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static util.Util.*;

public class FundsAllocationTest {

    private static final ArrayList<DepositPlan> plans = new ArrayList<>();
    private static final ArrayList<Deposit> deposits = new ArrayList<>();

    @BeforeEach
    public void setUp(){
        //Set up initial deposits
        Deposit inv1 = new Deposit("CustomerA", 10500.00);//First deposit
        deposits.add(inv1);

        //Set up initial deposit plans
        DepositPlan depositPlan = new DepositPlan("CustomerA", "High Risk", 10000.00, TransactionType.ONEOFF);
        depositPlan.addDeposit("CustomerA", "Retirement", 500.00, TransactionType.ONEOFF);
        plans.add(depositPlan);
    }

    @AfterEach
    public void cleanUp(){
        plans.clear();
        deposits.clear();
    }

    /**
     * Total deposits tally with total amount of deposit plan
     */
    @Test
    public void testTotalDepositsTally(){

        System.out.println("=================================================================================");
        Deposit inv = new Deposit("CustomerA", 100.00);// Second deposit
        deposits.add(inv);

        DepositPlan depositPlan = new DepositPlan("CustomerA", "Retirement", 100.00, TransactionType.MONTHLY);
        plans.add(depositPlan);

        double totalDeposits = getTotalDeposits(deposits);
        double plannedDeposits =  getTotalPlannedDeposits(plans);
        System.out.println("Total deposits: "+ totalDeposits);
        System.out.println("Total planned deposits: "+ plannedDeposits);

        List<HashMap<String,Double>> list = Main.allocateFunds(plans, deposits);
        double totalAllocated = list.stream().flatMap(map -> map.values()
                .stream()).mapToDouble(Double::doubleValue).sum();

        System.out.println("TEST-testTotalDepositsTally - Final allocation:" + list + ".Total : " + totalAllocated);
        assertEquals(totalDeposits,totalAllocated);

    }

    /**
     * Total deposits amount more than deposit plan
     */
    @Test
    public void testTotalDepositsExceededFractional() {

        System.out.println("=================================================================================");
        Deposit inv1 = new Deposit("CustomerA", 100.00);// Second deposit
        Deposit inv2 = new Deposit("CustomerA", 1.09);//Extra deposits
        deposits.add(inv1);
        deposits.add(inv2);

        DepositPlan depositPlan = new DepositPlan("CustomerA", "Retirement", 100.00, TransactionType.MONTHLY);
        plans.add(depositPlan);

        double totalDeposits = getTotalDeposits(deposits);
        double plannedDeposits =  getTotalPlannedDeposits(plans);
        System.out.println("Total deposits: "+ totalDeposits);
        System.out.println("Total planned deposits: "+ plannedDeposits);
        assertTrue(totalDeposits > plannedDeposits);

        List<HashMap<String,Double>> list = Main.allocateFunds(plans, deposits);
        double totalAllocated = list.stream().flatMap(map -> map.values()
                .stream()).mapToDouble(Double::doubleValue).sum();

        System.out.println("TEST-testTotalDepositsExceededFractional - Final allocation:" + list + ".Total : " + totalAllocated);
        assertEquals(totalDeposits, totalAllocated);
    }

    /**
     * Total deposits amount more than deposit plan
     */
    @Test
    public void testTotalDepositsExceeded() {

        System.out.println("=================================================================================");
        Deposit inv1 = new Deposit("CustomerA", 100.00);// Second deposit
        Deposit inv2 = new Deposit("CustomerA", 70.00);//Extra deposits
        deposits.add(inv1);
        deposits.add(inv2);

        DepositPlan depositPlan = new DepositPlan("CustomerA", "Retirement", 150.00, TransactionType.MONTHLY);
        plans.add(depositPlan);

        double totalDeposits = getTotalDeposits(deposits);
        double plannedDeposits =  getTotalPlannedDeposits(plans);
        System.out.println("Total deposits: "+ totalDeposits);
        System.out.println("Total planned deposits: "+ plannedDeposits);
        assertTrue(totalDeposits > plannedDeposits);

        List<HashMap<String,Double>> list = Main.allocateFunds(plans, deposits);
        double totalAllocated = list.stream().flatMap(map -> map.values()
                .stream()).mapToDouble(Double::doubleValue).sum();

        System.out.println("TEST-testTotalDepositsExceeded - Final allocation:" + list + ".Total allocated: " + totalAllocated);

        assertEquals(totalDeposits, totalAllocated);
    }

    /**
     * Test total deposits amount less than deposit plan
     */
    @Test
    public void testTotalDepositsLess() {

        System.out.println("=================================================================================");
        DepositPlan depositPlan = new DepositPlan("CustomerA", "Retirement-2", 30.00, TransactionType.MONTHLY);
        plans.add(depositPlan);

        double totalDeposits = getTotalDeposits(deposits);
        System.out.println("Total deposits: "+ totalDeposits);
        double plannedDeposits =  getTotalPlannedDeposits(plans);
        System.out.println("Total planned deposits: "+ plannedDeposits);
        List<HashMap<String,Double>> list = Main.allocateFunds(plans, deposits);

        double totalAllocated = list.stream().flatMap(map -> map.values()
                .stream()).mapToDouble(Double::doubleValue).sum();

        System.out.println("TEST-testTotalDepositsLess - Final allocation:" + list + ".Total : " + totalAllocated);
        assertTrue(totalDeposits < totalAllocated);
    }

    /**
     * Test redistributeFunds() method
     */
    @Test
    public void tesRedistributeFunds() {

        System.out.println("=================================================================================");
        Deposit inv1 = new Deposit("CustomerA", 300.00);// 2nd deposit
        Deposit inv2 = new Deposit("CustomerA", 300.00);// 3rd deposit
        deposits.add(inv1);
        deposits.add(inv2);

        DepositPlan depositPlan = new DepositPlan("CustomerA", "Retirement", 400.00, TransactionType.MONTHLY);
        plans.add(depositPlan);

        double totalDeposits = getTotalDeposits(deposits);
        double plannedDeposits =  getTotalPlannedDeposits(plans);
        System.out.println("Total deposits: "+ totalDeposits);
        System.out.println("Total planned deposits: "+ plannedDeposits);


        List<HashMap<String,Double>> list = Main.allocateFunds(plans, deposits);

        double totalAllocated = list.stream().flatMap(map -> map.values()
                .stream()).mapToDouble(Double::doubleValue).sum();

        HashMap<String,Double> hMapPortfolio = new HashMap<>();
        hMapPortfolio.put("High Risk", 1000.00);
        hMapPortfolio.put("Retirement", 500.00);
        hMapPortfolio.put("Retirement-2", 300.00);

        List<HashMap<String,Double>> explist = new ArrayList<>();
        explist.add(hMapPortfolio);

        //Main.redistributeFunds(balanceAmt, hMapPortfolio);
        List<HashMap<String,Double>>  finalList = Main.redistributeFunds2(plans, deposits, explist);
        //Main.redistributeFunds2(balanceAmt, hMapPortfolio);
        System.out.println("TEST-tesRedistributeFunds - Final allocation:" + finalList + ".Total : " + totalAllocated);
        assertEquals(totalDeposits,totalAllocated);

    }

    /**
     * Plan count. There should be only 1 one-time and/or 1 monthly (MAX 2 plans in total)
     */
    @Test
    public void testDepositPlanCount(){

        System.out.println("=================================================================================");
        ArrayList<DepositPlan> depositPlans = new ArrayList<>();
        DepositPlan depositPlan1 = new DepositPlan("CustomerA", "Retirement", 430.00, TransactionType.MONTHLY);
        DepositPlan depositPlan2 = new DepositPlan("CustomerA", "Retirement-2", 30.00, TransactionType.ONEOFF);
        depositPlans.add(depositPlan1);
        depositPlans.add(depositPlan2);
        assertFalse(Main.isExceededTotalPlan(depositPlans));
    }

    @Test
    public void testGetDecimals(){

        System.out.println("=================================================================================");
        double val1 = 1.01;
        double val2 = 3.52;

        double decVal1 = formatPrecision(getDecimals(val1),DISPLAY_PRECISION);
        double decVal2 = formatPrecision(getDecimals(val2) ,DISPLAY_PRECISION);

        assertEquals(0.01,decVal1);
        assertEquals(0.52,decVal2);

        System.out.println("Decimal part of '" + val1 + "' is '" + decVal1 +"'");
        System.out.println("Decimal part of '" + val2 + "' is '" + decVal2 +"'");
    }
}
