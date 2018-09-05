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

//@WebServlet(name = "DeleteServlet", urlPatterns = "/delete")
public class DeleteServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String bookId = request.getParameter("bookId");
        System.out.println(bookId);
        bookDao bd = new BookDaoImpl();
        bd.deleteBook(Integer.valueOf(bookId));
        //redirect to the list page
        response.sendRedirect(request.getContextPath()+"/list");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
