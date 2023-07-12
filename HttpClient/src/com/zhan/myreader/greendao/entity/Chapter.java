package com.zhan.myreader.greendao.entity;



/**
 * 绔犺妭
 * Created by zhao on 2017/7/24.
 */


public class Chapter {

    private String id;

    private String bookId;
    private int number;
    private String title;
    private String url;
    private String content;


    public Chapter(String id, String bookId, int number, String title, String url,
                   String content) {
        this.id = id;
        this.bookId = bookId;
        this.number = number;
        this.title = title;
        this.url = url;
        this.content = content;
    }
    public Chapter() {
    }
    public String getId() {
        return this.id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getBookId() {
        return this.bookId;
    }
    public void setBookId(String bookId) {
        this.bookId = bookId;
    }
    public int getNumber() {
        return this.number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getUrl() {
        return this.url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
