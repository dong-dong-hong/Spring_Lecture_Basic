package hello.core.singleton;

import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.AppConfig;
import hello.core.order.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class); // 원래는 구체타입으로 거내면 좋지 않다.
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();

        // 모두 같은 인스턴스를 참조하고 있다
        System.out.println("memberService -> memberRepository = " + memberRepository1);
        System.out.println("orderService -> memberRepository =" + memberRepository2);
        System.out.println("memberRepository = " + memberRepository);

        // 모두 같은 인스턴스를 참조하고 있다
        assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);

        // 확인해보면 memberRepository 인스턴스는 모두 같은 인스턴스가 공유되어 사용된다.
        // AppConfig의 자바 코드를 보면 분명히 각각 2번 new MemoryMemberRepository 호출해서 다른 인스턴스가 생성되어야 하는데?
        // 어떻게 된 일일까? 혹시 두 번 호출이 안되는 것일까? 실험을 통해 알아보자.
    }
    @Test
    void configurationDeep() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        // AppConfig도 스프링 빈으로 등록된다.
        AppConfig bean = ac.getBean(AppConfig.class);

        System.out.println("bean = " + bean.getClass());
        // 출력 : bean = class hello.core.order.AppConfig$$EnhancerBySpringCGLIB$$52085a3

        // 사실 AnnotationConfigApplicationContext에 파라미터로 넘긴 값은 스프링 빈으로 등록된다. 그래서 AppConfig도 스프링 빈이 된다.
        // AppConfig도 스프링 빈이 된다.
        // AppConfig 스프링 빈을 조회해서 클래스 정보를 출력해보자
        // bean = class hello.core.order.AppConfig$$EnhancerBySpringCGLIB$$52085a3
        // 순수한 클래스라면 class hello.core.AppConfig 출력되어야 한다.
        // 그런데 예상과는 다르게 클래스 명에 xxxCGLIB가 붙으면서 상당히 복잡해진 것을 볼 수 있다. 이것은 내가 만든 클래스가 아니라
        // 스프링이 CGLIB라는 바이크코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고 ,
        // 그 다른 클래스를 스프링 빈으로 등록한 것이다.


        // 그 임의의 다른 클래스가 바로 싱글톤이 보장되도록 해준다. 아마도 다음과 같은 바이트 코드를 조작해서 작성되어 있을 것이다.
        // (실제로는 CGLIB의 내부 기술을 사용하는 데 매우 복잡하다.)

        // @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프리 빈으로 등록하고 반환하는 코드가 동적으로 만들어진다.
        // 덕분에 싱글톤이 보장되는 것이다.
        // 참고: AppConfig@CGLIB는 AppConfig의 자식 타입이므로, AppConfig 타입으로 조회 할 수 있다.
    }
}
