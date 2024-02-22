package service.sale;

import model.Sale;

import java.sql.ResultSet;
import java.util.List;

public interface SaleService {
    List<Sale> findAll();
    List<Sale> findAllForEmployee(Long id);
}
