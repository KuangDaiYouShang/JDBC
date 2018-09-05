package com.bk.controller;

import com.bk.base.BaseServlet;
import com.bk.dao.bookDao;
import com.bk.dao.impl.BookDaoImpl;
import com.bk.entity.BookEntity;
import com.bk.util.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class BookController extends BaseServlet {

    public String list(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        bookDao bd = new BookDaoImpl();
        List<BookEntity> bookList = bd.getBooks();
        request.setAttribute("books", bookList);

        //request.getRequestDispatcher("/WEB-INF/book/list.jsp").forward(request,response);
        //上一行代码放在BaseServlet中执行，此处只需返回路径。
        return "/WEB-INF/book/list.jsp";
    }

    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String bookId = request.getParameter("bookId");
        System.out.println(bookId);
        bookDao bd = new BookDaoImpl();
        bd.deleteBook(Integer.valueOf(bookId));
        //redirect to the list page
        response.sendRedirect(request.getContextPath()+"/book?param=list");
    }

    public String initialUp(HttpServletRequest request, HttpServletResponse response) {
        String bookId = request.getParameter("bookId");
        bookDao bd = new BookDaoImpl();
        BookEntity b = bd.getBookById(Integer.valueOf(bookId));
        request.setAttribute("book", b);
        //request.getRequestDispatcher("/WEB-INF/book/update.jsp").forward(request,response);

        return "/WEB-INF/book/update.jsp";
    }

    public void update(HttpServletRequest request, HttpServletResponse response, BookEntity b) throws Exception {
/*        String id = request.getParameter("bookId");
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
        b.setBookDate(string2Date(date));*/
        //BookEntity b = BeanUtils.params2Fields(request, BookEntity.class);
        bookDao bd = new BookDaoImpl();
        bd.updateBook(b);
        System.out.println("------" + request.getContextPath()+ "--------");
        response.sendRedirect(request.getContextPath()+"/book?param=list");
    }

    public String initialAdd(HttpServletRequest request, HttpServletResponse response) {
        return "/WEB-INF/book/Add.jsp";
    }

    public void add(HttpServletRequest request, HttpServletResponse response, BookEntity b) throws Exception {
/*        String name = request.getParameter("bookName");
        String author = request.getParameter("bookAuthor");
        String price = request.getParameter("bookPrice");
        String date = request.getParameter("bookDate");
        BookEntity b = new BookEntity();
        b.setBookName(name);
        b.setBookAuthor(author);
        BigDecimal decimal = new BigDecimal(price);
        b.setBookPrice(decimal);
        b.setBookDate(string2Date(date));*/
        //BookEntity b = BeanUtils.params2Fields(request, BookEntity.class);
        bookDao bd = new BookDaoImpl();
        bd.addBook(b);
        response.sendRedirect(request.getContextPath()+"/book?param=list");
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
