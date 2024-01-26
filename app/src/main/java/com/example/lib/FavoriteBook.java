package com.example.lib;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity(tableName = "favorite_books",
        primaryKeys = {"userId", "bookTitle", "bookAuthor"})
public class FavoriteBook {
    @NonNull
    @ColumnInfo(name = "userId")
    private String userId;


    @NonNull
    @ColumnInfo(name = "bookTitle")
    private String bookTitle;

    @NonNull
    @ColumnInfo(name = "bookAuthor")
    private String bookAuthor;

    @ColumnInfo(name = "numberOfPages")
    private String numberOfPages;

    @ColumnInfo(name = "cover")
    private String cover;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "actualPage")
    private String actualPage;

    @ColumnInfo(name = "first_publish_year")
    private String firstPublishYear;
    @ColumnInfo(name = "language")
    private String language;

    @ColumnInfo(name = "first_sentence")
    private String firstSentence;

    // Getters and setters

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getActualPage() {
        return actualPage;
    }

    public void setActualPage(String actualPage) {
        this.actualPage = actualPage;
    }

    public String getFirstPublishYear() {
        return firstPublishYear;
    }

    public void setFirstPublishYear(String firstPublishYear) {
        this.firstPublishYear = firstPublishYear;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getFirstSentence() {
        return firstSentence;
    }

    public void setFirstSentence(String firstSentence) {
        this.firstSentence = firstSentence;
    }
}

