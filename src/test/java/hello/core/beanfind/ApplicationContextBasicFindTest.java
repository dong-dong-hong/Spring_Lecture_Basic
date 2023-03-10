package hello.core.beanfind;

import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.order.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationContextBasicFindTest { // ctrl + E -> 최근 파일 볼 수 있는 단축키
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);


//    스프링 컨테이너에서 스프링 빈을 찾는 가장 기본적인 조회 방법
//
//            ac.getBean(빈이름, 타입)
//            ac.getBean(타입)

    @Test
    @DisplayName("빈 이름으로 조회")
    void findBeanByName() {
        MemberService memberService =  ac.getBean("memberService", MemberService.class);
//        System.out.println("memberService = " + memberService);
//        System.out.println("memberService.getClass() = " + memberService.getClass());
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }
    @Test
    @DisplayName("이름 없이 타입으로만 조회")
    void findBeanByType() {
        MemberService memberService =  ac.getBean(MemberService.class);
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("구체 타입으로 조회")
    void findBeanByName2() {
        MemberServiceImpl memberService =  ac.getBean("memberService", MemberServiceImpl.class); // 인터페이스 아니어도 구체타입으로도 가능 (구현에 의존) 하지만 좋은 방법은 아니다 (유연성이 떨어진다.)역할과 구현을 구분짓지 못하기 때문이다.
        assertThat(memberService).isInstanceOf(MemberServiceImpl.class);
    }

    @Test
    @DisplayName("빈 이름으로 조회x")
    void findBeanByNameX() {
        // 조회 대상 스프링 빈이 없으면 예외 발생
        // org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named 'meme' available
        assertThrows(NoSuchBeanDefinitionException.class,
                () -> ac.getBean("meme", MemberService.class));
    }
}
