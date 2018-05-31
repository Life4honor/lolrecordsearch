package com.lolsearch.lolrecordsearch.repository.custom;

import com.lolsearch.lolrecordsearch.domain.User;

public interface UserRepositoryCustom {
    
    User findByEmail(String email);
    
    long countEmail(String email);
    
    long countNickname(String nickname);
    
    long countSummoner(String summoner);
    
}
