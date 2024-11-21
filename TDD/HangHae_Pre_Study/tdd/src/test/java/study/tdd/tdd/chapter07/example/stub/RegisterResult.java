package study.tdd.tdd.chapter07.example.stub;

public class RegisterResult {
    public static RegisterResult error(CardValidity validity) {
        return null;
    }

    public static RegisterResult success() {
        return null;
    }
}
