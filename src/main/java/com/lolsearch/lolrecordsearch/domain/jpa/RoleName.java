package com.lolsearch.lolrecordsearch.domain.jpa;

public enum  RoleName {
    ADMIN, USER, TEST;
    
    public String getRoleName() {
        return "ROLE_"+this.toString();
    }
}
