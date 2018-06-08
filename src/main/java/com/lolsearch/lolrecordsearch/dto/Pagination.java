package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter @ToString
@EqualsAndHashCode
public class Pagination implements Serializable {
    
    private int totalPageCnt; // 총 페이지 수
    private int pageSize; // 한 화면에 보일 페이지 수
    private long totalCnt; // 총 게시물 수
    private int postSize; // 페이지 당 게시물 수
    private long startIdx; // 페이지 당 시작 게시물 인덱스
    private long endIdx; // 페이지 당 마지막 게시물 인덱스
    private int startPage;
    private int endPage;
    private int page;
    
    public Pagination(long totalCnt, int postSize) {
        this(totalCnt, postSize, 1);
    }
    
    public Pagination(long totalCnt, int postSize, int page) {
        this(totalCnt, postSize, page, 5);
    }
    
    public Pagination(long totalCnt, int postSize, int page, int pageSize) {
        this.pageSize = pageSize;
        this.totalCnt = totalCnt;
        this.postSize = postSize;
        this.page = page;
        init();
    }
    
    public void init(){
        this.totalPageCnt = (int) Math.ceil(totalCnt*1.0/postSize);
        this.startPage = (page/(pageSize+1))*pageSize+1;
        this.endPage = startPage+pageSize-1;
        if(endPage > totalPageCnt){
            endPage = totalPageCnt;
        }
        this.startIdx = (this.page-1)*postSize;
        this.endIdx = startIdx + postSize-1;
        if(endIdx >= totalCnt){
            endIdx = totalCnt-1;
        }
    }
    
    public boolean hasPrev() {
        return (startPage-1) > 0;
    }
    
    public boolean hasNext() {
        return (endPage+1) <= totalPageCnt;
    }
    
    public void setPage(int page) {
        this.page = page;
    }
    
}
