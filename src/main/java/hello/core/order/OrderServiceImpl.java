package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
//@RequiredArgsConstructor // final 필드, @NoNull에 대한 필드에 대한 생성자 생성 -> 의존관계 추가할 때 정말 편리하다
// 정리
// 최근에는 생성자를 딱 1개 두고, @AutoWired를 생략하는 방법을 주로 사용한다. 여기에 Lombok 라이브러리의
// @RequiredArgsConstructor 함께 사용하면 기능은 다 제공하면서, 코드는 깔끔하게 사용할 수 있다.
public class OrderServiceImpl implements OrderService {
    //   @Autowired private MemberRepository memberRepository;
    private final MemberRepository memberRepository;
    //    @Autowired private DiscountPolicy discountPolicy;
    private final DiscountPolicy discountPolicy;

//   @Autowired
//   public void init(MemberRepository memberRepository, DiscountPolicy discountPolicy) {
//       this.memberRepository = memberRepository;
//       this.discountPolicy = discountPolicy;
    // 참고 : 어쩌면 당연한 이야기지만 의존관계 자동 주입은 스프링 컨테이너가 관리하는 스프링 빈이어야 한다.
    // 스프링 빈이 아닌 Member 같은 클래스에서 @AutoWired 코드를 적용해도 아무 기능도 동작하지 않는다.
//   }

//    @Autowired(required = false) // 선택적으로 필수값이 아니니까 있어도 없어도 상관없음
//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
////        System.out.println("memberRepository = " + memberRepository);
//        this.memberRepository = memberRepository;
//    }

//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
////        System.out.println("discountPolicy = " + discountPolicy);
//        this.discountPolicy = discountPolicy;
//    }

    // 참고 : @Autowired의 기본 동작은 주입할 대상이 없으면 오류가 발생한다.
    // 주입할 대상이 없어도 동작하게 하려면 @Autowired(require = false)로 지정하면 된다.

    // 참고: 자바빈 프로터티, 자바에서는 과거부터 필드의 값을 직접 변경하지 않고,
    // setXxx, getXxx라는 메서드를 통해서 값을 읽거나 수정하는 규칙을 만들었는 데,
    // 그것이 자바빈 프로터티 규약이다. 더 자세한 내용은 자바빈 프로터티를 검색해보자.

    // 주로 생성자 주입을 쓰고 변경 가능할 때는 가끔 수정자 주입이 쓰인다.

//    @Autowired // 생성자에서 여러 의존관계도 한번에 주입받을 수 있다.(생성자가 하나면 생략가능)
    public OrderServiceImpl(MemberRepository memberRepository, @MainDiscountPolicy DiscountPolicy discountPolicy) { // 싱글톤 보장이니까 위에 set이나 생성자나 같다.
//        System.out.println("1. OrderServiceImpl.OrderServiceImpl"); // ctrl + alt + b -> 구현을 찾을 수 있다.
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }
    //sout  : 문자열을 System.out으로 출력
    //soutm : 현재 클래스 및 메서드 이름을 System.out으로 출력
    //soutv : 값을 System.out으로 출력
    //soutp : 메서드 매개변수 이름 및 값을 System.out으로 출력
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.disCount(member, itemPrice);

        return new Order(memberId , itemName, itemPrice ,discountPrice);
    }
    // 테스트 용도
    public MemberRepository getMemberRepository() {
        return memberRepository;
    }
}
