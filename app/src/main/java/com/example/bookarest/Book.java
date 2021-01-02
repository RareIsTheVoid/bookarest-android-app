package com.example.bookarest;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Books")
public class Book {
    @PrimaryKey(autoGenerate = true)
    private int bookId;

    @ColumnInfo(name="Title")
    private String title;

    @ColumnInfo(name="AuthorId")
    private int authorOfBookId;

    @ColumnInfo(name="NumberOfPages")
    private int numberOfPages;

    public Book(String title, int authorOfBookId, int numberOfPages) {
        this.title = title;
        this.authorOfBookId = authorOfBookId;
        this.numberOfPages = numberOfPages;
    }

    public int getBookId() {
        return bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getAuthorOfBookId() {
        return authorOfBookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setAuthorOfBookId(int authorOfBookId) {
        this.authorOfBookId = authorOfBookId;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }
}
