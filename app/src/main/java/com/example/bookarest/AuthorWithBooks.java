package com.example.bookarest;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AuthorWithBooks {
    @Embedded
    public Author author;

    @Relation(
            parentColumn = "authorId",
            entityColumn = "AuthorId"
    )
    public List<Book> books;

    public AuthorWithBooks(Author author, List<Book> books) {
        this.author = author;
        this.books = books;
    }

    @Override
    public String toString() {
        return "AuthorWithBooks{" +
                "author=" + author +
                ", books=" + books +
                '}';
    }
}
