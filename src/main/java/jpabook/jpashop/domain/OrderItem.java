package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    /* 4
    주문상품-상품 관계는 단방향관계로, item에는 연관관계 거울이 없다
    상품이 '나를 호출하는 주문을 모두 찾아'하는 동작을 필요로하지 않기때문

    사실 Member-Order간, Order-OrderItem간
    양방향관계는 실무에서 쓰지않도록, 설계단계에서부터 고려해야한다.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    //주문가격
    private int orderPrice;

    //주문수량
    private int count;


    /**
     * 생성 메서드
     */
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();

        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);

        return orderItem;
    }


    /**
     * 비즈니스 로직
     * 주문취소
     */
    public void cancel() {
        getItem().addStock(count); //관계를 닫기위해 관계맺은 상품의 재고를 원복

        //this.setStatus(OrderItemStatus.CANCEL);
    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
