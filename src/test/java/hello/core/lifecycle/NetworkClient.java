package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy; // 관리하는 회사의 이름이 바꼇음
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

public class NetworkClient{

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    // 서비스 시작 시 호출
    public void connect() {
        System.out.println("connect : " + url);
    }
    public void call(String message){
        System.out.println("call : " + url + " message = " + message);
    }

    // 서비스 종료 시 호출
    public void disconnect() {
        System.out.println("close : " + url);
    }

//    @Override
//    public void afterPropertiesSet() throws Exception { // 의존관계 주입이 끝나면 호출해주겠디.
//        System.out.println("NetworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("NetworkClient.destroy");
//        disconnect();
//    }
    //       생성자 호출, url = null
//       NetworkClient.afterPropertiesSet
//       connect : http://hello-spring.dev
//       call : http://hello-spring.dev message = 초기화 연결 메시지
//       09:13:20.076 [main] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@6c64cb25, started on Mon Mar 13 09:13:19 KST 2023
//       NetworkClient.destroy
//       close : http://hello-spring.dev

// 출력 결과를 보면 초기화 메서드가 주입 완료 후에 적절하게 호출 된 것을 확인할 수 있다.
// 그리고 스프링 컨테이너의 종료가 호출되자 소멸 매서드가 호출 된 것도 확인할 수 있다.

// 초기화, 소멸 인터페이스 단점
// 이 인터페이스는 스프링 전용 인터페이스이다. 해당 코드가 스프링 전용 인터페이스에 의존한다.
// 초기화, 소멸 메서드의 이름을 변경할 수 없다.
// 내가 코드를 고칠 수 없는 외부 라이브러리에 적용할 수 없다.
// 참고 : 인터페이스를 사용하는 초기화, 종료 방법은 스프링 초창기에 나온 방법들이고, 지금은 다음의 더 나은 방법들이 있어서 거의 사용하지 않는다.

//    public void init() {
//        System.out.println("NetworkClient.init");
//        connect();
//        call("초기화 연결 메시지");
//    }
//
//    public void close() {
//        System.out.println("NetworkClient.close");
//        disconnect();
//    }
//    생성자 호출, url = null
//    NetworkClient.init
//    connect : http://hello-spring.dev
//    call : http://hello-spring.dev message = 초기화 연결 메시지
//            09:27:27.709 [main] DEBUG org.springframework.context.annotation.AnnotationConfigApplicationContext - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@6c64cb25, started on Mon Mar 13 09:27:27 KST 2023
//    NetworkClient.close
//    close : http://hello-spring.dev

// 설정 정보 사용 특징
// 메서드 이름을 자유롭게 줄 수 있다.
// 스프링 빈이 스프링 코드에 의존하지 않는다.
// 코드가 아니라 살정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 매서드를 적응할 수 있다.

// 종료 메서드 추론
// @Bean destoryMeyhod 속성에는 아주 특별한 기능이 있다.
// 라이브러리는 대부분 close, shutdown 이라는 이름의 종료 메서드를 사용한다.
// @Bean의 destoryMethod는 기본값이 (interred) (추론)으로 등록되어 있다.
// 이 추론 기능은 close, shutdown라는 이름의 메서드를 자동으로 호출해준다. 이름 그대로 종료 메서드를 추론해서 호출해준다.
// 따라서 직접 스프링 빈으로 등록하면 종료 메서드를 따로 적어주지 않아도 잘 동작한다.
// 추론 기능을 사용하기 싫으면 destoryMethod=""처럼 빈 공백을 지정하면 된다.

    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메시지");
    }
    @PreDestroy
    public void close() {
        System.out.println("NetworkClient.close");
        disconnect();
    }
    // @PostConstruct, @PreDestory 이 두 애노테이션을 사용하면 가장 편리하게 초기ㅘ와 종료를 실행할 수 있다.

    // @PostConstruct, @PreDestory 애노테이션 특정
    // 최신 스프링에서 가장 권장하는 방법이다.
    // 애노테이션 하나만 붙이면 되므로 매우 편리하다.
    // 패키지를 잘 보면 javax.anntation.PostConstruct이다. 스프링에 종속적인 기술이 아니라 JSR-250라는 자바 표준이다. 따라서 스프링이 아닌 다른 컨테이너에서도 동작한다.
    // 컴포넌트 스캔과 잘 어울린다.
    // 유일한 단점은 외부 라이브러리에는 적용하지 못한다는 것이다. 외부 라이브러리를 초기하, 종료 해야 하면 @Bean의 기능을 사용하자.

    // 정리
    // PostConstruct, @PreDestory 애노테이션을 사용하자.
    // 코드를 고칠 수 없는 외부 라이브러리를 초기화, 종료해야 하면 @Bean의 initMethod, destoryMethod를 사용하자.

}
