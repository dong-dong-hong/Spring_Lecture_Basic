package hello.core.scan.fliter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ComponentFilterAppConfigTest {

    @Test
    void filterScan() {
        ApplicationContext ac = new AnnotationConfigApplicationContext(ComponentFilterAppConfig.class);
        BeanA beanA = ac.getBean("beanA", BeanA.class);
        assertThat(beanA).isNotNull();

        // excludeComponent라서  org.springframework.beans.factory.NoSuchBeanDefinitionException: No bean named'beanB' available
        assertThrows(
                NoSuchBeanDefinitionException.class,
                () ->  ac.getBean("beanB", BeanB.class)
        );
    }


    @Configuration
    @ComponentScan(
            includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION , classes = MyIncludeComponent.class),
            excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION , classes = MyExcludeComponent.class)
            // 예를 들어 BeanA도 빼고 싶으면 다음과 같이 추가하면 된다.
            // @Filter(type = FliterType.ASSIGNABLE_TYPE, Classes = BeanA.class)

            // 참고: @Component면 충분하기 때문에 includeFilters를 사용할 일은 거의 없다.
            // excludeFilters는 여러가지 이유로 간혹 사용할 때가 있지만 많지는 않다.
            // 특히 최근 스프링 부트는 컨포넌트 스캔을 기본으로 제공하는 데, 개인적으로는 옵션을 변경하면서 사용하기 보다는
            // 스프링의 기본설정에 최대한 맞추어 사용하는 것을 권장하고, 선호하는 편이다.
    )
    // includeFliters에 MyIncludeComponent 애노테이션을 추가해서 BeanA가 스프링 빈에 등록된다.
    // ExcludeFliters에 MyExcludeComponent 애노테이션을 추가해서 BeanB가 스프링 빈에 등록되지 않는다.

    // FliterType 옵션
    // FliterType은 5가지 옵션이 있다.

    // ANNOTATION : 기본값(생략가능), 애노테이션을 인식해서 동작한다.
    // ex) org.exmple.SomeAnnotation
    // ASSIGNBLE_TYPE : 지정한 타입과 자식 타입을 인식해서 동작한다.
    // ex) org.example.SomeClass
    // ASPECTJ : AspectJ 패턴 사용
    // ex) org.example..*Service+
    // REGEX : 정규 표현식
    // ex) org\.example\.Default.*
    // CUSTOM : TypeFilter이라는 인터페이스를 구현해서 처리
    // ex) org.example.MyTypeFilter


    static class ComponentFilterAppConfig {
    }
}