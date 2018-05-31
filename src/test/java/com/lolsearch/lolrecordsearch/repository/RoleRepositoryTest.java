package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.Role;
import com.lolsearch.lolrecordsearch.domain.RoleName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@DataJpaTest
public class RoleRepositoryTest {
    
    @Autowired
    private RoleRepository roleRepository;
    
    @Test
    public void testNotNull() {
        assertThat(roleRepository).isNotNull();
    }
    
    @Test
    public void testFindByName() {
        Role role = roleRepository.findByName(RoleName.USER);
    
        assertThat(role).isNotNull();
    
        assertThat(role.getName()).isEqualTo(RoleName.USER);
    }

}