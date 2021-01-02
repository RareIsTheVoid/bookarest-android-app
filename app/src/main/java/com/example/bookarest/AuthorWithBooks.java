package com.example.bookarest;


import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class AuthorWithBooks {
    @Embedded
    public Author author;

    @Relation(
            parentColumn = "authorId",
            entityColumn = "authorOfBookId"
    )
    public List<Book> books;
}
