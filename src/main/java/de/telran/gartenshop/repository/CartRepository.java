package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {
}
