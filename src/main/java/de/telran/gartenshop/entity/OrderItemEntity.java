package de.telran.gartenshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "OrderItems")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class OrderItemEntity {

    @Id
    @Column(name = "OrderItemID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @Column(name = "Quantity")
    private long quantity;

    @Column(name = "PriceAtPurchase")
    private BigDecimal priceAtPurchase;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private ProductEntity products;

    @ManyToOne
    @JoinColumn(name = "OrderID")
    private OrderEntity orders;
}
