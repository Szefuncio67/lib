package com.example.lib;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void registerUser(UserEntity userEntity);

    @Query("SELECT * FROM users WHERE userId =(:userId) AND password =(:password)")
    UserEntity login(String userId, String password);

    @Insert
    void addToFavorites(FavoriteBook favoriteBook);

    @Query("SELECT * FROM favorite_books WHERE userId = :userId AND bookTitle = :bookTitle AND bookAuthor = :bookAuthor")
    FavoriteBook isBookInFavorites(String userId, String bookTitle, String bookAuthor);

    @Query("SELECT * FROM favorite_books WHERE userId = :userId")
    List<FavoriteBook> getFavoriteBooks(String userId);

    @Update
    void updateFavoriteBook(FavoriteBook favoriteBook);

    @Delete
    void deleteFavoriteBook(FavoriteBook favoriteBook);

    @Query("SELECT * FROM users WHERE userId = :userId")
    UserEntity getUserById(String userId);





}
