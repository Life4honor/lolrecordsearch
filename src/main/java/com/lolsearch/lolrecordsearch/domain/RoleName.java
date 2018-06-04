package com.lolsearch.lolrecordsearch.domain;

public enum  RoleName {
    ADMIN, USER, TEST;
    
    public String getRoleName() {
        return "ROLE_"+this.toString();
    }
}
