package service.book;

import model.Book;

import java.util.List;

public interface BookService {
    List<Book> findAll();
    Book findById(Long id);
    boolean save(Book book);
    void removeAll();
    int getAgeOfBook(Long id);
    void updateAmount(Long id, int newAmount);
    void buyBook(Book book, Long customerId, Long employeeId);
    Long getBookId(Book book);
}
