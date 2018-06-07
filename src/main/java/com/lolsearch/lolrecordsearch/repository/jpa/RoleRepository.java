package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.Role;
import com.lolsearch.lolrecordsearch.domain.jpa.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByName(RoleName roleName);

}
