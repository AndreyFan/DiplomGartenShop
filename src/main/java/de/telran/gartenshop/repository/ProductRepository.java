package de.telran.gartenshop.repository;

import de.telran.gartenshop.dto.querydto.ProductAwaitingPaymentDto;
import de.telran.gartenshop.dto.querydto.ProductProfitDto;
import de.telran.gartenshop.dto.querydto.ProductTopPaidCanceledDto;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.customs.ProductCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductCustomRepository {
    @Query(value = "SELECT * FROM Products " +
            "WHERE (DiscountPrice > 0 AND DiscountPrice IS NOT NULL) " +
            "AND (Price > 0 AND Price IS NOT NULL) " +
            "AND (1 - DiscountPrice / Price = " +
            "(SELECT MAX(1 - DiscountPrice / Price) FROM Products " +
            "WHERE (DiscountPrice > 0 AND DiscountPrice IS NOT NULL) " +
            "AND (Price > 0 AND Price IS NOT NULL)))", nativeQuery = true)
    List<ProductEntity> getProductOfDay();

    @Query(value =
            "SELECT " +
                    "    CASE " +
                    "        WHEN :period = 'DAY' THEN DATE_FORMAT(o.CreatedAt, '%Y-%m-%d') " +
                    "        WHEN :period = 'WEEK' THEN DATE_FORMAT(o.CreatedAt, '%x-%v') " + // %x-%v = год + неделя
                    "        WHEN :period = 'MONTH' THEN DATE_FORMAT(o.CreatedAt, '%Y-%m') " + // %Y-%m = год + месяц
                    "    END AS period, " +
                    "    SUM(oi.Quantity * oi.PriceAtPurchase) AS profit " +
                    "FROM Orders o " +
                    "JOIN OrderItems oi ON o.OrderID = oi.OrderID " +
                    "WHERE o.Status = 'PAID' " +
                    "AND o.CreatedAt >= " +
                    "    CASE " +
                    "        WHEN :period = 'DAY' THEN DATE_SUB(CURRENT_DATE, INTERVAL :value DAY) " +
                    "        WHEN :period = 'WEEK' THEN DATE_SUB(CURRENT_DATE, INTERVAL :value WEEK) " +
                    "        WHEN :period = 'MONTH' THEN DATE_SUB(CURRENT_DATE, INTERVAL :value MONTH) " +
                    "    END " +
                    "GROUP BY " +
                    "    CASE " +
                    "        WHEN :period = 'DAY' THEN DATE_FORMAT(o.CreatedAt, '%Y-%m-%d') " +
                    "        WHEN :period = 'WEEK' THEN DATE_FORMAT(o.CreatedAt, '%x-%v') " + // %x-%v для года и недели
                    "        WHEN :period = 'MONTH' THEN DATE_FORMAT(o.CreatedAt, '%Y-%m') " +
                    "    END",
            nativeQuery = true)
    List<ProductProfitDto> getProductProfitByPeriod(String period, Integer value);

    @Query(value =
            "SELECT  oi.ProductID as productId, p.Name as productName, COUNT(oi.ProductID) as saleFrequency, " +
                    " SUM(oi.Quantity) as saleQuantity, SUM(oi.Quantity*oi.PriceAtPurchase) as saleSumma " +
                    " FROM OrderItems oi " +
                    " JOIN Products p ON oi.ProductID = p.ProductID" +
                    " JOIN Orders o ON oi.OrderId = o.OrderID" +
                    " WHERE o.Status = 'PAID' " +
                    " GROUP BY oi.ProductID " +
                    " ORDER BY saleFrequency DESC, saleSumma DESC " +
                    " LIMIT 10 ",
            nativeQuery = true)
    List<ProductTopPaidCanceledDto> getTop10PaidProducts();

    @Query(value =
            "SELECT  oi.ProductID as productId, p.Name as productName, COUNT(oi.ProductID) as saleFrequency, " +
                    " SUM(oi.Quantity) as saleQuantity, SUM(oi.Quantity*oi.PriceAtPurchase) as saleSumma " +
                    " FROM OrderItems oi " +
                    " JOIN Products p ON oi.ProductID = p.ProductID" +
                    " JOIN Orders o ON oi.OrderId = o.OrderID" +
                    " WHERE o.Status = 'CANCELED' " +
                    " GROUP BY oi.ProductID " +
                    " ORDER BY saleFrequency DESC, saleSumma DESC " +
                    " LIMIT 10 ",
            nativeQuery = true)
    List<ProductTopPaidCanceledDto> getTop10CanceledProducts();

//    @Query("""
//                SELECT oi.product FROM OrderItemEntity oi
//                JOIN oi.order o
//                WHERE o.orderStatus = 'AWAITING_PAYMENT'
//                AND o.createdAt < :cutoffDate
//            """)
//    List<ProductEntity> findOrdersAwaitingPayment(@Param("cutoffDate") LocalDateTime cutoffDate);

    @Query(value =
            "SELECT  oi.ProductID as productId, p.Name as productName, o.OrderID as OrderId, " +
                    " o.CreatedAt as createdAt, oi.Quantity as quantity " +
                    " FROM OrderItems oi " +
                    " JOIN Products p ON oi.ProductID = p.ProductID" +
                    " JOIN Orders o ON oi.OrderId = o.OrderID" +
                    " WHERE o.Status = 'AWAITING_PAYMENT' AND o.CreatedAt < :cutoffDate " +
                    " ORDER BY CreatedAt ASC ",
            nativeQuery = true)
    List<ProductAwaitingPaymentDto> getAwaitingPaymentProducts(@Param("cutoffDate") LocalDateTime cutoffDate);
}
