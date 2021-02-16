package com.natasha.googlebooksapp.model;

import java.io.Serializable;

//Represents the blueprint for a book
//we are implementing Serializable so we can pass it around easily from one activity to another
public class Book implements Serializable {
    private static final long id = 1L;

    private String title;
    private String authors;
    private String publishedDate;
    private String description;
    private String pageCount;
    private String categories;
    private String previewLink;
    private String infoLink;
    private String saleInfo;
    private String bookPrice;
    private String thumbnail;
    private String buy;
    private int bookID;

    //empty constructor
    public Book(){

    }

    public Book(String title, String authors, String publishedDate, String description, String pageCount, String categories, String previewLink, String bookPrice, String thumbnail, String buy, int bookID) {
        this.title = title;
        this.authors = authors;
        this.publishedDate = publishedDate;
        this.description = description;
        this.pageCount = pageCount;
        this.categories = categories;
        this.previewLink = previewLink;
        this.bookPrice = bookPrice;
        this.thumbnail = thumbnail;
        this.buy = buy;
        this.bookID = bookID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(String publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getPreviewLink() {
        return previewLink;
    }

    public void setPreviewLink(String previewLink) {
        this.previewLink = previewLink;
    }

    public String getInfoLink() {
        return infoLink;
    }

    public void setInfoLink(String infoLink) {
        this.infoLink = infoLink;
    }

    public String getSaleInfo() {
        return saleInfo;
    }

    public void setSaleInfo(String saleInfo) {
        this.saleInfo = saleInfo;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getThumbnail() { return thumbnail; }

    public void setThumbnail(String thumbnail) { this.thumbnail = thumbnail; }

    public String getBuy() { return buy; }

    public void setBuy(String buy) { this.buy = buy; }

    public int getBookID() { return bookID; }

    public void setBookID(int bookID) { this.bookID = bookID; }
}
