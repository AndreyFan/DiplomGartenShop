package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.OrderEntity;
import de.telran.gartenshop.entity.enums.OrderStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository <OrderEntity, Long> {
    Optional<OrderEntity> findByOrderId(Long orderId);
    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity " +
            "FROM OrderItemEntity oi " +
            "GROUP BY oi.product " +
            "ORDER BY totalQuantity DESC " +
            "LIMIT 10")
    List<Object[]> findTop10Products();

    @Query("SELECT o, SUM(oi.quantity) as totalQuantity " +
            "FROM OrderEntity o " +
            "JOIN o.orderItems oi " +
            "WHERE o.orderStatus = 'CANCELED' " +
            "GROUP BY o " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> findTop10CanceledOrders();

//    @Query("SELECT oi.product, SUM(oi.quantity) as totalQuantity " +
//            "FROM OrderEntity o " +
//            "JOIN o.orderItems oi " +
//            "WHERE o.orderStatus = 'AWAITING_PAYMENT' " +
//            "AND DATEDIFF(CURRENT_DATE, o.createdAt) > :days " +
//            "GROUP BY oi.product " +
//            "ORDER BY totalQuantity DESC")
//    List<Object[]> findProductsAwaitingPaymentForMoreThanNDays(@Param("days") int days);

}
