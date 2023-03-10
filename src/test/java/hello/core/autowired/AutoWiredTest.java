package hello.core.autowired;

import hello.core.member.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.lang.Nullable;

import java.util.Optional;

public class AutoWiredTest {

    @Test
    void AutoWiredOption() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestBean.class);
    }

    static class TestBean {

        @Autowired(required = false) // @AutoWired(required=false) : 자동 주입할 대상이 없으면 수정자 메서드 자체가 호출 안됨
        public void setNoBean1(Member noBean1) {
            System.out.println("noBean1 = " + noBean1);
        }

        @Autowired // org.springframework.lang.@Nullable : 자동 주입할 대상이 없으면 null이 입력된다.
        public void setNoBean2(@Nullable Member noBean2) {
            System.out.println("noBean2 = " + noBean2);
        }

        @Autowired // Optional<> : 자동 주입할 대상이 없으면 Optional.empty가 입력된다.
        public void setNoBean3(Optional<Member> noBean3) {
            System.out.println("noBean3 = " + noBean3);
        }
        // Member는 스프링 빈이 아니다.
        // setNoBean()은 @AutoWired(reuired=false)이므로 호출 자체가 안된다.

        // noBean2 = null
        // noBean3 = Optional.empty

        // 참고: @Nullable, Optional은 스프링 전반에 걸쳐서 지원된다. 예를 들어서 생성자 자동 주입에서 특정 필드에만 사용해도 된다.
    }
}
