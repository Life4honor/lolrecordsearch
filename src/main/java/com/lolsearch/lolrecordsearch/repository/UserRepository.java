package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.User;
import com.lolsearch.lolrecordsearch.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
