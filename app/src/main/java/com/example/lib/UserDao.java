package com.example.lib;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void registerUser(UserEntity userEntity);

    @Query("SELECT * FROM users WHERE userId =(:userId) AND password =(:password)")
    UserEntity login(String userId, String password);

    @Insert
    void addToFavorites(FavoriteBook favoriteBook);

    @Query("SELECT * FROM favorite_books WHERE userId = :userId AND bookTitle = :bookTitle")
    FavoriteBook isBookInFavorites(String userId, String bookTitle);

    @Query("SELECT * FROM favorite_books WHERE userId = :userId")
    List<FavoriteBook> getFavoriteBooks(String userId);


}
