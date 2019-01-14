package com.bk.entity;

/*import Annotations.PK;
import Annotations.Table;
import Annotations.column;*/
import com.ruanmou.vip.orm.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@Table("t_book")
public class BookEntity {
    @PK
    @Column("book_id")
    private Integer bookId;

    @Column("author")
    private String bookAuthor;

    @Column("book_name")
    private String bookName;

    @Column("book_price")
    private BigDecimal bookPrice;

    @Column("book_date")
    private Date bookDate;

    public Integer getBookId() {
        return bookId;
    }

    public void setBookId(Integer bookId) {
        this.bookId = bookId;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }


}
