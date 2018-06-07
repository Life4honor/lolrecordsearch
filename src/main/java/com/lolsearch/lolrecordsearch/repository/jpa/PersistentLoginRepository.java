package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.PersistentLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersistentLoginRepository extends JpaRepository<PersistentLogin, String> {
    
    PersistentLogin findByUsername(String username);
    
}
