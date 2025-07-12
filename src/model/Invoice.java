package model;
import java.sql.*;;
import java.util.ArrayList;
import java.util.List;

public class Invoice {
    private int id;
    private int customerId;
    private String customerName;
    private Date invoiceDate;
    private double totalAmount;
    private List<InvoiceDetail> details;

    public Invoice() {
        this.details = new ArrayList<>();
    }

    public Invoice(int id, int customerId, String customerName, Date invoiceDate, double totalAmount) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.invoiceDate = invoiceDate;
        this.totalAmount = totalAmount;
        this.details = new ArrayList<>();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public Date getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(Date invoiceDate) { this.invoiceDate = invoiceDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public List<InvoiceDetail> getDetails() { return details; }
    public void setDetails(List<InvoiceDetail> details) { this.details = details; }
}
