package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    private final LogDemoService logDemoService;
//    private final MyLogger myLogger;
//    private final ObjectProvider<MyLogger> myLoggerProvider;
    private final MyLogger myLogger;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
        String requestURL = request.getRequestURL().toString();
//        MyLogger myLogger = myLoggerProvider.getObject();

        System.out.println("myLogger = " + myLogger.getClass());
        // myLogger = class hello.core.common.MyLogger$$SpringCGLIB$$0

        // CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입한다.
        // @Scope의 proxyMode = ScopeProxyMode.TARGET_CLASS를 설정하면 스프링 컨테이너는 CGLIB라는 바이트코드를 조작하는 라이브러리를 사용해서, MyLogger를 상속받은 가짜 프록시 객체를 생성한다.
        // 결과를 확인해보면 우리가 등록한 순수한 MyLogger 클래스가 아니라 MyLogger$$SpringCGLIB이라는 클래스로 만들어진 객체가 대신 등록된 것을 확인할 수 있다.
        // 그리고 스프링 컨테이너에 myLogger라는 이름으로 진짜 대신에 이 가짜 프록시 객체를 등록한다.
        // ac.getBean("myLogger", MyLogger.class)로 조회해도 프록시 객체가 조회되는 것을 확인할 수 있다.
        // 그래서 의존관계 주입도 이 가짜 프록시 객체가 주입된다.


        // 가짜 프록시 객체는 요청이 오면 그때 내부에서 진짜 빈을 요청하는 위임 로직이 들어있다.
        // 이 가짜 프록시 빈은 내부에 실제 MyLogger를 찾는 방법을 가지고 있다.
        // 클라이언트 myLogger.logic()을 호출하면 사실은 가짜 프록시 객체의 메서드를 호출한 것이다.
        // 가짜 프록시 객체는 request 스코프의 진짜 myLogger.logic()를 호출한다.
        // 가짜 프록시 객체는 원본 클래스를 상속 받아서 만들어졌기 때문에 이 객체를 사용하는 클라이언트 입장에서는 사실 원본인지 아닌지도 모르게, 동일하게 사용할 수 있다.(다형성)

        // 동작 정리
        // CGLIB라는 라이브러리로 내 클래스를 상속 받은 가짜 프록시 객체를 만들어서 주입한다.
        // 이 가짜 프록시 객체는 실제 요청이 오면 그때 내부에서 실제 빈을 요청하는 위임 로직이 들어있다.
        // 가짜 프록시 객체는 실제 request scope와는 관계가 없다. 그냥 가짜이고, 내부에 단순한 위임 로직만 있고, 싱글톤처럼 동작한다.

        // 특징 정리
        // 프록시 객체 덕분에 클라이언트는 마치 싱글톤 빈을 사용하듯이 편리하게 request scope를 사용할 수 있다.
        // 사실 Provide를 사용하든, 프록시를 사용하든 핵심 아이디어는 진짜 객체 조회를 꼭 필요한 시점까지 자연처리 한다는 점이다.
        // 단지 애노테이션 설정 변경만으로 원본 객체를 프록시 객체로 대체할 수 있다. 이것이 바로 다형성과 DI 컨테이너가 가진 가장 큰 강점이다.
        // 꼭 웹 스코프가 아니어도 프록시는 사용할 수 있다.


        // 주의점
        // 마치 싱글톤을 사용하는 것 같지만 다르게 동작하기 때문에 결국 주의해서 사용해야 한다.
        // 이런 특별한 scope는 꼭 필요한 곳에만 최소화해서 사용하자. 무분별하게 사용하면 유지보수하기 어려워진다.


       myLogger.setRequestURL(requestURL);

       myLogger.log("controller test");
       Thread.sleep(1000);
       logDemoService.logic("testId");
       return "Ok";
    }
}
// 로거가 잘 작동하는 지 확인하는 테스트용 컨트롤러다.
// 여기서 HttpServletRequest를 통해서 요청 URL을 받았다.
   // requestURL 값 http://localhost:8080/log-demo
// 이렇게 받은 requestURL 값을 myLogger에 저장해둔다. myLogger는 HTTP 요청 당 각각 구분되므로 다른 HTTP 요청 때문에 같이 섞이는 걱정은 하지 않아도 된다.
// 컨트롤러에서 controller test라는 로그를 남긴다.

// 참고: requestURL을 MyLogger에 저장하는 부분은 컨트롤러 보다는 공통 처리가 가능한 스프링 인터셉터나 서블릿 필터 같은 곳을 활용하는 것이 좋다.
// 여기서는 예제를 단순화하고, 아직 스프링 인터셉터를 학습하지 않은 분들을 위해서 컨트롤러를 사용했다.
// 스프링 웹에 익숙하다면 인터셉터를 사용해서 구현해보자.


//[91386980-3083-412a-ab07-b8710cab3b19] request scope bean create : hello.core.common.MyLogger@5ce08405
//[91386980-3083-412a-ab07-b8710cab3b19][http://localhost:8080/log-demo] controller test
//[91386980-3083-412a-ab07-b8710cab3b19][http://localhost:8080/log-demo] service id = testId
//[91386980-3083-412a-ab07-b8710cab3b19] request scope bean close : hello.core.common.MyLogger@5ce08405

// ObjectProvider 덕분에 ObjectProvider.getObject()를 호출하는 시점까지 request scope 빈의 생성을 지연할 수 있다.
// ObjectProvider.getObject()를 호출하는 시점에는 HTTP 요청이 진행 중이므로 request scope 빈의 생성이 정상 처리된다.
// ObjectProvider.getObject()를 LogDemoController, LogDemoService에서 각각 한 번 씩 따로 호출해도 같은 HTTP 요청이면 같은 스프링 빈이 반환된다. -> 직접 구현하려면 얼마나 힘들까..

// 이 정도에서 끝내도 될 것같지만.. 코드를 더 줄일 방법이 있다.





