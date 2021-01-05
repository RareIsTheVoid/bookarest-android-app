package com.example.bookarest;

import androidx.room.Entity;

import java.io.Serializable;

@Entity(tableName = "UserBookCrossRef", primaryKeys = {"userId","bookId"})
public class UserBookCrossRef implements Serializable {
    public int userId;
    public int bookId;
    public int category;
    public int progress;

    public UserBookCrossRef(int userId, int bookId, int category, int progress) {
        this.userId = userId;
        this.bookId = bookId;
        this.category = category;
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "UserBookCrossRef{" +
                "userId=" + userId +
                ", bookId=" + bookId +
                ", category=" + category +
                ", progress=" + progress +
                '}';
    }
}

