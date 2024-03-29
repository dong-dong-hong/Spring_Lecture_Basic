package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request" , proxyMode = ScopedProxyMode.TARGET_CLASS)
// 여기가 핵심이다. proxyMode = ScopeProxyMode.TARGET_CLASS를 추가해주자
   // 적용 대상이 인터페이스가 아닌 클래스면 TAGET_CLASS
   // 적용 대상이 인터페이스면 INTERFACES를 선택
// 이렇게 하면 MyLogger의 가짜 프록시 클래스를 만들어두고 HTTP request와 상관 없이 가짜 프록시 클래스를 다른 빈에 미리 주입해 둘 수 있다.

public class MyLogger {
    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL) {
        this.requestURL = requestURL;
    }
    public void log(String message) {
        System.out.println("[" + uuid +"]" + "[" + requestURL + "] " + message);
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid +"] request scope bean create : " + this);
    }
    @PreDestroy
    public void close() {
        System.out.println("[" + uuid +"] request scope bean close : " + this);
    }
}
// 로그를  출력하기 위한 MyLogger 클래스이다.
// @Scope(value = "request)를 사용해서 request 스코프로 지정했다. 이게 이 빈은 HTTP 요청 당 하나씩 새성되고, HTTP 요청이 끝나는 시점에 소멸된다.
// 이 빈이 생성되는 시점에 자동으로 @PostConstruct 초기화 메서드를 사용해서 uuid를 생성해서 저장해둔다. 이 빈은 HTTP 요청 당 하니씩 생성되므로, uuid를 저장해두면 다른 HTTP 요청과 구분할 수 있다.
// 이 빈이 소멸되는 시점에 @PreDestory를 사용해서 종료 메시지를 남긴다.
// requestURL은 이 빈이 생성되는 시점에는 알 수 없으므로, 외부에서 setter로 입력 받는다.

