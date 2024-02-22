package repository.book;

import model.Book;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookRepositoryCacheDecorator extends BookRepositoryDecorator{
    private Cache<Book> cache;

    public BookRepositoryCacheDecorator(BookRepository bookRepository, Cache<Book> cache){
        super(bookRepository);
        this.cache = cache;
    }
    @Override
    public List<Book> findAll() {
        if(cache.hasResult())
            return cache.load();

        List<Book> books = decoratedRepository.findAll();
        cache.save(books);
        return books;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return decoratedRepository.findById(id);
    }

    @Override
    public boolean save(Book book) {
        cache.invalidateCache();
        return decoratedRepository.save(book);
    }

    @Override
    public void removeAll() {
        cache.invalidateCache();
        decoratedRepository.removeAll();
    }

    @Override
    public void updateAmount(Long id, int newAmount, int oldAmount) {
        cache.invalidateCache();
        decoratedRepository.updateAmount(id,newAmount, oldAmount);
    }

    @Override
    public Long getBookId(Book book) {
        if(cache.hasResult())
            for(Book b : cache.load())
                if(b.getAuthor().equals(book.getAuthor()) && b.getTitle().equals(book.getTitle()) && b.getPublishedDate().equals(book.getPublishedDate()))
                    return b.getId();

        return decoratedRepository.getBookId(book);
    }
}
