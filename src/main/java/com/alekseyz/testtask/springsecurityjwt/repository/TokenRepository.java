package com.alekseyz.testtask.springsecurityjwt.repository;

import com.alekseyz.testtask.springsecurityjwt.entity.Token;
import com.alekseyz.testtask.springsecurityjwt.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<List<Token>> findTokensByUserAndExpiredFalseAndRevokedFalse(User user);

    Optional<Token> findByToken(String token);
}
