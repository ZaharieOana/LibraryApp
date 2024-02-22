package service.book;

import model.Book;
import repository.book.BookRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AudioBookServiceImpl implements BookService{
    private final BookRepository bookRepository;

    public AudioBookServiceImpl(BookRepository bookRepository){
        this.bookRepository = bookRepository;
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

    @Override
    public void updateAmount(Long id, int newAmount) {

    }

    @Override
    public void buyBook(Book book, Long customerId, Long employeeId) {

    }

    @Override
    public Long getBookId(Book book) {
        return null;
    }
}
