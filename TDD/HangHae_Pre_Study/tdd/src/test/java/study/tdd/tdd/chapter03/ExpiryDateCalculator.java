package study.tdd.tdd.chapter03;

import java.time.LocalDate;
import java.time.YearMonth;

public class ExpiryDateCalculator {

    public LocalDate calculateExpiryDate(PayData payData){
        // ** 금액을 나누어서 구독한 개월수를 구함
        int addedMonths = payData.getPayAmount() == 100_000 ? 12 : payData.getPayAmount() / 10_000;

        // ** 처음 구독한 날짜가 존재하는 경우 -> 만료일을 추가적으로 계산
        if(payData.getFirstBillingDate() != null ) {
            return expiryDateUsingFirstBillingDate(payData,addedMonths);
        }
        // ** 처음 구독한 날짜가 존재하지 않는 경우
        else{
            return payData.getBillingDate().plusMonths(addedMonths);
        }
    }

    public LocalDate expiryDateUsingFirstBillingDate(PayData payData,int addedMonths){

        LocalDate candidateExp = payData.getBillingDate().plusMonths(addedMonths);


        // 처음 구독한 달의 마지막 날
        final int dayOffFistBilling = payData.getFirstBillingDate().getDayOfMonth();

        // 처음 구독한 달과 만료 달의 마지막 날이 다를 경우
        if(dayOffFistBilling != candidateExp.getDayOfMonth()){
            // ** 구독 만료하는 달의 마지막 날 수
            final int dayLenOfCandiMon = YearMonth.from(candidateExp).lengthOfMonth();

            // ** 처음 구독한 날과 만료하는 달의 날 중 적은 달을 반환
            if(dayLenOfCandiMon < dayOffFistBilling ){
                return candidateExp.withDayOfMonth(dayLenOfCandiMon);
            }
            return candidateExp.withDayOfMonth(dayOffFistBilling);
        }
        else{
            return candidateExp;
        }
    }
}
