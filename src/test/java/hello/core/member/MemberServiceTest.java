package hello.core.member;

import hello.core.order.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MemberServiceTest {

    //    MemberService memberService = new MemberServiceImpl();
    MemberService memberService;
    @BeforeEach // 테스트 실행전에 무조건 실행되는 것 예를 들어 test가 두 개 있으면 beforeEach가 두 번 실행된다.
    public void beforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }
    @Test
    void join() {
        //given
        Member member = new Member(1L,"memberA",Grade.VIP);
        //when
        memberService.join(member);
        Member findMember =   memberService.findMember(1L);
        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }
}