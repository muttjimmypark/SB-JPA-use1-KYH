package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;


@Embeddable // jpa 내장타입을 생성. Embedded와 둘중하나만 써도 동작하지만, 보통 양쪽다 써줌
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
