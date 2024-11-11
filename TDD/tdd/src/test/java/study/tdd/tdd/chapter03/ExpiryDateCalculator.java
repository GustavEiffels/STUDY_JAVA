package study.tdd.tdd.chapter03;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData){
        int addedMonths = payData.getPayAmount() / 10_000;

        if(payData.getFirstBillingDate() != null ) {
            return expirt
        }
        return payData.getBillingDate().plusMonths(addedMonths);
    }

    public LocalDate expiryDateUsingFirstBillingDate(PayData payData,int addedMonths){
        LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);

        final int dayOffFistBilling = payData.getFirstBillingDate().getDayOfMonth();


        if(Offs)
    }
}
