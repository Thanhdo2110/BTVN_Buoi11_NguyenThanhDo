package com.data;

import model.Product;
import model.Customer;
import model.Invoice;
import model.InvoiceDetail;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        ProductDAOIpmi productDAO = new ProductDAOIpmi();
        CustomerDAOIpmi customerDAO = new CustomerDAOIpmi();
        InvoiceDAOIpmi invoiceDAO = new InvoiceDAOIpmi();
        showMenu();

        System.out.println("====");
        System.out.println("Nhập chức năng tương ứng:");
        Scanner sc = new Scanner(System.in);

        while (true) {
            try {
                int n = sc.nextInt();
                while (n < 1 || n > 5) {
                    System.out.println("Số chức năng chưa đúng. Vui lòng nhập lại:");
                    n = sc.nextInt();
                }
                switch (n) {
                    case 1:
                        System.out.println("=== Chức năng Quản lý điện thoại ===");
                        handleProductManagement(productDAO, sc);
                        break;
                    case 2:
                        System.out.println("=== Chức năng Quản lý khách hàng ===");
                        handleCustomerManagement(customerDAO, sc);
                        break;
                    case 3:
                        System.out.println("=== Chức năng Quản lý hoá đơn ===");
                        handleInvoiceManagement(invoiceDAO, productDAO, customerDAO, sc);
                        break;
                    case 4:
                        System.out.println("=== Chức năng Quản lý doanh thu ===");
                        handleRevenueManagement(invoiceDAO, sc);
                        break;
                    case 5:
                        System.out.println("Chức năng Đăng xuất");
                        System.out.println("Cảm ơn bạn đã sử dụng chương trình!");
                        return;
                    default:
                        System.out.println("Chức năng không hợp lệ!");
                }
                System.out.println("\nBạn có muốn tiếp tục? (y/n):");
                sc.nextLine(); // consume newline
                String continueChoice = sc.nextLine();
                if (!continueChoice.equalsIgnoreCase("y") && !continueChoice.equalsIgnoreCase("yes")) {
                    break;
                }
                showMenu();
                System.out.println("Nhập chức năng tương ứng:");

            } catch (Exception e) {
                System.out.println("Lỗi nhập liệu! Vui lòng nhập số nguyên hợp lệ.");
                sc.nextLine(); // clear invalid input
            }
        }
    }
    private static void handleInvoiceManagement(InvoiceDAOIpmi invoiceDAO, ProductDAOIpmi productDAO, CustomerDAOIpmi customerDAO, Scanner sc) {
        while (true) {
            showMenuInvoice();
            System.out.println("Nhập chức năng tương ứng:");
            try {
                int numChucNang = sc.nextInt();
                while (numChucNang < 1 || numChucNang > 6) {
                    System.out.println("Số chức năng chưa đúng. Vui lòng nhập lại:");
                    numChucNang = sc.nextInt();
                }
                switch (numChucNang) {
                    case 1:
                        List<Invoice> invoices = invoiceDAO.getAllInvoices();
                        invoiceDAO.showInvoices(invoices);
                        break;
                    case 2:
                        createNewInvoice(invoiceDAO, productDAO, customerDAO, sc);
                        break;
                    case 3:
                        viewInvoiceDetail(invoiceDAO, sc);
                        break;
                    case 4:
                        searchInvoiceByCustomer(invoiceDAO, sc);
                        break;
                    case 5:
                        searchInvoiceByDate(invoiceDAO, sc);
                        break;
                    case 6:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi nhập liệu! Vui lòng nhập số nguyên hợp lệ.");
                sc.nextLine();
            }
        }
    }
    private static void handleRevenueManagement(InvoiceDAOIpmi invoiceDAO, Scanner sc) {
        while (true) {
            showMenuRevenue();
            System.out.println("Nhập chức năng tương ứng:");
            try {
                int numChucNang = sc.nextInt();
                while (numChucNang < 1 || numChucNang > 4) {
                    System.out.println("Số chức năng chưa đúng. Vui lòng nhập lại:");
                    numChucNang = sc.nextInt();
                }
                switch (numChucNang) {
                    case 1:
                        invoiceDAO.showRevenueByDay();
                        break;
                    case 2:
                        invoiceDAO.showRevenueByMonth();
                        break;
                    case 3:
                        invoiceDAO.showRevenueByYear();
                        break;
                    case 4:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi nhập liệu! Vui lòng nhập số nguyên hợp lệ.");
                sc.nextLine();
            }
        }
    }
    private static void createNewInvoice(InvoiceDAOIpmi invoiceDAO, ProductDAOIpmi productDAO, CustomerDAOIpmi customerDAO, Scanner sc) {
        System.out.println("=== Tạo hóa đơn mới ===");
        try {
            List<Customer> customers = customerDAO.getListCustomer();
            if (customers.isEmpty()) {
                System.out.println("Không có khách hàng nào trong hệ thống!");
                return;
            }
            System.out.println("Danh sách khách hàng:");
            customerDAO.show(customers);

            System.out.print("Nhập ID khách hàng: ");
            int customerId = sc.nextInt();

            Customer customer = customerDAO.findById(customerId);
            if (customer == null) {
                System.out.println("Không tìm thấy khách hàng với ID: " + customerId);
                return;
            }
            Invoice invoice = new Invoice();
            invoice.setCustomerId(customerId);
            invoice.setCustomerName(customer.getCustomerName());
            invoice.setInvoiceDate(new Date(System.currentTimeMillis()));

            List<InvoiceDetail> details = new ArrayList<>();
            double totalAmount = 0;
            System.out.println("Nhập chi tiết hóa đơn (nhập 0 để kết thúc):");

            while (true) {
                List<Product> products = productDAO.getListProduct();
                if (products.isEmpty()) {
                    System.out.println("Không có sản phẩm nào trong hệ thống!");
                    break;
                }
                productDAO.show(products);

                System.out.print("Nhập ID sản phẩm (0 để kết thúc): ");
                int productId = sc.nextInt();

                if (productId == 0) break;

                Product product = productDAO.findById(productId);
                if (product == null) {
                    System.out.println("Không tìm thấy sản phẩm với ID: " + productId);
                    continue;
                }
                System.out.print("Nhập số lượng: ");
                int quantity = sc.nextInt();

                if (quantity <= 0) {
                    System.out.println("Số lượng phải lớn hơn 0!");
                    continue;
                }
                if (quantity > product.getStock()) {
                    System.out.println("Số lượng không đủ! Tồn kho: " + product.getStock());
                    continue;
                }
                double amount = product.getPrice() * quantity;
                totalAmount += amount;

                InvoiceDetail detail = new InvoiceDetail();
                detail.setProductName(product.getProductName());
                detail.setQuantity(quantity);
                detail.setUnitPrice(product.getPrice());
                detail.setAmount(amount);

                details.add(detail);

                System.out.println("Đã thêm: " + product.getProductName() + " x " + quantity + " = " + amount);
            }
            if (details.isEmpty()) {
                System.out.println("Không có sản phẩm nào được thêm vào hóa đơn!");
                return;
            }
            invoice.setDetails(details);
            invoice.setTotalAmount(totalAmount);

            int invoiceId = invoiceDAO.insert(invoice);

            if (invoiceId > 0) {
                System.out.println("Tạo hóa đơn thành công! ID: " + invoiceId);
                System.out.println("Tổng tiền: " + totalAmount + " VND");
            } else {
                System.out.println("Tạo hóa đơn thất bại!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi tạo hóa đơn!");
            e.printStackTrace();
            sc.nextLine();
        }
    }
    private static void viewInvoiceDetail(InvoiceDAOIpmi invoiceDAO, Scanner sc) {
        System.out.println("=== Xem chi tiết hóa đơn ===");
        try {
            System.out.print("Nhập ID hóa đơn: ");
            int invoiceId = sc.nextInt();
            invoiceDAO.showInvoiceDetail(invoiceId);

        } catch (Exception e) {
            System.out.println("Lỗi xem chi tiết hóa đơn!");
            sc.nextLine();
        }
    }
    private static void searchInvoiceByCustomer(InvoiceDAOIpmi invoiceDAO, Scanner sc) {
        System.out.println("=== Tìm kiếm hóa đơn theo tên khách hàng ===");
        try {
            sc.nextLine(); // consume newline
            System.out.print("Nhập tên khách hàng: ");
            String customerName = sc.nextLine();

            List<Invoice> invoices = invoiceDAO.searchByCustomerName(customerName);

            if (invoices.isEmpty()) {
                System.out.println("Không tìm thấy hóa đơn nào cho khách hàng: " + customerName);
            } else {
                System.out.println("Kết quả tìm kiếm:");
                invoiceDAO.showInvoices(invoices);
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm kiếm hóa đơn!");
            sc.nextLine();
        }
    }
    private static void searchInvoiceByDate(InvoiceDAOIpmi invoiceDAO, Scanner sc) {
        System.out.println("=== Tìm kiếm hóa đơn theo ngày ===");
        try {
            sc.nextLine();
            System.out.print("Nhập ngày (dd/mm/yyyy hoặc một phần): ");
            String date = sc.nextLine();
            List<Invoice> invoices = invoiceDAO.searchByDate(date);
            if (invoices.isEmpty()) {
                System.out.println("Không tìm thấy hóa đơn nào cho ngày: " + date);
            } else {
                System.out.println("Kết quả tìm kiếm:");
                invoiceDAO.showInvoices(invoices);
            }
        } catch (Exception e) {
            System.out.println("Lỗi tìm kiếm hóa đơn!");
            sc.nextLine();
        }
    }
    private static void handleCustomerManagement(CustomerDAOIpmi customerDAO, Scanner sc) {
        while (true) {
            showMenuCustomer();
            System.out.println("Nhập chức năng tương ứng:");

            try {
                int numChucNang = sc.nextInt();

                while (numChucNang < 1 || numChucNang > 5) {
                    System.out.println("Số chức năng chưa đúng. Vui lòng nhập lại:");
                    numChucNang = sc.nextInt();
                }
                switch (numChucNang) {
                    case 1:
                        List<Customer> customers = customerDAO.getListCustomer();
                        customerDAO.show(customers);
                        break;
                    case 2:
                        addNewCustomer(customerDAO, sc);
                        break;
                    case 3:
                        updateCustomer(customerDAO, sc);
                        break;
                    case 4:
                        deleteCustomerById(customerDAO, sc);
                        break;
                    case 5:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi nhập liệu! Vui lòng nhập số nguyên hợp lệ.");
                sc.nextLine();
            }
        }
    }

    private static void addNewCustomer(CustomerDAOIpmi customerDAO, Scanner sc) {
        System.out.println("=== Thêm mới khách hàng ===");
        try {
            sc.nextLine();
            System.out.print("Nhập mã khách hàng: ");
            String code = sc.nextLine();
            System.out.print("Nhập họ tên: ");
            String name = sc.nextLine();
            System.out.print("Nhập điện thoại: ");
            String phone = sc.nextLine();
            System.out.print("Nhập địa chỉ: ");
            String address = sc.nextLine();
            Customer c = new Customer(0, code, name, phone, address);
            int num = customerDAO.insert(c);
            System.out.println(num > 0 ? "Thêm thành công!" : "Thêm thất bại!");
        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu!");
            sc.nextLine();
        }
    }
    private static void updateCustomer(CustomerDAOIpmi customerDAO, Scanner sc) {
        System.out.println("=== Cập nhật khách hàng ===");
        try {
            System.out.print("Nhập id cần cập nhật: ");
            int id = sc.nextInt();
            Customer ex = customerDAO.findById(id);
            if (ex == null) {
                System.out.println("Không tìm thấy id");
                return;
            }
            sc.nextLine();
            System.out.print("Mã mới: ");
            String code = sc.nextLine();
            System.out.print("Tên mới: ");
            String name = sc.nextLine();
            System.out.print("Phone mới: ");
            String phone = sc.nextLine();
            System.out.print("Address mới: ");
            String address = sc.nextLine();
            Customer up = new Customer(id, code, name, phone, address);
            int num = customerDAO.update(up);
            System.out.println(num > 0 ? "Cập nhật thành công!" : "Cập nhật thất bại!");
        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu!");
            sc.nextLine();
        }
    }
    private static void deleteCustomerById(CustomerDAOIpmi customerDAO, Scanner sc) {
        System.out.println("=== Xóa khách hàng ===");
        try {
            List<Customer> list = customerDAO.getListCustomer();
            customerDAO.show(list);
            System.out.print("Nhập id cần xóa: ");
            int id = sc.nextInt();
            Customer ex = customerDAO.findById(id);
            if (ex == null) {
                System.out.println("Không tìm thấy id");
                return;
            }
            System.out.print("Xác nhận xóa có hoặc không : ");
            sc.nextLine();
            String cf = sc.nextLine();
            if (cf.equalsIgnoreCase("có")) {
                int num = customerDAO.delete(id);
                System.out.println(num > 0 ? "Xóa thành công" : "Xóa thất bại");
            } else {
                System.out.println("Hủy xóa");
            }
        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu!");
            sc.nextLine();
        }
    }
    private static void handleProductManagement(ProductDAOIpmi productDAO, Scanner sc) {
        while (true) {
            showMenuProduct();
            System.out.println("Nhập chức năng tương ứng:");

            try {
                int numChucNang = sc.nextInt();

                while (numChucNang < 1 || numChucNang > 8) {
                    System.out.println("Số chức năng chưa đúng. Vui lòng nhập lại:");
                    numChucNang = sc.nextInt();
                }

                switch (numChucNang) {
                    case 1:
                        List<Product> products = productDAO.getListProductProcedure();
                        productDAO.show(products);
                        break;
                    case 2:
                        addNewProduct(productDAO, sc);
                        break;
                    case 3:
                        updateProduct(productDAO, sc);
                        break;
                    case 4:
                        deleteProductById(productDAO, sc);
                        break;
                    case 5:
                        searchByBrand(productDAO, sc);
                        break;
                    case 6:
                        searchByPriceRange(productDAO, sc);
                        break;
                    case 7:
                        searchByStock(productDAO, sc);
                        break;
                    case 8:
                        return;
                }
            } catch (Exception e) {
                System.out.println("Lỗi nhập liệu! Vui lòng nhập số nguyên hợp lệ.");
                sc.nextLine();
            }
        }
    }

    private static void addNewProduct(ProductDAOIpmi productDAO, Scanner sc) {
        System.out.println("=== Thêm mới điện thoại ===");

        try {
            System.out.println("Nhập tên sản phẩm:");
            sc.nextLine();
            String productName = sc.nextLine();

            System.out.println("Nhập giá sản phẩm:");
            int price = sc.nextInt();

            System.out.println("Nhập thương hiệu:");
            sc.nextLine();
            String brand = sc.nextLine();

            System.out.println("Nhập số lượng tồn kho:");
            int stock = sc.nextInt();

            Product product = new Product(0, productName, price, brand, stock);
            int numAffect = productDAO.insert(product);

            if (numAffect > 0) {
                System.out.println("Thêm điện thoại thành công!");
            } else {
                System.out.println("Thêm điện thoại thất bại!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập đúng định dạng.");
            sc.nextLine();
        }
    }

    private static void updateProduct(ProductDAOIpmi productDAO, Scanner sc) {
        System.out.println("=== Cập nhật điện thoại ===");

        try {
            System.out.println("Nhập id sản phẩm cần cập nhật:");
            int id = sc.nextInt();

            Product existingProduct = productDAO.findById(id);
            if (existingProduct == null) {
                System.out.println("Sản phẩm với id " + id + " không tồn tại! Vui lòng nhập lại.");
                return;
            }

            System.out.println("Thông tin hiện tại:");
            System.out.println("ID: " + existingProduct.getId());
            System.out.println("Tên: " + existingProduct.getProductName());
            System.out.println("Giá: " + existingProduct.getPrice());
            System.out.println("Thương hiệu: " + existingProduct.getBrand());
            System.out.println("Tồn kho: " + existingProduct.getStock());

            System.out.println("Nhập thông tin mới:");
            System.out.println("Nhập tên sản phẩm mới:");
            sc.nextLine();
            String productName = sc.nextLine();

            System.out.println("Nhập giá sản phẩm mới:");
            int price = sc.nextInt();

            System.out.println("Nhập thương hiệu mới:");
            sc.nextLine();
            String brand = sc.nextLine();

            System.out.println("Nhập số lượng tồn kho mới:");
            int stock = sc.nextInt();

            Product updatedProduct = new Product(id, productName, price, brand, stock);
            int numAffect = productDAO.update(updatedProduct);

            if (numAffect > 0) {
                System.out.println("Cập nhật điện thoại thành công!");
            } else {
                System.out.println("Cập nhật điện thoại thất bại!");
            }
        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập đúng định dạng.");
            sc.nextLine();
        }
    }

    private static void deleteProductById(ProductDAOIpmi productDAO, Scanner sc) {
        List<Product> products = productDAO.getListProduct();
        productDAO.show(products);
        try {
            System.out.println("Nhập id điện thoại cần xoá:");
            int id = sc.nextInt();

            Product existingProduct = productDAO.findById(id);
            if (existingProduct == null) {
                System.out.println("Sản phẩm với id " + id + " không tồn tại!");
                return;
            }

            System.out.println("Thông tin sản phẩm sẽ bị xóa:");
            System.out.println("ID: " + existingProduct.getId());
            System.out.println("Tên: " + existingProduct.getProductName());
            System.out.println("Giá: " + existingProduct.getPrice());
            System.out.println("Thương hiệu: " + existingProduct.getBrand());
            System.out.println("Tồn kho: " + existingProduct.getStock());

            System.out.println("Bạn có chắc chắn muốn xóa? (y/n):");
            sc.nextLine();
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                int numAffect = productDAO.delete(id);
                if (numAffect > 0) {
                    System.out.println("Xoá điện thoại thành công, id = " + id);
                } else {
                    System.out.println("Xoá không thành công");
                }
            } else {
                System.out.println("Hủy bỏ thao tác xóa");
            }
        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập đúng định dạng.");
            sc.nextLine();
        }
    }

    private static void searchByBrand(ProductDAOIpmi productDAO, Scanner sc) {
        System.out.println("=== Tìm kiếm điện thoại theo thương hiệu ===");
        try {
            System.out.println("Nhập từ khóa thương hiệu:");
            sc.nextLine();
            String brandKeyword = sc.nextLine();

            List<Product> products = productDAO.findByBrand(brandKeyword);

            if (products.isEmpty()) {
                System.out.println("Không tìm thấy sản phẩm nào với thương hiệu chứa từ khóa: " + brandKeyword);
            } else {
                System.out.println("Kết quả tìm kiếm cho thương hiệu chứa từ khóa: " + brandKeyword);
                productDAO.show(products);
            }
        } catch (Exception e) {
            System.out.println("Lỗi trong quá trình tìm kiếm!");
            sc.nextLine();
        }
    }
    private static void searchByPriceRange(ProductDAOIpmi productDAO, Scanner sc) {
        System.out.println("=== Tìm kiếm điện thoại theo khoảng giá ===");
        try {
            System.out.println("Nhập giá tối thiểu:");
            int minPrice = sc.nextInt();

            System.out.println("Nhập giá tối đa:");
            int maxPrice = sc.nextInt();

            if (minPrice > maxPrice) {
                System.out.println("Giá tối thiểu không thể lớn hơn giá tối đa!");
                return;
            }
            List<Product> products = productDAO.findByPriceRange(minPrice, maxPrice);

            if (products.isEmpty()) {
                System.out.println("Không tìm thấy sản phẩm nào trong khoảng giá: " + minPrice + " - " + maxPrice);
            } else {
                System.out.println("Kết quả tìm kiếm trong khoảng giá: " + minPrice + " - " + maxPrice);
                productDAO.show(products);
            }
        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập số nguyên hợp lệ.");
            sc.nextLine(); // clear invalid input
        }
    }
    private static void searchByStock(ProductDAOIpmi productDAO, Scanner sc) {
        System.out.println("=== Tìm kiếm điện thoại theo tồn kho ===");
        try {
            System.out.println("Nhập số lượng tồn kho tối thiểu:");
            int stockThreshold = sc.nextInt();

            List<Product> products = productDAO.findByStock(stockThreshold);

            if (products.isEmpty()) {
                System.out.println("Không tìm thấy sản phẩm nào có tồn kho >= " + stockThreshold);
            } else {
                System.out.println("Kết quả tìm kiếm sản phẩm có tồn kho >= " + stockThreshold + ":");
                productDAO.show(products);
            }
        } catch (Exception e) {
            System.out.println("Lỗi nhập liệu! Vui lòng nhập số nguyên hợp lệ.");
            sc.nextLine(); // clear invalid input
        }
    }
    private static void showMenu() {
        System.out.println("==== Chương trình quản lý điện thoại ====");
        System.out.println("1. Quản lý điện thoại");
        System.out.println("2. Quản lý khách hàng");
        System.out.println("3. Quản lý hoá đơn");
        System.out.println("4. Quản lý doanh thu");
        System.out.println("5. Đăng xuất");
    }
    private static void showMenuCustomer() {
        System.out.println("==== Giao diện quản lý khách hàng ====");
        System.out.println("==== Chọn chức năng bên dưới: ====");
        System.out.println("1. Xem danh sách khách hàng");
        System.out.println("2. Thêm mới khách hàng");
        System.out.println("3. Cập nhật khách hàng");
        System.out.println("4. Xóa khách hàng theo id");
        System.out.println("5. Trở về");
    }
    private static void showMenuProduct() {
        System.out.println("==== Giao diện quản lý điện thoại ====");
        System.out.println("==== Chọn chức năng bên dưới: ====");
        System.out.println("1. Xem danh sách điện thoại");
        System.out.println("2. Thêm mới điện thoại");
        System.out.println("3. Cập nhật điện thoại");
        System.out.println("4. Xoá điện thoại theo id");
        System.out.println("5. Tìm kiếm theo thương hiệu");
        System.out.println("6. Tìm kiếm theo khoảng giá");
        System.out.println("7. Tìm kiếm theo tồn kho");
        System.out.println("8. Trở về");
    }
    private static void showMenuRevenue() {
        System.out.println("==== Giao diện quản lý doanh thu ====");
        System.out.println("==== Chọn chức năng bên dưới: ====");
        System.out.println("1. Xem doanh thu theo ngày");
        System.out.println("2. Xem doanh thu theo tháng");
        System.out.println("3. Xem doanh thu theo năm");
        System.out.println("4. Trở về");
    }
    private static void showMenuInvoice() {
        System.out.println("==== Giao diện quản lý hóa đơn ====");
        System.out.println("==== Chọn chức năng bên dưới: ====");
        System.out.println("1. Xem danh sách hóa đơn");
        System.out.println("2. Tạo hóa đơn mới");
        System.out.println("3. Xem chi tiết hóa đơn");
        System.out.println("4. Tìm kiếm hóa đơn theo khách hàng");
        System.out.println("5. Tìm kiếm hóa đơn theo ngày");
        System.out.println("6. Trở về");
    }
}