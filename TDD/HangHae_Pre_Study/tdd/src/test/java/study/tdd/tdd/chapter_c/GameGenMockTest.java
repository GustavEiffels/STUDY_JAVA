package study.tdd.tdd.chapter_c;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;

public class GameGenMockTest {
    @Test
    void mockTest(){
        // ** 특정 타입의 모의 객체 생성이 가능
        /**
         * Mockito.mock()
         * - 클래스
         * - 인터페이스
         * 추상 클래스에 대한 모의 객체를 생성할 수 있다.
         */
        GameNumGen genMock = Mockito.mock(GameNumGen.class);
    }

    /**
     * STUB 설정
     */
    @Test
    void mockTest_Stub(){
        // 1. 모의 객체를 만든다.
        GameNumGen genMock = Mockito.mock(GameNumGen.class);

        // 2. 모의 객체의 method 에 대한 INPUT 과 OUTPUT 을 설정
        BDDMockito.given(genMock.generate(GameLevel.EASY)).willReturn("123");

        // 3. 확인
        String num = genMock.generate(GameLevel.EASY);
        Assertions.assertEquals(num,"123");

        // 4. 설정하지 않은 parameter 로 넣을 때 null 을 반환
        String num2 = genMock.generate(GameLevel.NORMAL);
        Assertions.assertNull(num2);
    }

    @Test
    void mockThrowTest(){
        // 1. 모의 객체를 만든다.
        GameNumGen genMock = Mockito.mock(GameNumGen.class);

        // 2. 스텁의 예외가 발생하도록 설정한다.
        BDDMockito.given(genMock.generate(null)).willThrow(new NullPointerException());

        // 3. 예외가 발생
        Assertions.assertThrows(NullPointerException.class,()->{
            genMock.generate(null);
        });
    }

    @Test
    void mockThrowWhenReturnIsVoidTest(){
        // 1. 모의 객체를 만든다.
        GameNumGen genMock = Mockito.mock(GameNumGen.class);

        // 2. return 타입이 업는 메소드에 예외가 발생할 수 있도록 설정한다.
        BDDMockito.willThrow(UnsupportedOperationException.class)
                .given(genMock)
                .generateVoid(); // ** return type 이 void 인 method

        Assertions.assertThrows(UnsupportedOperationException.class, genMock::generateVoid);
    }

    @Test
    void over2ParameterCase(){
        // 1. 모의 객체를 생성한다.
        GameNumGen mock = Mockito.mock(GameNumGen.class);

        // 2. parameter 가 2개 이상인 것 사용
        BDDMockito.given(mock.generateWhen2Parameter(Mockito.anyInt(), Mockito.eq("test")))
                .willReturn("TEST DONE");

        // 3. 확인
        Assertions.assertEquals(mock.generateWhen2Parameter(5,"test"),"TEST DONE");
    }

    /**
     *  BDDMockito.then() => 호출 여부를 검증할 모의 객체를 전달 받음
     *
     */
    @Test
    void Testing(){
        // 1. 모의 객체 생성
        GameNumGen genMock = Mockito.mock(GameNumGen.class);

        Game game = new Game(genMock);
        game.gameTest();

        // 3.
        BDDMockito.then(genMock).should().generate(Mockito.any());
    }

    private class Game{
        private GameNumGen gameNumGen;
        public Game(GameNumGen gameNumGen){
            this.gameNumGen = gameNumGen;
        }

        public void gameTest(){
            gameNumGen.generate(GameLevel.NORMAL);
        }
    }

    private class GameNumGen {

        public String generateWhen2Parameter(int num, String name){
            return null;
        }


        public String generate(GameLevel gameLevel) {
            return null;
        }

        public void generateVoid(){
        }
    }

    private enum GameLevel {
        NORMAL, EASY
    }
}
