package com.example.bookarest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserBookCrossRefDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUserBookCrossRef(UserBookCrossRef userBookCrossRef);

    @Delete
    public void deleteUserBookCrossRef(UserBookCrossRef userBookCrossRef);

    @Query("SELECT * FROM UserBookCrossRef")
    public List<UserBookCrossRef> getAllUserBookCrossRef();

    @Query("SELECT * FROM UserBookCrossRef WHERE userId=:id")
    public List<UserBookCrossRef> getAllUserBookCrossRefById(int id);

    @Query("SELECT * FROM UserBookCrossRef WHERE userId=:id AND category=:category")
    public List<UserBookCrossRef> getAllUserBookCrossRefByIdAndCategory(int id, int category);

    @Query("SELECT bookId FROM UserBookCrossRef WHERE userId=:id AND category=:category")
    public List<Integer> getAllUserBooksIdRefByIdAndCategory(int id, int category);

    @Query("UPDATE UserBookCrossRef SET progress=:progress WHERE userId=:userId AND bookId=:bookId")
    public void updateProgress(int progress, int userId, int bookId);

    @Query("SELECT progress FROM userbookcrossref WHERE userId=:userId AND bookId=:bookId")
    public int getProgress(int userId, int bookId);

    @Query("SELECT * FROM USERBOOKCROSSREF WHERE category=:category")
    public UserBookCrossRef getAllByCategory(int category);

}
