package service.sale;

import model.Sale;
import repository.sale.SaleRepository;

import java.sql.ResultSet;
import java.util.List;

public class SaleServiceImpl implements SaleService{
    private final SaleRepository saleRepository;

    public SaleServiceImpl(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    @Override
    public List<Sale> findAllForEmployee(Long id) {
        return saleRepository.findAllForEmployee(id);
    }
}
