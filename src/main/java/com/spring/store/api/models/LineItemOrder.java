package com.spring.store.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Getter
@Setter
@Table(name = "line_item_orders")
public class LineItemOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private String amount;

    @Column(name = "size")
    private String size;

    @Column(name = "total")
    private String total;

    @Column(name = "status")
    private String status;

    @Column(name = "product_price")
    private String productPrice;

    @OneToOne(fetch = FetchType.LAZY/*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @JsonIgnore
    private Order order;
}
