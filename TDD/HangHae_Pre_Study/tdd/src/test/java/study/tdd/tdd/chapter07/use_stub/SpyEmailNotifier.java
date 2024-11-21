package study.tdd.tdd.chapter07.use_stub;

public class SpyEmailNotifier implements EmailNotifier{
    private boolean called;
    private String email;

    public boolean isCalled(){
        return called;
    }

    @Override
    public void sendRegisterEmail(String email) {
        this.called = true;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
