package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.entity.enums.OrderStatus;
import jakarta.persistence.TemporalType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderId(Long orderId);

    @Query("""
        SELECT o FROM OrderEntity o
        JOIN o.orderItems oi
        WHERE o.orderStatus = 'PAID'
        GROUP BY o
        ORDER BY COUNT(oi) DESC
        LIMIT 10
        """)
    List<OrderEntity> findTop10PaidOrders();

    @Query("""
                SELECT oi.product FROM OrderItemEntity oi
                JOIN oi.order o
                WHERE o.orderStatus = 'CANCELED'
                GROUP BY oi.product
                ORDER BY COUNT(oi) DESC
                LIMIT 10
            """)
    List<ProductEntity> findTop10CanceledProducts();



    @Query(value = "SELECT * FROM Orders WHERE Status = 'AWAITING_PAYMENT' AND CreatedAt < DATEADD('DAY', -:days, " +
            "NOW())", nativeQuery = true)
    List<OrderEntity> findOrdersAwaitingPayment(@Param("days") int days);

}
