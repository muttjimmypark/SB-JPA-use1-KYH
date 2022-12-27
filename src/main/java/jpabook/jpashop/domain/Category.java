package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/* 9
Getter, Setter를 어떻게 부여해야할까
getter는 조회고 데이터에 변형이 발생하지 않으며, 실제 호출도 잦기때문에 다 열어두는것이 편리
하지만 setter를 무분별하게 전부 열어놓으면 언제 어느시점에 데이터가 수정됐는지 파악을 할수 없게된다

엔티티 변경을 위한 비즈니스 메서드를 별도로 제공해야한다.
변경 지점 시점을 명확히 할수 있도록
 */
@Entity
@Getter
@Setter
public class Category {

    /* 10
    식별자 필드명은 id로 통일
    엔티티는 독립적인 타입(클래스)가 존재하므로 Category.id로 분간할수 있다
    하지만 테이블은 전부 테이블이고 타입을 분류하지 않으므로 컬럼명을 따로 명명
     */
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /* 7
    다대다 관계는 중간에 JoinTable이 생성되도록 설계
     */
    @ManyToMany
    @JoinTable(
            name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    private List<Item> items = new ArrayList<>();

    /* 8
    나 자신에 연관관계 매핑을 부여해서
    카테고리 같은 정보를 다룰때의 수직구조를 구현할 수 있다.
     */
    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> chlid = new ArrayList<>();
}
