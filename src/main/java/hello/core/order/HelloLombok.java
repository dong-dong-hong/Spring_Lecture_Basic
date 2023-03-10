package hello.core.order;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
        HelloLombok helloLombok = new HelloLombok();
        helloLombok.setName("dong-dong");

        String name = helloLombok.getName();
        System.out.println("name = " + name);
    }
}
