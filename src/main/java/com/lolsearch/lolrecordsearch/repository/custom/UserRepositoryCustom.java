package com.lolsearch.lolrecordsearch.repository.custom;

import com.lolsearch.lolrecordsearch.domain.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    
    User findByEmail(String email);
    
    long countEmail(String email);
    
    long countNickname(String nickname);
    
    long countSummoner(String summoner);
    
    Optional<User> findUserByNickname(String nickname);
    
    Optional<User> findUserByEmailAndNickname(String email, String nickname);
    
}
