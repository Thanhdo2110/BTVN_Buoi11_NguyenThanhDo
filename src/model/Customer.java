package model;

public class Customer {
        private int id;
        private String customerName;
        private String email;
        private String phone;
        private String address;

        public Customer() {}

        public Customer(int id, String customerName, String email, String phone, String address) {
            this.id = id;
            this.customerName = customerName;
            this.email = email;
            this.phone = phone;
            this.address = address;
        }

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getCustomerName() { return customerName; }
        public void setCustomerName(String customerName) { this.customerName = customerName; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
    }

