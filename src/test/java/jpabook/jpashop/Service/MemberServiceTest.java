package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.fail;

@ExtendWith(SpringExtension.class) //@RunWith(SpringRunner.class)를 대신함
@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
//    @Rollback(false)
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member);

        //then
//        em.flush();
        assertThat(memberRepository.findOne(savedId)).isEqualTo(member);

        /*
        돌려보면 insert문이 없음
        persist contxt위에서 돌려보고
        transactional이 testcase에서 롤백시킴
        Rollbak(false)나 em.flush를 넣어주면 db에 반영한다는 동작이므로 insert문이 보일거임
         */
    }

    /*
    expected가 JUnit4라서 끌어올수 없음
     */
//    @Test(expected = IllegalStateException.class)
//    void 중복회원예외_강의내용() {
//        //given
//        Member member1 = new Member();
//        member1.setName("kim");
//
//        Member member2 = new Member();
//        member2.setName("kim");
//
//        //when
//        memberService.join(member1);
//        memberService.join(member2);
//
//        //then
//        fail("예외가 발생해야 한다.");
//    }

    @Test
    void 중복회원예외_내가작성() {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);

        //then
        assertThatThrownBy(() -> memberService.join(member2))
                .isInstanceOf(IllegalStateException.class);
    }


    /**
     * test 경로가 별도의 리소스폴더/application.yml을 가지고 있으면
     * 별도의 환경설정을 할수 있다.
     * jdbc:h2:mem:test
     *
     * 스프링부트는 설정값을 다 지웠을시 메모리db에서 돌아가게 지원해준다
     */
}