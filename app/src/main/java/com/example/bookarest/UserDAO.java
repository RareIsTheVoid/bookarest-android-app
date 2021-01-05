package com.example.bookarest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface UserDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE )
    public void insertUser(User user);

    @Delete
    public void deleteUser(User user);

    @Query("SELECT * FROM users")
    List<User> getAllUsers();

    @Transaction
    @Query("SELECT * FROM users")
    List<UserWithBooks> getUserWithBooks();

    @Query("SELECT * FROM users WHERE email=:email")
    User getUserByEmail(String email);

    @Query("SELECT COUNT(*) FROM users WHERE email=:email AND password=:password")
    int getMatchingUser(String email, String password);

    @Query("SELECT COUNT(*) FROM users WHERE email=:email")
    int checkExistingEmail(String email);

    @Query("SELECT MAX(userId) FROM users")
    int getMaxUserId();
}
