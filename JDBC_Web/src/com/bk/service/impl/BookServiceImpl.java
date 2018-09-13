package com.bk.service.impl;

import annotation.MyAutoWired;
import annotation.MyService;
import com.bk.dao.bookDao;
import com.bk.dao.impl.BookDaoImpl;
import com.bk.entity.BookEntity;
import com.bk.service.BookService;

import java.util.List;

@MyService
public class BookServiceImpl implements BookService {

    @MyAutoWired
    private bookDao bookDAO;

    @Override
    public void addBook(BookEntity book) {
        bookDAO.addBook(book);
    }

    @Override
    public void deleteBook(Integer id) {
        bookDAO.deleteBook(id);
    }

    @Override
    public BookEntity getBookById(Integer id) {
        return bookDAO.getBookById(id);
    }

    @Override
    public void updateBook(BookEntity book) {
        bookDAO.updateBook(book);
    }

    @Override
    public List<BookEntity> getBooks() {
       return  bookDAO.getBooks();
    }
}
