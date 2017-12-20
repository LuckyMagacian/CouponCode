package com.lanxi.couponcode.test;

import com.baomidou.mybatisplus.toolkit.IdWorker;
import org.junit.Test;

import java.sql.*;

public class TestSql {
    @Test
    public void test1() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection conn = DriverManager.getConnection("jdbc:mysql://192.168.17.62:3306/couponcode?autoReconnect=true", "yyj", "131056");
        ResultSet rs = conn.prepareStatement("select * from users").executeQuery();
        ResultSetMetaData rsmd = rs.getMetaData();
        int couloumCount = rsmd.getColumnCount();
        int line = 0;
        while (rs.next()) {
            System.out.println(line + ":");
            for (int i = 1; i < couloumCount; i++) {
                System.out.println("	" + rsmd.getColumnName(i) + ":" + rs.getString(i));
            }
        }


        conn.close();
    }

    @Test
    public void test2() {
        System.out.println(IdWorker.get32UUID());
        System.out.println(IdWorker.getId());
    }
}
