package com.example.expense_tracker.repository;

import com.example.expense_tracker.domain.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query(value = """
      select t from Token t inner join User u\s
      on t.registeredUser.id = u.id\s
      where u.id = :id and (t.expired = false or t.revoked = false)\s
      """)
    List<Token> findAllValidTokenByUser(Integer id);
    @Query(value = """
    select t from Token t
    where t.token = :token and (t.expired = false or t.revoked = false)
    """)
    Optional<Token> findOneByToken(String token);
}
