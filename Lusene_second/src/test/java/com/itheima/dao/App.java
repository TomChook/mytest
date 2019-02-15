package com.itheima.dao;

import com.itheima.dao.impl.BookDaoImpl;
import org.junit.Test;
import pojo.Book;

import java.util.List;

public class App {
    @Test
    public void bookTest() {
        IBookDao bookDao = new BookDaoImpl();

        List<Book> bookList = bookDao.findAll();
        for (Book book : bookList) {
            System.out.println(book);
        }
    }
}
