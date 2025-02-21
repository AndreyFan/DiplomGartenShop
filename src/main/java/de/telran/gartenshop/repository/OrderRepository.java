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
                SELECT oi.product FROM OrderItemEntity oi
                JOIN oi.order o
                WHERE o.orderStatus = 'PAID'
                GROUP BY oi.product
                ORDER BY COUNT(oi) DESC
                LIMIT 10
            """)
    List<ProductEntity> findTop10PaidOrders();

    @Query("""
                SELECT oi.product FROM OrderItemEntity oi
                JOIN oi.order o
                WHERE o.orderStatus = 'CANCELED'
                GROUP BY oi.product
                ORDER BY COUNT(oi) DESC
                LIMIT 10
            """)
    List<ProductEntity> findTop10CanceledProducts();



    @Query("""
    SELECT oi.product FROM OrderItemEntity oi
    JOIN oi.order o
    WHERE o.orderStatus = 'AWAITING_PAYMENT'
    AND o.createdAt < :cutoffDate
""")
    List<ProductEntity> findOrdersAwaitingPayment(@Param("cutoffDate") LocalDateTime cutoffDate);


}
