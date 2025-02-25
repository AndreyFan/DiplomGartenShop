package de.telran.gartenshop.repository;

import de.telran.gartenshop.dto.queryDto.ProductProfitDto;
import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.customs.ProductCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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
}
