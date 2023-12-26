package com.study.springbatch.core.domain.orders;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderItem;

    private int price;

    private LocalDateTime orderDate;

    public Orders(String orderItem, int price, LocalDateTime orderDate) {
        this.orderItem = orderItem;
        this.price = price;
        this.orderDate = orderDate;
    }
}
