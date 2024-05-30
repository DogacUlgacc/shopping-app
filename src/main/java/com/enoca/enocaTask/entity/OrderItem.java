package com.enoca.enocaTask.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Table(name = "order_items")
@Data
public class OrderItem  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;


    private int quantity;

}
