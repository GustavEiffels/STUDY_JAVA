package study.tdd.tdd.chapter07.use_stub;

public interface EmailNotifier {
    boolean isCalled();

    void sendRegisterEmail(String email);
}
