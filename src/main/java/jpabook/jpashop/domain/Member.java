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

    /* 2
    연관관계 거울이라는걸 주입
    Order자료형 테이블에 있는 member의 거울이다
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
