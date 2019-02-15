package com.itheima.dao;


import pojo.Book;

import java.util.List;

public interface IBookDao {

    List<Book> findAll();
}
