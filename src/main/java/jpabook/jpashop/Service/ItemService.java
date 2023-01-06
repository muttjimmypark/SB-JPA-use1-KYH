package jpabook.jpashop.Service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long saveItem(Item item) {
        /**
         * 트랜젝셔널 부여를 클래스단에서 기본 readOnly로 두고
         * 아닌 부분만 오버라이딩 식으로 작성하는 편의
         */
        itemRepository.save(item);

        return item.getId();
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }


    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);

        /**
         * Transactional이니까 flush가 날라가므로
         * 변경감지로 데이터가 변경되는 더 나은 방법, 안전한 방법
         *
         * 변경감지는 원하는 속성만 선택해서 변경할 수 있지만
         * 병합을 사용하면 모든 속성이 변경된다.
         * 병합시 값이 없는 필드를 null로 업데이트 해버리기때문에 위험하기 때문
         * ex> 통으로 불러와서 재입력을 받아내는데, private 필드라 로드부터 안되고 그걸 병합으로 밀어버리면 null저장이 돼버림
         */
    }
}
