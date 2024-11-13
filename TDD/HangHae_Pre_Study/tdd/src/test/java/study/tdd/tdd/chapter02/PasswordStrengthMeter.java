package study.tdd.tdd.chapter02;

public class PasswordStrengthMeter {

    public  PasswordStrength meter(String s){

        if(s == null || s.isEmpty() ) return PasswordStrength.INVALID;
        int meetsCounts = getMetCriteriaCounts(s);
        if(meetsCounts <= 1) return PasswordStrength.WEAK;
        if(meetsCounts == 2) return PasswordStrength.NORMAL;
        return PasswordStrength.STRONG;
    }

    private int getMetCriteriaCounts(String s){
        int meetsCounts = 0;
        if(s.length() >= 8) meetsCounts++;
        if(isMeetsContainingNumberCriteria(s))    meetsCounts++;
        if(isMeetsContainingUpperCaseCriteria(s)) meetsCounts++;
        return meetsCounts;
    }


    private boolean isMeetsContainingNumberCriteria(String s){
        for( char ch : s.toCharArray() ) {
            if(ch >= '0' && ch <= '9'){
                return true;
            }
        }
        return false;
    }

    private boolean isMeetsContainingUpperCaseCriteria(String s){
        for(char ch : s.toCharArray() ){
            if(Character.isUpperCase(ch))
                return true;
        }
        return false;
    }
}
