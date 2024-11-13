# JUnit 5 기초 


## 구성 
- JUnit 플랫폼 : 테스트 프레임 워크를 구동하기 위한 엔진과 API 
- JUnit 주피터 : JUnit 5를 위한 테스트 API 실행 엔진 
- JUnit 빈티지 : JUnit 3,4 로 작성된 테스트를 실행하기 위한 모듈 
> gradle 4.6 버전 부터 JUnit 5 플랫폼을 지원한다.

## @Test
- @Test 가 붙은 method 는 private 이 되면 안된다.
> JUnit 5 와 JUnit 4 는 패키지 구조나 제약조건과 
> 패키지 구성이 다르기 때문에 주의해서 사용해야한다.

## 주요 Method 들 중 
 - fail : 테스트가 실패했음을 알리는 method 
 - assertThrows : 지정한 익셉션이 발생하는지 확인하는 method 

## 라이프 사이클 
- 순서
> 1. 테스트 메소드를 포함한 객체 생성 
> 2. @BeforeEach 존재 시 해당 method 실행 
> 3. @Test 붙은 method 실행 
> 4. @AfterEach 가 붙은 메서드 존재시 실행

- @BeforeEach : 테스트를 실행하는데 필요한 준비 작업을 할 때 사용 
- @AfterEach  : 테스트를 실행 한 후 정리할 것이 있을 때 사용하는 method 

- @BefoareAll : 정적 메서드에 붙은 어노테이션
- 클래스의 모든 테스트 메서드를 실행하기 전 한번만 실행된다.

## 실행 순서 의존과 필드 공유하지 않기 
- JUnit 이 테스트 순서를 결정하지만
- 실행되는 순서는 JUnit 버전에 따라 달라지기 때문에
- 각 메소드 마다 독립성을 유지하는 것이 좋다.