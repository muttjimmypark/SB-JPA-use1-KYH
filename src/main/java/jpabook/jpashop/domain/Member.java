package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    /* 12
    엔티티 설계시 유의점
    컬렉션은 필드에서 바로 초기화해야
    1. null이슈로부터 안전하다.
    2. Hibernate가 entity를 영속화할때 컬렉션은 감싸서 하이버네이트의 내장 컬렉션이 되는데, 내부 메커니즘에 문제가 발생할 수 있다.
     */
    /* 2
    연관관계 거울이라는걸 주입
    Order자료형 테이블에 있는 member의 거울이다
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
