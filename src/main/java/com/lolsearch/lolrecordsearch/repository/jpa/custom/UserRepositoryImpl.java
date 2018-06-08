package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.QUser;
import com.lolsearch.lolrecordsearch.domain.jpa.User;
import com.lolsearch.lolrecordsearch.domain.jpa.UserState;
import com.lolsearch.lolrecordsearch.domain.jpa.UserStatus;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import javax.persistence.EntityManager;
import java.util.Optional;

public class UserRepositoryImpl extends QuerydslRepositorySupport implements UserRepositoryCustom {
    
    private QUser qUser = QUser.user;
    
    public UserRepositoryImpl() {
        super(User.class);
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
    
    @Override
    public long updateUserNickname(Long id, String nickname) {
    
        return update(qUser).set(qUser.nickname, nickname).where(qUser.id.eq(id)).execute();
    }
    
    @Override
    public long updateUserSummoner(Long id, String summoner) {
    
        return update(qUser).set(qUser.summoner, summoner).where(qUser.id.eq(id)).execute();
    }
    
    @Override
    public long updateUserPassword(Long id, String password) {
        
        return update(qUser).set(qUser.password, password).where(qUser.id.eq(id)).execute();
    }
    
    @Override
    public long updateUserState(Long id, UserState userState) {
        
        return update(qUser).set(qUser.userState, userState).where(qUser.id.eq(id)).execute();
    }
}
