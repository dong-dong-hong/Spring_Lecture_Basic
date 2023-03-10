package hello.core.singleton;

public class SingletonService {
    // static 영역에 객체를 딱 1개만 생성해둔다.
    private static final SingletonService instance = new SingletonService();

    // 2. public으로 열어서 객체 인스턴스가 필요하면, 이 static 메서드를 통해서만 조회하도록 허용한다.
    public static SingletonService getInstance() {
        return instance;
    }
    // 3. 생성자를 private으로 선언해서 외부에서 new 키워드를 사용한 객체 생성을 못하게 막는다.
    private SingletonService() {
    }
    public void logic() {
        System.out.println("싱글톤 객체 로직 호출");
    }
}
// 1. static 영역에 객체 instance를 미리 하나 생성해서 올려둔다.
// 2. 이 객체 인스턴스가 필요하면 오직 getInstance() 메서드를 통해서만 조회할 수 있다.
// 이 메서드를 호출하면 항상 같은 인스턴스가 반환된다.
// 3. 딱 1개의 객체 인스턴스만 존재해야 하므로, 생성자를 private으로 막아서
// 혹시라도 외부에서 new 키워드로 객체 인스턴스가 생성되는 것을 막는다.
