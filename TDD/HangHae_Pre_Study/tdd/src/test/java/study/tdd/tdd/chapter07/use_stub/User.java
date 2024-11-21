package study.tdd.tdd.chapter07.use_stub;

public class User {
    private final String id;
    private final String pw;
    private final String mail;

    public User(String id, String pw, String mail) {
        this.id = id;
        this.pw = pw;
        this.mail = mail;
    }


    public String getId() {
        return id;
    }

    public String getPw() {
        return pw;
    }

    public String getMail() {
        return mail;
    }
}
