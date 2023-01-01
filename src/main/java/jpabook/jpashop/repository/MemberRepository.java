package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /**
     * 영속성컨텍스트 : 스프링이 em 만들고 주입함
     * @PersistenceContext
     *
     * 스프링데이터jpa가 @Autowired로도 영속성컨텍스트 주입할수 있게 해줌
     * 그러면 생성자주입 -> final 부여후 lombok으로 생략 (MemberService 작성처럼) 가능
     */
//    @PersistenceContext
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
