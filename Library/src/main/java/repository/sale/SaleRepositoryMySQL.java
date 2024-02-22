package repository.sale;

import model.Sale;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SaleRepositoryMySQL implements SaleRepository {
    protected final Connection connection;
    public SaleRepositoryMySQL(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean buyBook(Long bookId, Long customerId, Long employeeId) {
        String sql = "INSERT INTO sale VALUES(null, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, customerId);
            preparedStatement.setLong(2, employeeId);
            preparedStatement.setLong(3, bookId);

            int rowsInserted = preparedStatement.executeUpdate();
            return (rowsInserted != 1) ? false : true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Sale> findAll() {
        String sql = "Select e.username as employee, c.username as customer, book.title \n" +
                "From sale\n" +
                "Join user as c\n" +
                "\ton c.id = sale.customer_id\n" +
                "Join user as e\n" +
                "\ton e.id = sale.employee_id\n" +
                "Join book \n" +
                "\ton book.id = sale.book_id";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Sale> sales = new ArrayList<>();
            while(resultSet.next()) {
                Sale sale = new Sale(resultSet.getString("employee"), resultSet.getString("customer"), resultSet.getString("title"));
                sales.add(sale);
            }
            return sales;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Sale> findAllForEmployee(Long id) {
        String sql = "Select e.username as employee, c.username as customer, book.title \n" +
                "From sale\n" +
                "Join user as c\n" +
                "\ton c.id = sale.customer_id\n" +
                "Join user as e\n" +
                "\ton e.id = sale.employee_id and e.id = " + id + "\n" +
                "Join book \n" +
                "\ton book.id = sale.book_id";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<Sale> sales = new ArrayList<>();
            while(resultSet.next()) {
                Sale sale = new Sale(resultSet.getString("employee"), resultSet.getString("customer"), resultSet.getString("title"));
                sales.add(sale);
            }
            return sales;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
