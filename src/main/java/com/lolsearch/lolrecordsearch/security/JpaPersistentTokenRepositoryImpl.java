package com.lolsearch.lolrecordsearch.security;

import com.lolsearch.lolrecordsearch.domain.PersistentLogin;
import com.lolsearch.lolrecordsearch.repository.PersistentLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Transactional(readOnly = true)
@Service
public class JpaPersistentTokenRepositoryImpl implements PersistentTokenRepository {
    
    @Autowired
    private PersistentLoginRepository persistentLoginRepository;
    
    @Transactional
    @Override
    public void createNewToken(PersistentRememberMeToken token) {
        PersistentLogin persistentLogin = new PersistentLogin();
        persistentLogin.setSeries(token.getSeries());
        persistentLogin.setToken(token.getTokenValue());
        persistentLogin.setUsername(token.getUsername());
    
        LocalDateTime localDateTime = convertToLocalDateTime(token.getDate());
        persistentLogin.setLastUsed(localDateTime);
    
        persistentLoginRepository.save(persistentLogin);
    }
    
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    
    @Transactional
    @Override
    public void updateToken(String series, String tokenValue, Date lastUsed) {
    
        Optional<PersistentLogin> optionalPersistentLogin = persistentLoginRepository.findById(series);
        
        if(!optionalPersistentLogin.isPresent()) {
            return;
        }
    
        PersistentLogin persistentLogin = optionalPersistentLogin.get();
        persistentLogin.setToken(tokenValue);
        persistentLogin.setLastUsed(convertToLocalDateTime(lastUsed));
    }
    
    @Override
    public PersistentRememberMeToken getTokenForSeries(String seriesId) {
        Optional<PersistentLogin> optionalPersistentLogin = persistentLoginRepository.findById(seriesId);
        
        if(!optionalPersistentLogin.isPresent()) {
            return null;
        }
    
        PersistentLogin persistentLogin = optionalPersistentLogin.get();
        
        Date lastUsed = convertToDate(persistentLogin.getLastUsed());
        
        return new PersistentRememberMeToken(persistentLogin.getUsername(), persistentLogin.getSeries(), persistentLogin.getToken(), lastUsed);
    }
    
    public Date convertToDate(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
    
    @Transactional
    @Override
    public void removeUserTokens(String username) {
        PersistentLogin persistentLogin = persistentLoginRepository.findByUsername(username);
        if(persistentLogin == null) {
            return;
        }
        persistentLoginRepository.delete(persistentLogin);
    }
}
