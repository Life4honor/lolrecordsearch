package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.UserState;
import com.lolsearch.lolrecordsearch.domain.UserStatus;
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
public class UserStateRepositoryTest {
    
    @Autowired
    private UserStateRepository userStateRepository;
    
    @Test
    public void testNotNull() {
        assertThat(userStateRepository).isNotNull();
    }
    
    @Test
    public void testFindByName() {
        UserState userState = userStateRepository.findByName(UserStatus.NORMAL);
    
        assertThat(userState).isNotNull();
    
        assertThat(userState.getName()).isEqualTo(UserStatus.NORMAL);
    }
    
}