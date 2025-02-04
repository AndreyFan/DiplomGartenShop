package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long > {
}
