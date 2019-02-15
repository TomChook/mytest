package com.itheima.dao.impl;

import com.itheima.dao.IBookDao;
import org.apache.commons.beanutils.BeanUtils;
import pojo.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements IBookDao {
    @Override
    public List<Book> findAll() {
        List<Book> bookList = new ArrayList<>();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection("jdbc:mysql:///lucene", "root", "root");
            String sql = "select * from book";
            // 获取statement
            PreparedStatement statement = connection.prepareStatement(sql);
            // 执行, 返回结果集
            ResultSet rs = statement.executeQuery();

            //获取结果集的元数据
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Book book = new Book();

                for (int i = 1; i <= columnCount; i++) {

                    String name = metaData.getColumnName(i);
                    Object value = rs.getObject(name);

                    BeanUtils.setProperty(book, name, value);
                }
                bookList.add(book);
            }

        } catch (Exception e) {
            e.getMessage();
        }
        return bookList;
    }
}
