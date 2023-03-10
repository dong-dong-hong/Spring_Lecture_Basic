package hello.core.beanDefinition;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.support.GenericXmlApplicationContext;

public class BeanDefinitionTest {
    //    AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
    GenericXmlApplicationContext ac = new GenericXmlApplicationContext("appConfig.xml");
    // ApplicationContext를 하게되면 getBeanDefinition을 못하게 된다.
    @Test
    @DisplayName("빈 설정 메타정보 확인")
    void findApplicationBean() {
        String[] beanDefinitionNames = ac.getBeanDefinitionNames();
        for (String beanDefinitionName : beanDefinitionNames) {
            BeanDefinition beanDefinition = ac.getBeanDefinition(beanDefinitionName);

            if(beanDefinition.getRole() == BeanDefinition.ROLE_APPLICATION) {
                System.out.println("beanDefinitionName = " + beanDefinitionName +
                        " beanDefinition = " + beanDefinition);
            }
        }
    }
}
// 직접 스프링 빈을 등록하는 방법, Factory 메소드를 이용해서 등록하는 방법
