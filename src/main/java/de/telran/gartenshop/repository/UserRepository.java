package de.telran.gartenshop.repository;

import de.telran.gartenshop.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT u FROM UserEntity u WHERE u.email=?1 ")
   UserEntity findByEmail(String email);

    @Query("SELECT u FROM UserEntity u WHERE u.refreshToken=?1")
    List<UserEntity> getByRefreshToken(String refreshToken);

}
