package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery", fetch = FetchType.LAZY)
    private Order order;

    @Embedded
    private Address address;

    /* 5
    enum을 활용할때 반드시 붙여줘야하는 애노테이션이다.
    붙이지 않을시 기본값인 EnumType.ORDINAL은
    enum 순서대로 index를 붙여 db에 저장하겠다는 옵션인데
    나중에 enum에 새로운 타입을 정의할때
    순차대로 부여하지않고 중간에 끼워넣게 되면
    db가 전부 꼬여버린다
     */
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;


}

