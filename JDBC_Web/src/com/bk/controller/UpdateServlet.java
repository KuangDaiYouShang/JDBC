package com.bk.controller;

import com.bk.dao.bookDao;
import com.bk.dao.impl.BookDaoImpl;
import com.bk.entity.BookEntity;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//@WebServlet(name = "UpdateServlet", urlPatterns = "/update")
public class UpdateServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("bookId");
        String name = request.getParameter("bookName");
        String author = request.getParameter("bookAuthor");
        String price = request.getParameter("bookPrice");
        String date = request.getParameter("bookDate");
        BookEntity b = new BookEntity();
        b.setBookId(Integer.valueOf(id));
        b.setBookName(name);
        b.setBookAuthor(author);
        BigDecimal decimal = new BigDecimal(price);
        b.setBookPrice(decimal);
        b.setBookDate(string2Date(date));
        bookDao bd = new BookDaoImpl();
        bd.updateBook(b);
        response.sendRedirect(request.getContextPath()+"/list");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    private Date string2Date(String s) {
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return sim.parse(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
