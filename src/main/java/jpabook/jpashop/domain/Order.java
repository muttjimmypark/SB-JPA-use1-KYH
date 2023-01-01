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

    /* 11
    모든 연관관계는 지연로딩으로 설정
    @XToOne에서 기본값인 (fetch = FetchType.EAGER)로 놔두면
    처음 jpql이 연관관계에 해당하는 컬럼의 모든 원소를 불러와버려 불필요한 성능낭비를 초래 (n+1 문제라고 한다)
    때문에 필요할때만 불러오는 지연로딩으로 설정하기 위해 lazy를 넣어줘야한다.

    (@OneToMany는 LAZY가 디폴트)
    (연관된 엔티티를 db에서 조회할 일이 생기면 fetch join 또는 엔티티그래프 기능을 사용 : 기본편에서 다루니까 2회독때 체크)
     */
    /* 1
    일대다 관계에서는
    다 쪽에 fk가 있으므로 연관관계 주인이고 (주인임을 주입하기위해 별다른 표기를 넣진 않는다)
    일 쪽은 반대로 연관관계 거울이 된다

    JoinColumn은 fk의 원본이 어느 pk인지 확인하는
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    /* 13
    cascade (폭포, 직렬)
    order의 변경사항 persist가 발생하면, 해당하는 orderItems에 모두 persist 반영
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    /* 6
    일대일 관계의 fk는 어디에 두든 각각 장단점이 있는데,
    자주 접근하는 테이블쪽에 지정해서 연관관계 주인을 부여하는쪽이
    주로 선택된다.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;


    /* 14
    아래는 연관관계 편의 메서드 들이다
    양방향관계에서 한쪽 엔티티의 메서드만 동작시켜도 양쪽에 반영되게 설계하는것
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
