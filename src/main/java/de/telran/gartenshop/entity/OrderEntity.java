package de.telran.gartenshop.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import de.telran.gartenshop.entity.enums.DeliveryMethod;
import de.telran.gartenshop.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = {"user", "orderItems"})
@EqualsAndHashCode(exclude = {"user", "orderItems"})
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OrderID")
    private Long orderId;

    @Column(name = "CreatedAt")
    private Timestamp createdAt;

    @Column(name = "DeliveryAddress")
    private String deliveryAddress;

    @Column(name = "ContactPhone")
    private String contactPhone;

    @Enumerated(EnumType.STRING)
    @Column(name = "DeliveryMethod")
    private DeliveryMethod deliveryMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status")
    private OrderStatus orderStatus;

    @Column(name = "UpdatedAt")
    private Timestamp updatedAt;

    @ManyToOne
    @JoinColumn(name = "UserID")
    @JsonBackReference
    private UserEntity user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<OrderItemEntity> orderItems = new HashSet<>();
}
