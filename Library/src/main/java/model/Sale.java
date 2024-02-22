package model;

public class Sale {
    private String customer;
    private String employee;
    private String title;

    public Sale(String employee, String customer, String book) {
        this.customer = customer;
        this.employee = employee;
        this.title = book;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
