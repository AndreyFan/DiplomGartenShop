package de.telran.gartenshop.entity;

//CartItems - товары в корзине

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "CartItems")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CartItemEntity {

    @Id
    @Column(name = "CartItemID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @Column(name = "Quantity")
    private Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CartID")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProductID")
    private ProductEntity product;


}
