package model;

public class InvoiceDetail {
        private int id;
        private int invoiceId;
        private String productName;
        private int quantity;
        private double unitPrice;
        private double amount;

        public InvoiceDetail() {}

        public InvoiceDetail(int id, int invoiceId, String productName, int quantity, double unitPrice, double amount) {
            this.id = id;
            this.invoiceId = invoiceId;
            this.productName = productName;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.amount = amount;
        }
        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getInvoiceId() { return invoiceId; }
        public void setInvoiceId(int invoiceId) { this.invoiceId = invoiceId; }

        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getUnitPrice() { return unitPrice; }
        public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

        public double getAmount() { return amount; }
        public void setAmount(double amount) { this.amount = amount; }
    }

