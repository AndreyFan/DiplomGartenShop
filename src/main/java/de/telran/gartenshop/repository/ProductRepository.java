package de.telran.gartenshop.repository;

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
}
