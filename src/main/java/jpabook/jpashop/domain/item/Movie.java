package jpabook.jpashop.domain.item;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("M") //없으면 클래스명이 기본값
@Getter
@Setter
public class Movie extends Item {

    private String director;

    private String actor;
}
