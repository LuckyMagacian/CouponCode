package com.lanxi.couponcode.test;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import org.junit.Test;

import java.io.*;
import java.sql.*;

public class TestSql {
    @Test
    public void test1() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.17.62:3306/couponcode?autoReconnect=true", "yyj", "131056");
        InputStream fin=TestSql.class.getClassLoader().getResourceAsStream("background.jpg");
        byte[] bytes=new byte[fin.available()];
        fin.read(bytes);
        Blob blob= conn.createBlob();
        blob.setBytes(1,bytes);
        Long id=1001L;
        String sql="insert into merchant_pics values(?,?,?,?)";
        PreparedStatement preparedStatement=conn.prepareStatement(sql);
        preparedStatement.setLong(1,id);
        preparedStatement.setBlob(2,blob);
        preparedStatement.setBlob(3,blob);
        preparedStatement.setBlob(4,blob);
        preparedStatement.execute();
//        ResultSet rs = conn.prepareStatement("select * from merchant_pics").executeQuery();
//        ResultSetMetaData rsmd = rs.getMetaData();
//        int couloumCount = rsmd.getColumnCount();
//        int line = 0;
//        while (rs.next()) {
//            System.out.println(line + ":");
//            for (int i = 1; i < couloumCount; i++) {
//                System.out.println("	" + rsmd.getColumnName(i) + ":" + rs.getString(i));
//            }
//        }
//
//
        conn.close();
    }

    @Test
    public void test2() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.17.62:3306/couponcode?autoReconnect=true", "yyj", "131056");
        String sql="select other_pic from merchant_pics where merchant_id = 1001";
        ResultSet resultSet=conn.prepareStatement(sql).executeQuery();
        resultSet.next();
        Blob blob=resultSet.getBlob(1);
        FileOutputStream fout=new FileOutputStream(new File("blob.jpg"));
        fout.write(blob.getBytes(1, (int)blob.length()));
        conn.close();
    }
}
