package repository.sale;

import model.Sale;

import java.sql.ResultSet;
import java.util.List;

public interface SaleRepository {
    boolean buyBook(Long bookId, Long customerId, Long employeeId);
    List<Sale> findAll();
    List<Sale> findAllForEmployee(Long id);
}
