package hello.core.beanfind;

import hello.core.order.AppConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

class ApplicataionContextInfoTest {
    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    @Test
    @DisplayName("모든 빈 출력하기")
    void findAllBean() { // Junit5부터는 public 설정을 안해도 상관없다.
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) { // iter -> for문 자동완성
            Object bean = ac.getBean(beanDefinitionName); // ac.getBean() : 빈 이름으로 빈 객체(인스턴스)를 조회한다.
            System.out.println("name = " + beanDefinitionName + " object = " + bean);
        }
    }
    @Test
    @DisplayName("애플리케이션 빈 출력하기") // 스프링 내부에서 사용하는 빈은 제외하고, 내가 등록한 빈만 출력!
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames(); // ac.getBeanDefinitionNames() : 스프링에 등록된 모든 빈 이름을 조회한다.
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName); // 빈 하나하나에 대한 메타데이터 정보

            // BeanDefinition.ROLE_APPLICATION : 직접 등록한 애플리케이션 빈(일반적으로 사용자가 정의한 빈)
            // BeanDefinition.ROLE_INFRASTRUCTURE : 스프링 내부에서 사용하는 빈
            // 필요에 따라 어떤 빈이 등록되어 있는지 확인할 수 있다.
            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) { // 스프링 내부의 빈이 아니라 내가 주로 애플리케이션 개발을 위해 만든 것  또는 외부 라이브러리
                // 스프링이 내부에서 사용하는 빈은 getRole()로 구분할 수 있다.
                Object bean = ac.getBean(beanDefinitionName);
                System.out.println("name = " + beanDefinitionName + " object = " + bean);
            }
        }
    }
}
