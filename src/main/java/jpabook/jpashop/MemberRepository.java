package jpabook.jpashop;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;


    public Long save(Member member) {
        em.persist(member);

        // 커멘드와 쿼리를 분리하라. 무분별한 엔티티반환을 피하는 스타일
        return member.getId();
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }

}
