package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Embeddable // jpa 내장타입을 생성. Embedded와 둘중하나만 써도 동작하지만, 보통 양쪽다 표기를 위해 써줌
@Getter
@AllArgsConstructor
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
        /*
        불변값을 저장시키기 위해 Setter도 부여하지 않았다.

        JPA 스펙상 NoArgsConstructor를 반드시 필요로 하므로 (리플렉션 등 기술 지원)
        jpa가 허용하는 최대한 방어적인 권한인 protected로 NoArgs를 별도 정의내려놓는다.

        (리플렉션이란 일단 이 클래스의 타입을 몰라도, 여기있는 메서드-타입-변수 들에 접근할수 있도록 api가 지원해주는것)
        ㄴ> Object이기 때문에 가능한걸까
         */
    }
}
