package com.lolsearch.lolrecordsearch.domain;

public enum  RoleName {
    ADMIN, USER;
    
    public String getRoleName() {
        return "ROLE_"+this.toString();
    }
}
