package service.book;

import model.Book;
import repository.book.BookRepository;
import repository.sale.SaleRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class BookServiceImpl implements BookService{
    private final BookRepository bookRepository;
    private final SaleRepository saleRepository;

    public BookServiceImpl(BookRepository bookRepository, SaleRepository saleRepository){
        this.bookRepository = bookRepository;
        this.saleRepository = saleRepository;
    }

    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book with id: %d not found".formatted(id)));
    }

    @Override
    public boolean save(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void removeAll() {
        bookRepository.removeAll();
    }

    @Override
    public int getAgeOfBook(Long id) {
        Book book = this.findById(id);
        LocalDate now = LocalDate.now();
        return (int) ChronoUnit.YEARS.between(book.getPublishedDate(), now);
    }

    public void updateAmount(Long id, int newAmount){
        Book book = this.findById(id);
        bookRepository.updateAmount(id, newAmount, book.getAmount());
    }

    @Override
    public void buyBook(Book book, Long customerId, Long employeeId) {
        Long id = bookRepository.getBookId(book);
        updateAmount(id, -1);
        saleRepository.buyBook(id, customerId, employeeId);
    }

    @Override
    public Long getBookId(Book book) {
        return bookRepository.getBookId(book);
    }
}
