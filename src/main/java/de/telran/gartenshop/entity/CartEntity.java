package de.telran.gartenshop.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="Cart")
public class CartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CartID")
    private Long cartId;

    @OneToOne
    @JoinColumn(name="UserID", referencedColumnName = "UserId")
    private UserEntity user;

    public CartEntity() {
    }

    public CartEntity(Long cartId, UserEntity user) {
        this.cartId = cartId;
        this.user = user;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Long getCartId() {
        return cartId;
    }



    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }


}
