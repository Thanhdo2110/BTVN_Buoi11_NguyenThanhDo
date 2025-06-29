package com.data;

import model.Product;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

class ProductDAOImpi {
    public List<Product> getListProduct() {
        Connection conn = null;
        List<Product> products = new ArrayList<>();

        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM product");
            while (rs.next()) {
                int id = rs.getInt("id");
                String productName = rs.getString("product_name");
                int price = rs.getInt("price");
                String brand = rs.getString("brand");
                int stock = rs.getInt("stock");

                Product product = new Product(id, productName, price, brand, stock);
                products.add(product);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu!");
        }

        return products;
    }

    public List<Product> getListProductProcedure() {
        Connection conn = null;
        List<Product> products = new ArrayList<>();

        try {
            conn = ConnectionDB.openConn();
            CallableStatement callst = conn.prepareCall("CAll find_all_product");
            //Statement st = conn.createStatement();

            ResultSet rs = callst.executeQuery("SELECT * FROM product");
            while (rs.next()) {
                int id = rs.getInt("id");
                String productName = rs.getString("product_name");
                int price = rs.getInt("price");
                String brand = rs.getString("brand");
                int stock = rs.getInt("stock");

                Product product = new Product(id, productName, price, brand, stock);
                products.add(product);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu!");
        }

        return products;
    }

    public void show(List<Product> products) {
        System.out.println("==== Danh sách sản phẩm ====");

        System.out.println("---------------------------------");
        System.out.println("|  Id  |  Product Name  |  Price  | Brand | Stock |");
        products.forEach(product -> {
            StringBuilder row = new StringBuilder();
            row.append("|  " + product.getId());
            row.append("|  " + product.getProductName());
            row.append("|  " + product.getPrice());
            row.append("|  " + product.getBrand());
            row.append("|  " + product.getStock() + "   |");

            System.out.println(row);
        });
    }

    public int delete (int id) {
        Connection conn = null;
        int countAffect = 0;
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            countAffect = st.executeUpdate("DELETE FROM product WHERE id = " + id);
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu!");
        }
        return countAffect;
    }
    public int insert(Product product) {
        Connection conn = null;
        int countAffect = 0;
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            String sql = "INSERT INTO product (product_name, price, brand, stock) VALUES ('"
                    + product.getProductName() + "', " + product.getPrice() + ", '"
                    + product.getBrand() + "', " + product.getStock() + ")";

            countAffect = st.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Lỗi thêm dữ liệu!");
        }
        return countAffect;
    }

    public int update(Product product) {
        Connection conn = null;
        int countAffect = 0;
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            String sql = "UPDATE product SET product_name = '" + product.getProductName()
                    + "', price = " + product.getPrice() + ", brand = '" + product.getBrand()
                    + "', stock = " + product.getStock() + " WHERE id = " + product.getId();

            countAffect = st.executeUpdate(sql);
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật dữ liệu!");
        }
        return countAffect;
    }

    public Product findById(int id) {
        Connection conn = null;
        Product product = null;
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM product WHERE id = " + id);
            if (rs.next()) {
                String productName = rs.getString("product_name");
                int price = rs.getInt("price");
                String brand = rs.getString("brand");
                int stock = rs.getInt("stock");

                product = new Product(id, productName, price, brand, stock);
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm dữ liệu!");
        }
        return product;
    }
}