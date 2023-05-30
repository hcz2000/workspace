package com.zhan.myreader.greendao.entity;



/**
 * 绔犺妭
 * Created by zhao on 2017/7/24.
 */


public class Chapter {

    private String id;

    private String bookId;//绔犺妭鎵�灞炰功鐨処D
    private int number;//绔犺妭搴忓彿
    private String title;//绔犺妭鏍囬
    private String url;//绔犺妭閾炬帴
    private String content;//绔犺妭姝ｆ枃


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
