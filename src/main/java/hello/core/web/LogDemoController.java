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
    private final ObjectProvider<MyLogger> myLoggerProvider;

    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest request) throws InterruptedException {
        String requestURL = request.getRequestURL().toString();
        MyLogger myLogger = myLoggerProvider.getObject();
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





