package de.telran.gartenshop.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "OrderItems")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = {"product", "order"})
@ToString(exclude = {"product", "order"})
public class OrderItemEntity {

    @Id
    @Column(name = "OrderItemID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemId;

    @Column(name = "Quantity")
    private Integer quantity;

    @Column(name = "PriceAtPurchase")
    private BigDecimal priceAtPurchase;

    @ManyToOne
    @JoinColumn(name = "ProductID")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "OrderID")
    @JsonIgnore
    private OrderEntity order;
}
