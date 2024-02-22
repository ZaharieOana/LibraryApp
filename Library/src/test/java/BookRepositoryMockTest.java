import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.*;
import repository.book.BookRepository;
import repository.book.BookRepositoryMock;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryMockTest {
    private static Book book1;
    private static Book book2;
    private static Book book3;
    private static List<Book> bookList;
    private static BookRepository bookRepository;

    @BeforeAll
    public static void initialization(){
        bookRepository = new BookRepositoryMock();

        book1 = new BookBuilder()
                .setId(1L)
                .setAuthor("Cezar Petrescu")
                .setTitle("Fram Ursul Polar")
                .setPublishedDate(LocalDate.of(1931, 6, 2))
                .build();
        book2 = new BookBuilder()
                .setId(2L)
                .setAuthor("Stephen King")
                .setTitle("IT")
                .setPublishedDate(LocalDate.of(1986, 9, 15))
                .build();
        book3 = new BookBuilder()
                .setId(3L)
                .setAuthor("C. J. Tudor")
                .setTitle("Omul de creta")
                .setPublishedDate(LocalDate.of(2018, 1, 1))
                .build();
        bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
    }

    @BeforeEach
    public void clearList(){
        bookRepository.removeAll();
    }

    @Test
    public void saveAndFindAllTest(){
        assertTrue(bookRepository.save(book1));
        assertTrue(bookRepository.save(book2));
        assertTrue(bookRepository.save(book3));
        assertArrayEquals(bookList.toArray(), bookRepository.findAll().toArray());
    }

    @Test
    public void findByIdTest(){
        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);
        assertEquals(book3, bookRepository.findById(3L).get());
        assertEquals(Optional.empty(), bookRepository.findById(4L));
    }

    @Test
    public void removeAllTest(){
        bookRepository.save(book1);
        bookRepository.removeAll();
        assertTrue(bookRepository.findAll().isEmpty());
    }
}
