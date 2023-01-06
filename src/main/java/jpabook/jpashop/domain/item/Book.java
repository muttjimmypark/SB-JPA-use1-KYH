package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("B") //없으면 클래스명이 기본값
@Getter
@Setter
public class Book extends Item {

    private String author;

    private String isbn;
}
