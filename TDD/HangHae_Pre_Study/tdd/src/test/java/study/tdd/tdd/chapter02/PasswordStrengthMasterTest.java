package study.tdd.tdd.chapter02;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PasswordStrengthMasterTest {

    private PasswordStrengthMeter meter = new PasswordStrengthMeter();

    private void assertStrength(String password, PasswordStrength expStr){
        PasswordStrength result = meter.meter(password);
        assertEquals(result,expStr);
    }

    @Test
    void meetsAllCriteria_Then_Strong(){
        assertStrength("ab12!@AB",PasswordStrength.STRONG);
        assertStrength("abc1!Add",PasswordStrength.STRONG);
    }

    @Test
    void meetsOtherCriteria_except_for_Length_Then_Normal(){
        assertStrength("ab12!@A",PasswordStrength.NORMAL);
        assertStrength("Ab12!c",PasswordStrength.NORMAL);
    }

    @Test
    void meetsOtherCriteria_except_for_Number_Then_Normal(){
        assertStrength("ab!@ABqwer",PasswordStrength.NORMAL);
        assertStrength("Ab12!c",PasswordStrength.NORMAL);
    }

    @Test
    void nullInput_Then_Invalid(){
        assertStrength(null,PasswordStrength.INVALID);
    }

    @Test
    void emptyInput_Then_Invalid(){
        assertStrength(null,PasswordStrength.INVALID);
    }


    @Test
    void meetsOtherCriteria_except_for_UpperCase_Then_Normal(){
        assertStrength("ab12!@df",PasswordStrength.NORMAL);
    }

    @Test
    void meetsOnlyLengthCriteria_Then_Weak(){
        assertStrength("abdefghi",PasswordStrength.WEAK);
    }

    @Test
    void meetsOnlyNumCriteria_Then_Weak(){
        assertStrength("12345",PasswordStrength.WEAK);
    }

    @Test
    void meetsOnlyUpperCriteria_Then_Weak(){
        assertStrength("ABZEF",PasswordStrength.WEAK);
    }

    @Test
    void meetsNoCriteria_Then_Weak(){
        assertStrength("abc",PasswordStrength.WEAK);
    }

    /*
     * 첫번째 테스트 :
     * 테스트를 결정한 기준 - 테스트를 선택할 때는 가장 쉽거나 예외 적인 상황을 선택해야한다.
     * - 모든 규칙을 충족하는 경우 :  각 조건을 검사하는 코드를 만들지 않고 '강함'에 해당하는 코드를 리턴하면 테스트 통과 가능
     * - 모든 규칙을 충족하지 않는 경우 : 통과 조건 - 각 조건을 검사하는 코드를 모두 구현 해야 한다.
     * ( 한번에 만들어야 할 코드가 많아지고, 통과하는 시간 또한 길어진다 )
     *
     */

//    @Test
//    void meetsAllCriteria_Then_Strong(){
//        PasswordStrengthMeter meter  = new PasswordStrengthMeter();
//        PasswordStrength      result = meter.meter("ab12!@AB");
//        assertEquals(PasswordStrength.STRONG,result);
//        PasswordStrength      result2 = meter.meter("abc1!Add");
//        assertEquals(PasswordStrength.STRONG,result2);
//    }
//
//    @Test
//    void meetsOtherCriteria_except_for_Length_Then_Normal(){
//        PasswordStrengthMeter meter  = new PasswordStrengthMeter();
//        PasswordStrength      result = meter.meter("ab12!@A");
//        assertEquals(PasswordStrength.NORMAL, result);
//        PasswordStrength      result2 = meter.meter("Ab12!c");
//        assertEquals(PasswordStrength.NORMAL, result2);
//    }
//
//    @Test
//    void meetsOtherCriteria_except_for_Number_Then_Normal(){
//        PasswordStrengthMeter meter  = new PasswordStrengthMeter();
//        PasswordStrength      result = meter.meter("ab!@ABqwer");
//        assertEquals(PasswordStrength.NORMAL, result);
//    }
}

