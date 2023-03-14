package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.Assertions.*;

public class SingletonWithPrototypeTest {

    @Test
    void protoTypeFind() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototpyeBean1 = ac.getBean(PrototypeBean.class);
        prototpyeBean1.addCount();
        assertThat(prototpyeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototpyeBean2 = ac.getBean(PrototypeBean.class);
        prototpyeBean2.addCount();
        assertThat(prototpyeBean2.getCount()).isEqualTo(1);
    }


    @Test
    void singletonClientUsePrototype() {
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(2);
    }
    @Scope("singleton")
//    @RequiredArgsConstructor
    static class ClientBean {
        private final PrototypeBean prototypeBean; // 생성시점에 주입

        @Autowired
        public ClientBean(PrototypeBean prototypeBean) {
            this.prototypeBean = prototypeBean;
        }

        public int logic() {
            prototypeBean.addCount();
            int count = prototypeBean.getCount();
            return count;
        }
        // 스프링은 일반적으로 싱글톤 빈을 사용하므로, 싱글톤 빈이 프로토타입 빈을 사용하게 된다.
        // 그런데 싱글톤 빈은 생성 시점에만 의존관계 주입을 받기 때문에, 프로토 타입 빈이 새로 생성되기는 하지만,
        // 싱글톤 빈과 함께 계속 유지되는 것이 문제다.

        // 아마 원하는 것이 이런 것은 아닐 것이다. 프로토타입 빈을 주입 시점에만 새로 생성하는게 아니라,
        // 사용할 때 마다 새로 생성해서 사용하는 것을 원할 것이다.

        // 참고: 여러 빈에서 같은 프로토타입 빈을 주입 받으면, 주입 받는 시점에 각각 새로운 프로토타입 빈이 생성 된다.
        // 예를 들어서 clientA, clientB가 각각 의존관계 주입을 받으면 각각 다른 인스턴스의 프로토타입 빈을 주입받는다.
        // clientA -> prototypeBean@x01
        // clientB -> prototypeBean@x02
        // 물론 사용할 때 마다 새로 생성되는 것은 아니다.


    }

    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0 ;

        public void addCount() {
            count++;
        }

        public int getCount() {
            return count;
        }
        @PostConstruct
        public void init() {
            System.out.println("PrototypeBean.init" + this);
        }
        @PreDestroy
        public void destory() {
            System.out.println("PrototypeBean.destory");
        }
    }
}
