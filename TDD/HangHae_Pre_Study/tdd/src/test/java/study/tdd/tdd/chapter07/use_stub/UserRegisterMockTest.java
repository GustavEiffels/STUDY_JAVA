package study.tdd.tdd.chapter07.use_stub;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.BDDMockito;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserRegisterMockTest {
    private UserRegister userRegister;
    private WeakPasswordChecker mockPasswordChecker = Mockito.mock(WeakPasswordChecker.class);

    private MemoryUserRepository fakeRepository = new MemoryUserRepository();

    private EmailNotifier mockEmailNotifier = Mockito.mock(EmailNotifier.class);

    @BeforeEach
    void setUp(){
        userRegister = new UserRegister(mockPasswordChecker,fakeRepository,mockEmailNotifier);
    }

    @DisplayName("약한 암호면 가입 실패")
    @Test
    void weakPassword(){
        BDDMockito.given(mockPasswordChecker.checkPasswordWeak("pw")).willReturn(true);

        assertThrows(WeakPasswordException.class,()->{
            userRegister.register("id","pw","email");
        });
    }


    @DisplayName("회원 가입시 암호 검사 수행")
    @Test
    void checkPassword(){
        userRegister.register("id","pw","email");
        // ** checkPasswordWeak 메서드 호출 여부를 확인
        BDDMockito.then(mockPasswordChecker).should().checkPasswordWeak(BDDMockito.anyString());
    }

    @DisplayName("가입하면 메일을 전송함 ")
    @Test
    void whenRegisterThenSendEmail(){
        userRegister.register("id","pw","email@email.com");

        // ** ArgumentCaptor 모의 객체를 메서드 호출 시마다 전달한 객체를 담는 기능을 제공
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        BDDMockito.then(mockEmailNotifier).should().sendRegisterEmail(captor.capture());
        BDDMockito.then(mockEmailNotifier).should().sendRegisterEmail(captor.capture());

        String realEmail = captor.getValue();
        assertEquals("email@email.com",realEmail);
    }

    /**
     * 대역을 사용하지 않는경우
     * - 외부 상황 담당자에게 요청을 해야함으로 테스트를 시작하기 까지의 시간이 많이 걸린다.
     * -> 대역을 사용하면 실제 구현 없이 다양한 상황에 대해 테스트 할 수 ㅣㅇㅆ다.
     * -> 실제 구현없이 실행 결과를 확인할 수 있다.
     *
     * 대역은 의존하는 대상을 구현하지 않아도 테스트를 완성시킬 수 있으며
     * 개발속도를 증진하는데 도움이된다.
     *
     *
     * 모의 객체를 과하게 사용하지 않기
     * - 오히려 코그다 복잡해 지기도 한다. => 단순 가짜 구현이 더 편할 수도 있다
     * ==> ArgumentCaptor 이거 관리 
     */
}
