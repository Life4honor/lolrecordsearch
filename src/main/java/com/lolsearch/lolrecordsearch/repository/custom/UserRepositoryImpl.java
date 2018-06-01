package com.lolsearch.lolrecordsearch.repository.custom;

import com.lolsearch.lolrecordsearch.domain.QUser;
import com.lolsearch.lolrecordsearch.domain.User;
import com.lolsearch.lolrecordsearch.domain.UserStatus;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.Optional;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {
    
    private QUser qUser = QUser.user;
    
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
        
        JPQLQuery<User> jpqlQuery = from(qUser)
                                    .innerJoin(qUser.userState).fetchJoin()
                                    .innerJoin(qUser.roles).fetchJoin()
                                    .where(qUser.email.eq(email));
    
        return jpqlQuery.fetchOne();
    }
    
    @Override
    public long countEmail(String email) {
        
        return from(qUser).where(qUser.email.eq(email)).fetchCount();
    }
    
    @Override
    public long countNickname(String nickname) {
        
        return from(qUser).where(qUser.nickname.eq(nickname)).fetchCount();
    }
    
    @Override
    public long countSummoner(String summoner) {
        
        return from(qUser).where(qUser.summoner.eq(summoner)).fetchCount();
    }
    
    @Override
    public Optional<User> findUserByNickname(String nickname) {
    
        User user = from(qUser)
                    .where(
                        qUser.nickname.eq(nickname)
                        .and(qUser.userState.name.ne(UserStatus.WITHDRAW))
                    ).fetchOne();
    
        return Optional.ofNullable(user);
    }
    
    @Override
    public Optional<User> findUserByEmailAndNickname(String email, String nickname) {
    
        User user = from(qUser).where(qUser.email.eq(email))
                .where(qUser.nickname.eq(nickname))
                .where(qUser.userState.name.ne(UserStatus.WITHDRAW))
                .fetchOne();
    
        return Optional.ofNullable(user);
    }
}
