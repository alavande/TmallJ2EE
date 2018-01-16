package org.tmall.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *  数据库工具类：
 *      1：初始化驱动
 *      2：用于获取连接
 */
public class DBUtil {

    // 数据库ip，本地127.0.0.1
    static String ip = "127.0.0.1";
    // 数据库端口，mysql默认3306
    static int port = 3306;
    // 数据库名字
    static String database = "tmall";
    // 编码格式使用UTF-8
    static String encoding = "UTF-8";
    // 用户名
    static String username = "root";
    // 密码
    static String password = "123456";

    // 初始化数据库驱动
    static {
        try{
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    public static Connection getConnection() throws SQLException{
        String url = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&characterEncoding=%s", ip, port, database, encoding);
        Connection conn = DriverManager.getConnection(url, username, password);
        return conn;
    };
}
