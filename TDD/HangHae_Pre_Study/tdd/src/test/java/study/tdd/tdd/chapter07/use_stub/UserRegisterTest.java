package study.tdd.tdd.chapter07.use_stub;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserRegisterTest {
    private UserRegister userRegister;

    private StubWeakPasswordChecker stubWeakPasswordChecker = new StubWeakPasswordChecker();

    private MemoryUserRepository fakeRepository = new MemoryUserRepository();

    private SpyEmailNotifier spyEmailNotifier = new SpyEmailNotifier();


    @BeforeEach
    void setUp(){
        userRegister = new UserRegister(stubWeakPasswordChecker,fakeRepository,spyEmailNotifier);
    }

    // ** STUB 사용
    @DisplayName("약한 암호면 가입 실패")
    @Test
    void weakPassword(){
        stubWeakPasswordChecker.setWeak(true);

        Assertions.assertThrows(WeakPasswordException.class,()->{
            userRegister.register("id","pw","email");
        });
    }

    // ** FAKE 사용
    @DisplayName("이미 같은 ID 가 존재하면 가입 실패")
    @Test
    void dupIdExists(){
        fakeRepository.save(new User("id","pw1","email@email.com"));
        Assertions.assertThrows(DupIdException.class,()->{
            userRegister.register("id","pw2","email");
        });
    }

    @DisplayName("같은 ID가 없으면 가입 성공함")
    @Test
    void noDupId_RegisterSuccess(){
        userRegister.register("id","pw","email");
        User saveUser = fakeRepository.findById("id");
        assertEquals("id",saveUser.getId());
        assertEquals("email",saveUser.getMail());
    }

    // ** SPY 사용
    @DisplayName("가입하면 메일을 전송")
    @Test
    void whenRegisterThenSendMail(){
        userRegister.register("id","pw","email@email.com");
        assertTrue(spyEmailNotifier.isCalled());
        assertEquals("email@email.com",spyEmailNotifier.getEmail());
    }

    // ** 모의 객체


}
