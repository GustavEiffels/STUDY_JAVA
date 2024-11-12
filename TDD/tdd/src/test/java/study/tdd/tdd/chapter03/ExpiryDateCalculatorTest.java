package study.tdd.tdd.chapter03;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExpiryDateCalculatorTest {

    // ** 중복 제거를 위한 코드
    private void assertExpiryDate(PayData payData,LocalDate expectedExpiryDate){
        ExpiryDateCalculator cal = new ExpiryDateCalculator();
        LocalDate realExpiryDate = cal.calculateExpiryDate(payData);
        assertEquals(expectedExpiryDate,realExpiryDate);
    }

    @Test
    void  pay_TenThousand_ExpiryDate_After_One_Month(){
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,3,1))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,4,1));

        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,5,5))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,6,5));
    }

    @Test
    void pay_date_isNotSame_with_AfterMonth_ExpiryDate(){
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,1,31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,2,28));

        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,5,31))
                        .payAmount(10_000)
                        .build(),
                LocalDate.of(2019,6,30));
    }

    @Test
    void pay10_000Won_different_Between_FistPayDate_And_NextPayDate(){
        PayData payData = PayData.builder()
                .firstBillingDate(LocalDate.of(2019,1,31))
                .billingDate(LocalDate.of(2019,2,28))
                .payAmount(10_000)
                .build();

        assertExpiryDate(payData,LocalDate.of(2019,3,31));
    }

    @Test
    void payOver_20_000Won_CalculateExpiryDate_From_Money(){
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,3,1))
                        .payAmount(20_000)
                        .build(),
                LocalDate.of(2019,5,1));

        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,3,1))
                        .payAmount(30_000)
                        .build(),
                LocalDate.of(2019,6,1));
    }


    @Test
    void payOver_20_000Won_When_firstExpiryMonthDate_IsNotSameWith_thisMonthExpiryDate(){
        assertExpiryDate(
                PayData.builder()
                        .firstBillingDate(LocalDate.of(2019,1,31))
                        .billingDate(LocalDate.of(2019,2,28))
                        .payAmount(20_000)
                        .build(), LocalDate.of(2019,4,30));
    }

    @Test
    void pay100_000Won_Subscribe_OneYear(){
        assertExpiryDate(
                PayData.builder()
                        .billingDate(LocalDate.of(2019,1,28))
                        .payAmount(100_000)
                        .build(),
                LocalDate.of(2020,1,28));
    }
}

