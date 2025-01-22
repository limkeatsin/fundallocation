package util;

import model.Deposit;
import model.DepositPlan;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class Util {

    public static final int DISPLAY_PRECISION = 2;
    public static final int CALCULATE_PRECISION = 4;

    public static double formatPrecision(Double val, int precision ){
        return BigDecimal.valueOf(val).setScale(precision, RoundingMode.HALF_UP).doubleValue();
    }

    public static double getTotalDeposits(List<Deposit> deposits){
        Optional<Double> optRawTotalDeposits = deposits.stream().map(Deposit::getAmount).reduce(Double::sum);

        return formatPrecision(optRawTotalDeposits.orElse(0.00),DISPLAY_PRECISION);
    }

    public static double getTotalPlannedDeposits(List<DepositPlan> depositPlans){
        double totalPlannedDeposits = 0.00;

        totalPlannedDeposits = depositPlans.stream()
                .map(DepositPlan::totalAmount)
                .reduce(Double::sum)
                .orElse(0.00);

        return totalPlannedDeposits;
    }

    public static double getDecimals(double amount){
        BigDecimal bDecimal = new BigDecimal(amount);
        int integerPart = bDecimal.intValue();

        return bDecimal.subtract(new BigDecimal(integerPart)).doubleValue();
    }
}
