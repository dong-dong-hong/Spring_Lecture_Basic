package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.MemberRepository;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // 애플리케이션에 설정 구성 정보
public class AppConfig { // MemoryMemberRepository의 역할이 전혀 보이지 않는다.

    // @Bean memberService -> new MemoryMemeberRepository()
    // @Bean orderService -> new MemoryMemeberRepository()

    // 기대치
    // call AppConfig.memberService
    // call AppConfig.memberRepository
    // call AppConfig.memberRepository
    // call AppConfig.orderService
    // call AppConfig.memberRepository

    // 실제 결괏값
    //call AppConfig.memberService
    //call AppConfig.memberRepository
    //call AppConfig.orderService

    @Bean // spring container에 등록이 된다.
    public MemberService memberService() {
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
        // 생성자 주입
    } // ctrl + alt + m -> extract method(리펙토링) -> 각자의 역할을 알 수 있다.
    @Bean
    public MemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }

    @Bean
    public OrderService orderService() {
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
//        return null;
    }
    @Bean
    public DiscountPolicy discountPolicy() {
//        return new FixDiscountPolicy(); // 정액 할인
        return new RateDiscountPolicy(); // 정률 할인
    }
}
// memberService 빈을 만드는 코드를 보면 memberReposiotory()를 호출한다.
// 이 메서드를 호출하면 new MemoryMemberRepository()를 호출한다.
// orderSerivice 빈을 만드는 코드도 동일하게 memberRepository()를 호출한다.
//  이 메서드를 호출하면 new MemoryMemberRepository()를 호출한다.

// 결과적으로 각각 다른 2개의 MemeoryMemberReposiotry가 생성되면서 싱글톤이 깨지는 것처럼 보인다.
// 스프링 컨테이너는 이 문제를 어떻게 해결할까?

