package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT ce FROM UserEntity ce WHERE ce.email=?1 ")
   UserEntity findByEmail(String email);

}
