package com.lolsearch.lolrecordsearch.repository.custom;

import com.lolsearch.lolrecordsearch.domain.QUser;
import com.lolsearch.lolrecordsearch.domain.User;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {
    
    @Autowired
    public UserRepositoryImpl(EntityManager entityManager) {
        this(User.class);
        setEntityManager(entityManager);
    }
    
    public UserRepositoryImpl(Class<?> domainClass) {
        super(domainClass);
    }
    
    @Override
    public User findByEmail(String email) {
        
        QUser qUser = QUser.user;
    
        JPQLQuery<User> jpqlQuery = from(qUser)
                                    .innerJoin(qUser.userState).fetchJoin()
                                    .innerJoin(qUser.roles).fetchJoin()
                                    .where(qUser.email.eq(email));
    
        return jpqlQuery.fetchOne();
    }
}
