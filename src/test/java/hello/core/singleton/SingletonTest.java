package hello.core.singleton;

import hello.core.member.MemberService;
import hello.core.order.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class SingletonTest {

// 우리가 만들었던 스프링 없는 순수한 DI 컨테이너인 AppConfig는 요청을 할 때 마다 객체를 새로 생성한다.
// 고객 트래픽이 초당 100이 나오면 초당 100개 객체가 생성되고 소멸된다 -> 메모리 낭비가 심하다.
// 해결방안은 해당 객체가 딱 1개만 생성되고, 공유하도록 설계하면 된다. -> 싱글톤 패턴

    @Test
    @DisplayName("스프링 없는 순수한 DI 컨테이너")
    void pureContainer() {
        AppConfig appConfig = new AppConfig();
        // 1. 조회 : 호출할 때 마다 객체를 생성
        MemberService memberService1 = appConfig.memberService();

        // 2. 조회 : 호출할 때 마다 객체를 생성
        MemberService memberService2 = appConfig.memberService();

        //참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

        // memberService1 != memberService2
        assertThat(memberService1).isNotSameAs(memberService2);
    }
    @Test
    @DisplayName("싱글톤 패턴을 적용한 객체 사용")
    void singletonServiceTest() {
//        new SingletonService(); // java: SingletonService() has private access
        // 1. 조회 : 호출할 때 마다 같은 객체를 반환
        SingletonService singletonService1 = SingletonService.getInstance();
        // 2. 조회 : 호출할 때 마다 같은 객체를 반환
        SingletonService singletonService2 = SingletonService.getInstance();

        // 참조값이 같은 것을 확인
        System.out.println("singletonService1 = " + singletonService1);
        System.out.println("singletonService2 = " + singletonService2);

        //singletonService1 == singletonService2
        assertThat(singletonService1).isSameAs(singletonService2);
        // same ==
        // equal 자바 equals랑 같음

        singletonService1.logic();

        // private으로 new 키워드를 막아두었다.
        // 호출할 때 마다 같은 객체 인스턴스를 반환하는 것을 확인할 수 있다.
        // 참고: 싱글톤 패턴을 구현하는 방법은 여러가지가 있다. 여기서는 객체를 미리 생성해두는 가장 단순하고 안전한 방법을 선택했다.

        // 싱글톤 패턴을 적용하면 고객의 요청이 올 때 마다 객체를 생성하는 것이 아니라,이미 만들어진 객체를 공유해서 효율적으로 사용할 수 있다.
        // 하지만 싱글톤 패턴은 다음과 같은 수 많은 문제점들을 가지고 있다.

        // 싱글톤 패턴 문제점
        // 싱글톤 패턴을 구현하는 코드 자체가 많이 들어간다.
        // 의존관계상 클라이언트가 구체 클래스에 의존한다. -> DIP를 위반한다.
        // 클라이언트가 구체 클래스에 의존해서 OCP를 위반할 가능성이 높다.
        // 테스트 하기 어렵다.
        // 내부 속성을 변경하거나 초기화 하기 어렵다.
        // private 생성자로 자식 클래스를 만들기 어렵다.
        // 결론적으로 유연성이 떨어진다.
        // 안티패턴으로 불리기도 한다.
    }
    @Test
    @DisplayName("스프링 컨테이너와 싱글톤")
    void springcontainer() {

//        AppConfig appConfig = new AppConfig();
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        MemberService memberService1 = ac.getBean("memberService", MemberService.class);
        MemberService memberService2 = ac.getBean("memberService", MemberService.class);

        //참조값이 다른 것을 확인
        System.out.println("memberService1 = " + memberService1);
        System.out.println("memberService2 = " + memberService2);

//      assertThat(memberService1).isNotSameAs(memberService2);
        assertThat(memberService1).isSameAs(memberService2);

        // 스프링 컨테이너 적용 후
        // 스프링 컨테이너 덕분에 고객의 요청이 올 떄 마다 객체를 생성하는 것이 아니라, 이미 만들어진 객체를 공유해서 효율적으로 재사용할 수 있다.
        // 참고: 스프링의 기본 빈 등록 방식은 싱글톤이지만, 싱글톤 방식만 지원하는 것은 아니다. 요청할 때 마다 새로운 객체를 생성해서 반환하는 기능도 제공한다.
        // 자세한 내용은 뒤에 빈 스코프에서 설명하겠다.
    }
}