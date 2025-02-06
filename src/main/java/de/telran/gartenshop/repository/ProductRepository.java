package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.customs.ProductCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductCustomRepository {
    @Query(value = "SELECT * FROM Products WHERE DiscountPrice =(SELECT max(DiscountPrice) FROM Products)", nativeQuery = true)
    List<ProductEntity> getProductOfDay();
}
