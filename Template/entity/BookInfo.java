package entity;

import java.io.Serializable;

public class BookInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int bookID;
	private String author;
	private double price;
	
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
