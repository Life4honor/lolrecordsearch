package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.User;
import com.lolsearch.lolrecordsearch.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    
    Optional<User> findUserByNickname(String nickname);
    
    Optional<User> findUserByEmailAndNickname(String email, String nickname);
    
}
