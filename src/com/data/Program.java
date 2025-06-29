package com.data;

import model.Product;

import java.util.List;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        ProductDAOImpi productDAO = new ProductDAOImpi();
        showMenu();

        System.out.println("====");
        System.out.println("Nhập chức năng tương ứng:");
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();

        while (n < 1 || n > 5) {
            System.out.println("Số chức năng chưa đúng!. Vui lòng nhập lại:");
            n = sc.nextInt();
        }
        switch (n) {
            case 1:
                System.out.println("=== Chức năng Quản lý điện thoại");
                showMenuProduct();

                int numChucNang = 0;
                sc = new Scanner(System.in);
                numChucNang = sc.nextInt();

                while (numChucNang < 1 || numChucNang > 5) {
                    System.out.println("Số chức năng chưa đúng!. Vui lòng nhập lại:");
                    numChucNang = sc.nextInt();
                }
                if (numChucNang == 1) {

                    List<Product> products = productDAO.getListProductProcedure();
                    productDAO.show(products);
                } else if (numChucNang == 4) {
                    List<Product> products = productDAO.getListProduct();
                    productDAO.show(products);

                    System.out.println("Nhập id điện thoại cần xoá:");
                    int id = 0;
                    sc = new Scanner(System.in);
                    id = sc.nextInt();

                    int numAffect = productDAO.delete(id);
                    if (numAffect > 0) {
                        System.out.println("Xoá điện thoại thành công, id = " + id);
                    } else {
                        System.out.println("Xoá không thành công, id không tồn tại");
                    }
                }
                break;
            case 2:
                System.out.println("Chức năng Quản lý khách hàng");
                break;
            case 3:
                System.out.println("Chức năng Quản lý hoá đơn");
                break;
            case 4:
                System.out.println("Chức năng Quản lý doanh thu");
                break;
            default:
                System.out.println("Chức năng Đăng xuất");
        }
    }
    private static void handleProductManagement(ProductDAOImpi productDAO, Scanner sc) {
        while (true) {
            showMenuProduct();
            int numChucNang = sc.nextInt();

            while (numChucNang < 1 || numChucNang > 5) {
                System.out.println("Số chức năng chưa đúng!. Vui lòng nhập lại:");
                numChucNang = sc.nextInt();
            }

            if (numChucNang == 1) {
                List<Product> products = productDAO.getListProductProcedure();
                productDAO.show(products);
            } else if (numChucNang == 2) {
                addNewProduct(productDAO, sc);
            } else if (numChucNang == 3) {
                updateProduct(productDAO, sc);
            } else if (numChucNang == 4) {
                deleteProductById(productDAO, sc);
            } else if (numChucNang == 5) {
                break;
            }
        }
    }

    private static void addNewProduct(ProductDAOImpi productDAO, Scanner sc) {
        System.out.println("=== Thêm mới điện thoại ===");

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
    }

    private static void updateProduct(ProductDAOImpi productDAO, Scanner sc) {
        System.out.println("=== Cập nhật điện thoại ===");
        System.out.println("Nhập id sản phẩm cần cập nhật:");
        int id = sc.nextInt();

        Product existingProduct = productDAO.findById(id);
        if (existingProduct == null) {
            System.out.println("Sản phẩm với id " + id + " không tồn tại!");
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
    }

    private static void deleteProductById(ProductDAOImpi productDAO, Scanner sc) {
        List<Product> products = productDAO.getListProduct();
        productDAO.show(products);

        System.out.println("Nhập id điện thoại cần xoá:");
        int id = sc.nextInt();

        Product existingProduct = productDAO.findById(id);
        if (existingProduct == null) {
            System.out.println("Sản phẩm với id " + id + " không tồn tại");
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
    }
    private static void showMenu() {
        System.out.println("==== Chương trình quản lý điện thoại ====");
        System.out.println("1. Quản lý điện thoại");
        System.out.println("2. Quản lý khách hàng");
        System.out.println("3. Quản lý hoá đơn");
        System.out.println("4. Quản lý doanh thu");
        System.out.println("5. Đăng xuất");
    }

    private static void showMenuProduct() {
        System.out.println("==== Chọn chức năng bên dưới: ====");
        System.out.println("1. Xem danh sách điện thoại");
        System.out.println("2. Thêm mới điện thoại");
        System.out.println("3. Cập nhật điện thoại");
        System.out.println("4. Xoá điện thoại theo id");
        System.out.println("5. Trở về");
    }
}


