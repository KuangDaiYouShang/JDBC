package com.bk.dao;

import com.bk.entity.BookEntity;

import java.util.List;

public interface bookDao {
    void addBook(BookEntity book);
    void deleteBook(Integer id);
    BookEntity getBookById(Integer id);
    void updateBook(BookEntity book);
    List<BookEntity> getBooks();
}
