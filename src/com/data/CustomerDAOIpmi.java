package com.data;

import model.Customer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOIpmi {
    public List<Customer> getListCustomer() {
        Connection conn = null;
        List<Customer> customers = new ArrayList<>();

        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM customer");
            while (rs.next()) {
                int id = rs.getInt("id");
                String customerName = rs.getString("customer_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                Customer customer = new Customer(id, customerName, email, phone, address);
                customers.add(customer);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy dữ liệu khách hàng!");
        }

        return customers;
    }

    public void show(List<Customer> customers) {
        System.out.println("==== Danh sách khách hàng ====");
        if (customers.isEmpty()) {
            System.out.println("Không có khách hàng nào.");
            return;
        }

        System.out.println("--------------------------------------------------");
        System.out.println("| ID | Tên khách hàng | Email | Số điện thoại | Địa chỉ |");
        System.out.println("--------------------------------------------------");
        customers.forEach(customer -> {
            System.out.printf("| %d | %s | %s | %s | %s |%n",
                    customer.getId(),
                    customer.getCustomerName(),
                    customer.getEmail(),
                    customer.getPhone(),
                    customer.getAddress());
        });
        System.out.println("--------------------------------------------------");
    }

    public int insert(Customer customer) {
        Connection conn = null;
        int countAffect = 0;
        try {
            conn = ConnectionDB.openConn();
            CallableStatement callSt = conn.prepareCall("{CALL insert_customer(?, ?, ?, ?)}");

            callSt.setString(1, customer.getCustomerName());
            callSt.setString(2, customer.getEmail());
            callSt.setString(3, customer.getPhone());
            callSt.setString(4, customer.getAddress());

            countAffect = callSt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi thêm khách hàng!");
        }
        return countAffect;
    }

    public int update(Customer customer) {
        Connection conn = null;
        int countAffect = 0;
        try {
            conn = ConnectionDB.openConn();
            CallableStatement callSt = conn.prepareCall("{CALL update_customer(?, ?, ?, ?, ?)}");

            callSt.setInt(1, customer.getId());
            callSt.setString(2, customer.getCustomerName());
            callSt.setString(3, customer.getEmail());
            callSt.setString(4, customer.getPhone());
            callSt.setString(5, customer.getAddress());

            countAffect = callSt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi cập nhật khách hàng!");
        }
        return countAffect;
    }

    public int delete(int id) {
        Connection conn = null;
        int countAffect = 0;
        try {
            conn = ConnectionDB.openConn();
            CallableStatement callSt = conn.prepareCall("{CALL delete_customer(?)}");
            callSt.setInt(1, id);

            countAffect = callSt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Lỗi xóa khách hàng!");
        }
        return countAffect;
    }

    public Customer findById(int id) {
        Connection conn = null;
        Customer customer = null;
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("SELECT * FROM customer WHERE id = " + id);
            if (rs.next()) {
                String customerName = rs.getString("customer_name");
                String email = rs.getString("email");
                String phone = rs.getString("phone");
                String address = rs.getString("address");

                customer = new Customer(id, customerName, email, phone, address);
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm khách hàng!");
        }
        return customer;
    }
}


