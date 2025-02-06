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
//@EqualsAndHashCode
//@ToString
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
            //(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID")
    private ProductEntity product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "OrderID", nullable = false)
    private OrderEntity order;

//    @Override
//    public String toString() {
//        return "OrderItemEntity{" +
//                "orderItemId=" + orderItemId +
//                ", quantity=" + quantity +
//                ", priceAtPurchase=" + priceAtPurchase +
//                '}';
//    }
}
