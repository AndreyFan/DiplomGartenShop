package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.ProductEntity;
import de.telran.gartenshop.repository.customs.ProductCustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductCustomRepository {
}
