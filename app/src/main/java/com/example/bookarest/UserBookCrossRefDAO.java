package com.example.bookarest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
}
