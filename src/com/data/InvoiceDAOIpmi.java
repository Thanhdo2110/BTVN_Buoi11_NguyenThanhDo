package com.data;
import model.Invoice;
import model.InvoiceDetail;

import java.sql.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAOIpmi {
    private DecimalFormat df = new DecimalFormat("#,###.##");
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    public int insert(Invoice invoice) {
        Connection conn = null;
        int invoiceId = 0;
        try {
            conn = ConnectionDB.openConn();
            conn.setAutoCommit(false);

            CallableStatement callSt = conn.prepareCall("{CALL insert_invoice(?, ?, ?, ?)}");
            callSt.setInt(1, invoice.getCustomerId());
            callSt.setDate(2, invoice.getInvoiceDate());
            callSt.setDouble(3, invoice.getTotalAmount());
            callSt.registerOutParameter(4, Types.INTEGER);

            callSt.executeUpdate();
            invoiceId = callSt.getInt(4);

            for (InvoiceDetail detail : invoice.getDetails()) {
                CallableStatement detailCallSt = conn.prepareCall("{CALL insert_invoice_detail(?, ?, ?, ?, ?)}");
                detailCallSt.setInt(1, invoiceId);
                detailCallSt.setString(2, detail.getProductName());
                detailCallSt.setInt(3, detail.getQuantity());
                detailCallSt.setDouble(4, detail.getUnitPrice());
                detailCallSt.setDouble(5, detail.getAmount());
                detailCallSt.executeUpdate();
            }

            conn.commit();
        } catch (Exception e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.out.println("Lỗi thêm hóa đơn!");
            e.printStackTrace();
        }
        return invoiceId;
    }

    public List<Invoice> getAllInvoices() {
        Connection conn = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT i.id, i.customer_id, c.customer_name, i.invoice_date, i.total_amount " +
                            "FROM invoices i JOIN customer c ON i.customer_id = c.id " +
                            "ORDER BY i.invoice_date DESC"
            );

            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );
                invoices.add(invoice);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy danh sách hóa đơn!");
        }
        return invoices;
    }

    public Invoice getInvoiceById(int invoiceId) {
        Connection conn = null;
        Invoice invoice = null;
        try {
            conn = ConnectionDB.openConn();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT i.id, i.customer_id, c.customer_name, i.invoice_date, i.total_amount " +
                            "FROM invoices i JOIN customer c ON i.customer_id = c.id WHERE i.id = ?"
            );
            pst.setInt(1, invoiceId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );

                invoice.setDetails(getInvoiceDetails(invoiceId));
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy thông tin hóa đơn!");
        }
        return invoice;
    }

    public List<InvoiceDetail> getInvoiceDetails(int invoiceId) {
        Connection conn = null;
        List<InvoiceDetail> details = new ArrayList<>();
        try {
            conn = ConnectionDB.openConn();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT * FROM invoice_details WHERE invoice_id = ?"
            );
            pst.setInt(1, invoiceId);

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                InvoiceDetail detail = new InvoiceDetail(
                        rs.getInt("id"),
                        rs.getInt("invoice_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("amount")
                );
                details.add(detail);
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy chi tiết hóa đơn!");
        }
        return details;
    }

    public Date getInvoiceDate(int invoiceId) {
        Connection conn = null;
        Date invoiceDate = null;
        try {
            conn = ConnectionDB.openConn();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT invoice_date FROM invoices WHERE id = ?"
            );
            pst.setInt(1, invoiceId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                invoiceDate = rs.getDate("invoice_date");
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy ngày hóa đơn!");
        }
        return invoiceDate;
    }

    public double getTotalAmount(int invoiceId) {
        Connection conn = null;
        double totalAmount = 0;
        try {
            conn = ConnectionDB.openConn();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT total_amount FROM invoices WHERE id = ?"
            );
            pst.setInt(1, invoiceId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                totalAmount = rs.getDouble("total_amount");
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy tổng tiền hóa đơn!");
        }
        return totalAmount;
    }

    public int getCustomerId(int invoiceId) {
        Connection conn = null;
        int customerId = 0;
        try {
            conn = ConnectionDB.openConn();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT customer_id FROM invoices WHERE id = ?"
            );
            pst.setInt(1, invoiceId);

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                customerId = rs.getInt("customer_id");
            }
        } catch (Exception e) {
            System.out.println("Lỗi lấy ID khách hàng!");
        }
        return customerId;
    }

    public void showInvoiceDetail(int invoiceId) {
        Invoice invoice = getInvoiceById(invoiceId);
        if (invoice == null) {
            System.out.println("Không tìm thấy hóa đơn!");
            return;
        }

        System.out.println("===============================================");
        System.out.println("              CHI TIẾT HÓA ĐƠN");
        System.out.printf("ID Hóa đơn: %d%n", invoice.getId());
        System.out.printf("Khách hàng: %s%n", invoice.getCustomerName());
        System.out.printf("Ngày xuất: %s%n", sdf.format(invoice.getInvoiceDate()));
        System.out.println("| STT | Tên sản phẩm | SL | Đơn giá | Thành tiền |");

        int stt = 1;
        for (InvoiceDetail detail : invoice.getDetails()) {
            System.out.printf("| %d | %s | %d | %s | %s |%n",
                    stt++,
                    detail.getProductName(),
                    detail.getQuantity(),
                    df.format(detail.getUnitPrice()),
                    df.format(detail.getAmount())
            );
        }
        System.out.println("-----------------------------------------------");
        System.out.printf("Tổng tiền: %s VND%n", df.format(invoice.getTotalAmount()));
    }

    public void showInvoices(List<Invoice> invoices) {
        System.out.println("==== Danh sách hóa đơn ====");
        if (invoices.isEmpty()) {
            System.out.println("Không có hóa đơn nào.");
            return;
        }

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("| ID | Khách hàng | Ngày xuất | Tổng tiền |");
        for (Invoice invoice : invoices) {
            System.out.printf("| %d | %s | %s | %s VND |%n",
                    invoice.getId(),
                    invoice.getCustomerName(),
                    sdf.format(invoice.getInvoiceDate()),
                    df.format(invoice.getTotalAmount())
            );
        }
    }

    public List<Invoice> searchByCustomerName(String customerName) {
        Connection conn = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            conn = ConnectionDB.openConn();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT i.id, i.customer_id, c.customer_name, i.invoice_date, i.total_amount " +
                            "FROM invoices i JOIN customer c ON i.customer_id = c.id " +
                            "WHERE c.customer_name LIKE ? ORDER BY i.invoice_date DESC"
            );
            pst.setString(1, "%" + customerName + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );
                invoices.add(invoice);
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm kiếm hóa đơn theo tên khách hàng!");
        }
        return invoices;
    }

    public List<Invoice> searchByDate(String date) {
        Connection conn = null;
        List<Invoice> invoices = new ArrayList<>();
        try {
            conn = ConnectionDB.openConn();
            PreparedStatement pst = conn.prepareStatement(
                    "SELECT i.id, i.customer_id, c.customer_name, i.invoice_date, i.total_amount " +
                            "FROM invoices i JOIN customer c ON i.customer_id = c.id " +
                            "WHERE DATE_FORMAT(i.invoice_date, '%d/%m/%Y') LIKE ? ORDER BY i.invoice_date DESC"
            );
            pst.setString(1, "%" + date + "%");

            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                Invoice invoice = new Invoice(
                        rs.getInt("id"),
                        rs.getInt("customer_id"),
                        rs.getString("customer_name"),
                        rs.getDate("invoice_date"),
                        rs.getDouble("total_amount")
                );
                invoices.add(invoice);
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm kiếm hóa đơn theo ngày!");
        }
        return invoices;
    }

    public void showRevenueByDay() {
        Connection conn = null;
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT DATE_FORMAT(invoice_date, '%d/%m/%Y') as day, SUM(total_amount) as revenue " +
                            "FROM invoices GROUP BY DATE(invoice_date) ORDER BY invoice_date DESC"
            );

            System.out.println("==== Thống kê doanh thu theo ngày ====");
            System.out.println("| Ngày | Doanh thu |");

            while (rs.next()) {
                System.out.printf("| %s | %s VND |%n",
                        rs.getString("day"),
                        df.format(rs.getDouble("revenue"))
                );
            }
        } catch (Exception e) {
            System.out.println("Lỗi thống kê doanh thu theo ngày!");
        }
    }

    public void showRevenueByMonth() {
        Connection conn = null;
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT DATE_FORMAT(invoice_date, '%m/%Y') as month, SUM(total_amount) as revenue " +
                            "FROM invoices GROUP BY YEAR(invoice_date), MONTH(invoice_date) ORDER BY YEAR(invoice_date) DESC, MONTH(invoice_date) DESC"
            );

            System.out.println("==== Thống kê doanh thu theo tháng ====");
            System.out.println("| Tháng | Doanh thu |");

            while (rs.next()) {
                System.out.printf("| %s | %s VND |%n",
                        rs.getString("month"),
                        df.format(rs.getDouble("revenue"))
                );
            }
        } catch (Exception e) {
            System.out.println("Lỗi thống kê doanh thu theo tháng!");
        }
    }

    public void showRevenueByYear() {
        Connection conn = null;
        try {
            conn = ConnectionDB.openConn();
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery(
                    "SELECT YEAR(invoice_date) as year, SUM(total_amount) as revenue " +
                            "FROM invoices GROUP BY YEAR(invoice_date) ORDER BY YEAR(invoice_date) DESC"
            );

            System.out.println("==== Thống kê doanh thu theo năm ====");
            System.out.println("| Năm | Doanh thu |");

            while (rs.next()) {
                System.out.printf("| %d | %s VND |%n",
                        rs.getInt("year"),
                        df.format(rs.getDouble("revenue"))
                );
            }
        } catch (Exception e) {
            System.out.println("Lỗi thống kê doanh thu theo năm");
        }
    }
}


