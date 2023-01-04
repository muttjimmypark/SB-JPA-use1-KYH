package jpabook.jpashop.Service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;


    @Test
    void 상품주문() {
        int price = 10000;
        int stock = 10;
        int orderCount = 2;

        //given
        Member member = createMember("회원1");

        Item book = createBook("시골jpa", price, stock);

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order foundOrder = orderRepository.findOne(orderId);
        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(foundOrder.getOrderItems().size()).isEqualTo(1);
        assertThat(foundOrder.getTotalPrice()).isEqualTo(price * orderCount);
        assertThat(book.getStockQuantity()).isEqualTo(stock - orderCount);
    }


    private Item createBook(String name, int price, int stock) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stock);
        em.persist(book); //item은 cascade로 엮어놓지 않은 단방향관계로, 따로 persist 날려줘야. 사실 이게 일반적
        return book;
    }


    private Member createMember(String name) {
        Member member = new Member();
        member.setName(name);
        member.setAddress(new Address("서울", "진흥로", "18603"));
        em.persist(member);
        return member;
    }


//    @Test(expected = NotEnoughStockException.class)
    @Test
    void 상품주문_재고수량초과() {
        //given
        Member member = createMember("회원1");
        Item book = createBook("시골JPA", 10000, 10);
        int orderCount = 11;

        //when
//        orderService.order(member.getId(), book.getId(), orderCount);

        //then
        assertThatThrownBy(() -> orderService.order(member.getId(), book.getId(), orderCount))
                .isInstanceOf(NotEnoughStockException.class);

    }


    @Test
    void 주문취소() {
        //given
        Member member = createMember("회원1");
        Item book = createBook("시골jpa", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order foundOrder = orderRepository.findOne(orderId);

        assertThat(foundOrder.getStatus()).isEqualTo(OrderStatus.CANCEL);
        assertThat(book.getStockQuantity()).isEqualTo(10);
    }
}