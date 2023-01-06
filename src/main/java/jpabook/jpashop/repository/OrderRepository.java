package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    public List<Order> findAllEveryParams(OrderSearch orderSearch) {

        /**
         * 요구사항에서, 다양한 검색옵션이 들어가게됨
         * 동적 쿼리가 들어가야함
         *
         * orderSearch에 param이 꼭 다 들어있을 때
         * = 주문조회검색에서 회원이름, 주문상태를 정해서 검색날릴때
         *
         * 검색 param을 아무것도 안넣었을때는 이렇게 처리할수 없고
         * 동적쿼리가 적용되어야 한다
         */
        //JPQL join : 객체를 참조하는 스타일
        return em.createQuery("select o from Order o join o.member m" +
                        " where o.status = :status " +
                        " and m.name like :name", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("name", orderSearch.getMemberName())
                .setMaxResults(1000) //최대 1000건
                .getResultList();
    }

    public List<Order> findAllByString(OrderSearch orderSearch) {

        /**
         * 동적쿼리 적용 방법 1. jpql 명령을 생짜로 (?)
         * (배우려는 방법의 비교군이므로 예제 복붙)
         *
         * 경우마다 if문 걸어서 jpql과 쿼리에 덧붙이기를 하는 형식으로
         * 쿼리 하나 날리자고 코드가 너무 길어지고
         * 타이핑 실수로 인한 버그가 자주 발생하며, 오류 발생시 원인찾기가 어려워진다.
         */
        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }

        //쿼리에 param으로 부여
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }

        //완성 후 리턴
        return query.getResultList();
    }

    public List<Order> findAllByCriteria(OrderSearch orderSearch) {

        /**
         * 동적쿼리 적용 방법 2. JPA Criteria를 사용
         * jpa 표준 스펙에서 제공하는 기능
         * (역시 비교군이므로 예제 복붙)
         *
         * if문 대신 제공되는 메서드와의 대화형으로 구성할 수 있는 방법
         * 유지보수하기 어렵다는 단점. 무슨 쿼리가 나가는건지 직관적으로 파악이 매우 어려움
         */

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> cq = cb.createQuery(Order.class);
        Root<Order> o = cq.from(Order.class);
        Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
        List<Predicate> criteria = new ArrayList<>();

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            Predicate status = cb.equal(o.get("status"),
                    orderSearch.getOrderStatus());
            criteria.add(status);
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            Predicate name =
                    cb.like(m.<String>get("name"), "%" +
                            orderSearch.getMemberName() + "%");
            criteria.add(name);
        }

        cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
        TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건

        return query.getResultList();
    }

    public List<Order> findAll(OrderSearch orderSearch) {

        /**
         * 동적쿼리 적용 방법 3. QueryDSL로 처리
         * 직관적이고 간단하게 작성할 수 있다.
         * 정적쿼리도 복잡해지면 이거로 짜는 것을 추천
         *
         * 로드맵 제일 나중에 다루기로 함
         */

        return null;
    }
}
