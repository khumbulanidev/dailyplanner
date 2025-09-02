package com.khumbu.dailyplanner.repository;

import com.khumbu.dailyplanner.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<RefreshToken,Long> {

    Optional<RefreshToken> findByRefreshToken(java.lang.String string);

    @Query(value = "SELECT * FROM refresh_token WHERE email = :email", nativeQuery = true)
    Optional<RefreshToken> findByEmail(java.lang.String email);
}
