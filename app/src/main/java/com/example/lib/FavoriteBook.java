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
}

