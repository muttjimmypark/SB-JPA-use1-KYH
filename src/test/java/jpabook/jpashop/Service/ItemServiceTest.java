package jpabook.jpashop.Service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * 강의에서 생략된 부분, MemberServiceTest 복습 개념으로 직접 진행
 */

@ExtendWith(SpringExtension.class) //이 부분의 의의를 아직 모르겠어. 2회독때 확인
@SpringBootTest //스프링부트 환경 위에서 테스트 돌린다
@Transactional //트랜잭션은 테스트환경에서 기본값이 롤백
class ItemServiceTest {

    /**
     * 해당 테스트의 같은 계통 autowired
     */
    @Autowired
    ItemService itemService;
    @Autowired
    ItemRepository itemRepository;

    @Test
    void 상품저장() {
        //given
        Item item = new Book();
        item.setName("bible");

        //when
        Long savedId = itemService.saveItem(item);

        //then
        assertThat(itemRepository.findOne(savedId)).isEqualTo(item);

    }
}