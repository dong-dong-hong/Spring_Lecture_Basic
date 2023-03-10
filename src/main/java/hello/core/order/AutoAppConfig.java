package hello.core.order;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan( //자동으로 스프링 빈을 등록할 수 있는 기능
        basePackages = "hello.core", // 어디서부터 찾는 지 지정 가능
        //basePackages : 탐색할 패키지의 시작위치를 지정한다. 이 패키지를 포함해서 하위 패키지를 모두 탐색한다.
        //basePackages = {"hello.core", "hello.service"} 이렇게 여러 시작 위치를 지정할 수도 있다.
        basePackageClasses = AutoAppConfig.class,
        //basePackagesClasses : 지정한 클래스의 패키지를 탐색 시작 위로 지정한다.
        //만약 지정하지 않으면 @ComponentScan이 붙은 설정 정보 클래스의 패키지가 시작 위치가 된다.(default가 된다.)

        // 권장하는 방법
        // 개인적으로 즐겨 사용하는 방법은 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것이다.
        // 최근 스프링 부트도 이 방법을 기본으로 제공한다.

        // 예를 들어 프로젝트가 다음과 같은 구조가 되어 있으면

        // com.hello
        // com.hello.service
        // com.hello.repository

        // com.hello -> 프로젝트 시작 루트, 여기에 AppConfig 같은 메인 설정 정보를 두고, @ComponentScan 에노테이션을 부이고,
        // basePackages지점은 생략한다.

        // 이렇게 하면 com.hello를 포함한 하위는 모두 자동으로 컴포넌트 스캔의 대상이 된다. 그리고 프로젝트 메인 설정 정보는 프로젝트를
        // 대표하는 정보이기 때문에 프로젝트 시작 루트 위치에 두는 것이 좋다 생각한다.
        // 참고로 스프링 부트를 사용하면 스프링 부트의 대표 시작 정보인 @SpringBootApplication를 이 프로젝트 시작 루트 위치에 두는 것이 관례이다.
        // (그리고 이 설정안에 바로 @ComponentScan이 들어있다.)
        excludeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION ,classes = Configuration.class)
)
// 컴포넌트 스캔 기본 대상
// 컴포넌트 스캔은 @Component 뿐만 아니라 다음과 같은 내용과 추가로 대상에 포함한다.
// @Component : 컴포넌트 스캔에서 사용
// @Controller : 스프링 MVC 컨트롤러에서 사용
// @Service : 스프링 비즈니스 로직에서 사용
// @Repoistory : 스프링 데이터 접근 계층에서 사용
// @Configuration : 스프링 설정 정보에서 사용

// 해당 클래스의 소스 코드를 보면 @Component를 포함하고 있는 것을 알 수 있다.

// @Component
// public @interface Controller {
// }
// @Component
// public @interface Service {
// }
// @Component
// public @interface Configuration {
// }

// 참고 : 사실 어노테이션에는 상속관계라는 것이 없다. 그래서 이렇게 애노테이션이 특정 애노테이션을 들고 있는 것을
// 인식할 수 있는 것은 자바 언어가 지원하는 기능은 아니고, 스프링이 지원하는 기능이다.

// 컴포넌트 스캔의 용도 뿐만 아니라 다음 애노테이션이 있으면 스프링은 부가 기능을 수행한다.
// @Controller : 스프링 MVC 컨트롤러로 인식
// @Repoistory : 스프링 데이터 접근 계층으로 인식하고, 데이터 계층의 예외를 스프링 예외로 변환시켜준다.
// @Configuration : 앞서 보았듯이 스프링 설정 정보로 인식하고, 스프링 빈이 싱글톤을 유지하도록 추가 처리를 한다.
// @Service : 사실 @Service는 특별한 처리를 하지 않는다. 대신 개발자들이 핵심 비즈니스 로직이 여기에 있겠구나 라고
// 비즈니스 계층을 인식하는 데 도움이 된다.

// 참고 : useDefaultFilters 옵션은 기본으로 켜져있는 데,이 옵션을 끄면 기본 스캔 대상들이 제외된다. 그냥 이런 옵션이 있구나 정도 말고 넘어가자.
public class AutoAppConfig {

//    @Autowired MemberRepository memberRepository;
//    @Autowired
//    DiscountPolicy discountPolicy;
//
//    @Bean
//    OrderService orderService( MemberRepository memberRepository, DiscountPolicy discountPolicy ) {
//        return new OrderServiceImpl(memberRepository,discountPolicy);
//    }

//    @Bean(name = "memoryMemberRepository")
//    MemberRepository memberRepository() {
//        return new MemoryMemberRepository();


    // 이 경우 수동 빈 등록이 우선권을 가진다.
    // (수동 빈이 자동 빈을 오버라이딩 해버린다)

    // 수동 빈 등록 시 남는 로그
    // Overriding bean definition for been 'memeoryMemberRepository' with a different deintion : replacing

    // 물론 개발자가 의도적으로 이런 결과를 기대했다면, 자동 보다는 수동이 우선권을 가지는 것이 좋다. 하지만 현실은
    // 개발자가 의도적으로 설정해서 이런 결과가 만들어지기 보다는 여러 설정들이 꼬여서 이런 결과가 만들어지는 경우가 대부분이다.
    // 그러면 정말 잡기 어려운 버그가 만들어진다. 항상 잡기 어려운 버그는 애매한 버그다.
    // 그래서 최근 스프링 부트에서는 수동 빈 등록과 자동 빈 등록이 충돌나면 오류가 발생하도록 기본 값을 바꾸었다.

    // 수동 빈 등록, 자동 빈 등록 오류 시 스프링 부트 에러
    // Consider renaming one of the beans or enabling overriding by setting spring.main.allow-been-definition-overriding=true

    // 스프링 부트인 CoreApplication을 실행해보면 오류를 볼 수 있다.
//    }
}
// 컴포넌트 스캔을 사용하려면 먼저 @ComponmentScan을 설정 정보에 붙여주면 된다.
// 기존의 AppConfig와는 다르게 @Bean으로 등록된 클래스가 하나도 없다.

// 참고 : 컴포넌트 스캔을 사용하면 @Configuration이 붙은 설정 정보도 자동으로 등록되기 때문에, AppConfig, TestConfig 등
// 앞서 만들어두었던 설정 정보도 함께 등록되고, 실행되어 버린다. 그래서 excludeFulters를 이용헤서 설정정보는
// 컴포넌트 스캔 대상에서 제외했다. 보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만,
// 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택했다.

// 컴포넌트 스캔은 이름 그대로 @Componet 에노테이션이 붙은 클래스를 스캔해서 스프링 빈으로 등록한다. @Componet를 붙여주자

// 참고: @Configuration이 컴포넌트 스캔의 대상이 된 이유도 @Configuration 소스코드를 열어보면 @Component 어노테이션이 붙어있기 때문이다.

// 이제 각 클래스가 컴포넌트 스캔의 대상이 되도록 @Component 어노테이션을 붙여주자.


// @ComponentScan은 @Compnent가 붙은 모든 클래스를 스프링 빈으로 등록한다.
// 이때 스프링 빈의 기본 이름은 클래스명을 사용하되 맨 앞글자만 소문자를 사용한다.
// 빈 이름 기본 전략 : MemberServiceImpl 클래스 -> memberServiceImpl
// 빈 이름 직접 지정 : 만약 스프링 빈의 이름을 직접 지정하고 싶으면 @Component("memberService") 이런식으로 이름을 부여하면 된다.

// 생성자에 @AutoWired를 지정하면, 스프링 컨테이너가 자동으로 해당 스프링 빈을 칮아서 주입한다.
// 이때 기본 조회 전략은 타입이 같은 빈을 찾아서 주입한다.
// getBean(MemberServiceImpl.class)와 동일하다고 이해하면 된다.
// 더 자세한 내용은 뒤에서 설명한다.



