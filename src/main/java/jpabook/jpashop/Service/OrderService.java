package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    //주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        /**
         * repo에서 끌어온 member 외에 나머지는 여기 메서드에서 생성됐으며
         * persist를 다 날려줘야 하지만
         * 앞서 Order에 작성한 cascade 옵션덕분에
         * 연관관계에 엮여있는 엔티티들의 persist도 같이 날려준다
         *
         * 본 order를 repo에 저장시키는 시점에 발동
         *
         * delivery, orderItem은 라이프사이클 상 order만 참조함
         * (orderItem이 Item을 참조하지만 orderItem'을' 참조하는건 order뿐)
         * 그런 private한 관계가 아니라면 cascade 함부로 사용 x
         */

        //주문 저장
        orderRepository.save(order);
        return order.getId();
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
        /**
         * 주문에서 - 배송상태.완료 만 아니면 주문상태.취소 로 변경, 엮인 주문상품들을 캔슬
         * 주문상품에서 - 해당 아이템 재고를 원복
         *
         * 원래같으면 item entity의 쿼리를 직접 날려야 하지만
         * JPA는 로직이 데이터를 변경시키면
         * 알아서 변경내역감지(더티체킹)해서 알아서 쿼리를 날린다!
         */

        /**
         * entity에 비즈니스 로직이 있어서 객체지향의 특성을 적극활용하는 것을
         * 도메인 모델 패턴 이라고한다. -> 서비스레이어에서 엔티티에 동작호출만 한다.
         * [JPA 등 orm을 사용하면]
         *
         * [단순 sql를 사용하면]
         * 반대로, 서비스계층에서 비즈니스 로직을 구성하고 처리하는 것을
         * 트랜잭션 스크립트 패턴 이라고한다.
         */
    }

    //검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch)
//    }
}

