package study.tdd.tdd.chapter07.use_stub;

public class UserRegister {
    private final WeakPasswordChecker passwordChecker;
    private UserRepository userRepository;
    private EmailNotifier emailNotifier;

    public UserRegister(WeakPasswordChecker passwordChecker, UserRepository userRepository, EmailNotifier emailNotifier) {
        this.passwordChecker    = passwordChecker;
        this.userRepository     = userRepository;
        this.emailNotifier       = emailNotifier;
    }
    public UserRegister(WeakPasswordChecker passwordChecker) {
        this.passwordChecker = passwordChecker;

    }

    public void register(String id, String pw, String email) {
        if(passwordChecker.checkPasswordWeak(pw)){
            throw new WeakPasswordException();
        }
        User user = userRepository.findById(id);
        if(user!=null){
            throw new DupIdException();
        }

        userRepository.save(new User(id,pw,email));
        emailNotifier.sendRegisterEmail(email);
    }
}
