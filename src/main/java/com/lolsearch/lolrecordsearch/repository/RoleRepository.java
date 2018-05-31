package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.Role;
import com.lolsearch.lolrecordsearch.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(RoleName roleName);

}
