package com.example.bookarest;

public class BookCover {
    private int bookId;
    private String coverFile;

    public BookCover() {
    }

    public BookCover(int bookId, String coverFile) {
        this.bookId = bookId;
        this.coverFile = coverFile;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getCoverFile() {
        return coverFile;
    }

    public void setCoverFile(String coverFile) {
        this.coverFile = coverFile;
    }

    @Override
    public String toString() {
        return "BookCover{" +
                "bookId=" + bookId +
                ", coverFile='" + coverFile + '\'' +
                '}';
    }
}
