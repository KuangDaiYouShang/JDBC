package com.bk.dao.impl;

//import Handler.MySQLTemplate;
//import Handler.TemplateHandler;
import com.bk.entity.BookEntity;
import com.bk.dao.bookDao;
import com.ruanmou.vip.orm.constant.SearchMode;
import com.ruanmou.vip.orm.core.handler.HandlerTemplate;
import com.ruanmou.vip.orm.core.handler.mysql.MySQLTemplateHandler;
//import enums.SearchMode;

import java.util.List;

public class BookDaoImpl implements bookDao {

    //private TemplateHandler template = new MySQLTemplate();
    private HandlerTemplate template = new MySQLTemplateHandler();

    @Override
    public void addBook(BookEntity book) {
        template.save(book);
    }

    @Override
    public void deleteBook(Integer id) {
        BookEntity b = new BookEntity();
        b.setBookId(id);
        template.delete(b);
    }

    @Override
    public BookEntity getBookById(Integer id) {
        BookEntity b = new BookEntity();
        b.setBookId(id);
        List<BookEntity> t = template.queryForList(BookEntity.class, b, SearchMode.EQ);
        return t.get(0);
    }

    @Override
    public void updateBook(BookEntity book) {
        template.update(book);
    }

    @Override
    public List<BookEntity> getBooks() {
        return template.queryForList(BookEntity.class);
    }
}
