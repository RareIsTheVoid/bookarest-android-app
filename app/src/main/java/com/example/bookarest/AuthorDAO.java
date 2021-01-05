package com.example.bookarest;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface AuthorDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAuthor(Author author);

    @Delete
    public void deleteAuthor(Author author);

    @Query("SELECT * FROM authors")
    public List<Author> getAllAuthors();

    @Query("SELECT authors.Name FROM authors WHERE authorId=:id")
    public String getAuthorNameById(int id);

    @Transaction
    @Query("SELECT * From authors")
    public List<AuthorWithBooks> getAuthorsWithBooks();

    @Query("DELETE FROM authors")
    public void deleteAuthors();
}
