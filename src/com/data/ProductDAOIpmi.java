package com.data;

import model.Product;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOIpmi {
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
            CallableStatement callst = conn.prepareCall("CALL find_all_product()");

            ResultSet rs = callst.executeQuery();
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
        if (products.isEmpty()) {
            System.out.println("Không có sản phẩm nào.");
            return;
        }

        System.out.println("---------------------------------");
        System.out.println("|  Id  |  Product Name  |  Price  | Brand | Stock |");
        System.out.println("---------------------------------");
        products.forEach(product -> {
            StringBuilder row = new StringBuilder();
            row.append("|  " + product.getId());
            row.append("|  " + product.getProductName());
            row.append("|  " + product.getPrice());
            row.append("|  " + product.getBrand());
            row.append("|  " + product.getStock() + "   |");

            System.out.println(row);
        });
        System.out.println("---------------------------------");
    }

    public int delete(int id) {
        Connection conn = null;
        int countAffect = 0;
        try {
            conn = ConnectionDB.openConn();
            CallableStatement callSt = conn.prepareCall("{CALL delete_product(?)}");
            callSt.setInt(1, id);

            countAffect = callSt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi xóa dữ liệu!");
        }
        return countAffect;
    }

    public int insert(Product product) {
        Connection conn = null;
        int countAffect = 0;
        try {
            conn = ConnectionDB.openConn();
            CallableStatement callSt = conn.prepareCall("{CALL insert_product(?, ?, ?, ?)}");

            callSt.setString(1, product.getProductName());
            callSt.setInt(2, product.getPrice());
            callSt.setString(3, product.getBrand());
            callSt.setInt(4, product.getStock());

            countAffect = callSt.executeUpdate();
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
            CallableStatement callSt = conn.prepareCall("{CALL update_product(?, ?, ?, ?, ?)}");

            callSt.setInt(1, product.getId());
            callSt.setString(2, product.getProductName());
            callSt.setInt(3, product.getPrice());
            callSt.setString(4, product.getBrand());
            callSt.setInt(5, product.getStock());

            countAffect = callSt.executeUpdate();
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

    public List<Product> findByBrand(String brandKeyword) {
        Connection conn = null;
        List<Product> products = new ArrayList<>();
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM product WHERE brand LIKE '%" + brandKeyword + "%'");
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
            System.out.println("Lỗi tìm kiếm theo brand!");
        }
        return products;
    }

    public List<Product> findByPriceRange(int minPrice, int maxPrice) {
        Connection conn = null;
        List<Product> products = new ArrayList<>();
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM product WHERE price BETWEEN " + minPrice + " AND " + maxPrice);
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
            System.out.println("Lỗi tìm kiếm theo khoảng giá!");
        }
        return products;
    }

    public List<Product> findByStock(int stockThreshold) {
        Connection conn = null;
        List<Product> products = new ArrayList<>();
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM product WHERE stock >= " + stockThreshold);
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
            System.out.println("Lỗi tìm kiếm theo tồn kho!");
        }
        return products;
    }
}
