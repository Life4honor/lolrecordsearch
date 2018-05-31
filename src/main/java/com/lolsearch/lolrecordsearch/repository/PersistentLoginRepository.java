package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {
    
    PersistentLogin findByUsername(String username);
    
}
