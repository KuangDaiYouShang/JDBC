package entity;

import java.io.Serializable;

import Annotations.*;

@Table(value = "t_book")
public class BookInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@PK
	@column(value = "book_id")
	private Integer bookID;
	
	@column(value = "author")
	private String author;
	
	@column(value = "book_price")
	private Double price;
	
	public int getBookID() {
		return bookID;
	}
	public void setBookID(int bookID) {
		this.bookID = bookID;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
}
