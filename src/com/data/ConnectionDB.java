package com.data;

import java.sql.*;

public class ConnectionDB {
    private static final String URL = "jdbc:mysql://localhost:3307/tb_dien_thoai?createDatabaseIfNotExist=true";
    private static final String USERNAME = "root";
    private static final String PASSWORLD = "123456";

    public static Connection openConn() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USERNAME, PASSWORLD);

            return conn;
        } catch (Exception e) {
            System.out.println("Ket noi db loi!");
            e.printStackTrace();
        }
        return null;
    }
}
