package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") //order by와 혼선을 막기 위해 db테이블이름을 별도 명명
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    /* 1
    일대다 관계에서는
    다 쪽에 fk가 있으므로 연관관계 주인이고 (주인임을 주입하기위해 별다른 표기를 넣진 않는다)
    일 쪽은 반대로 연관관계 거울이 된다

    JoinColumn은 fk의 원본이 어느 pk인지 확인하는
     */
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    /* 6
    일대일 관계의 fk는 어디에 두든 각각 장단점이 있는데,
    자주 접근하는 테이블쪽에 지정해서 연관관계 주인을 부여하는쪽이
    주로 선택된다.
     */
    @OneToOne
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
