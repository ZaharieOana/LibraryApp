import database.JDBConnectionWrapper;
import model.Book;
import model.builder.BookBuilder;
import org.junit.jupiter.api.*;
import repository.book.BookRepository;
import repository.book.BookRepositoryMySQL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class BookRepositoryMySQLTest {
    private static Book book1;
    private static Book book2;
    private static Book book3;
    private static List<Book> bookList;
    private static BookRepository bookRepository;

    @BeforeAll
    public static void initialization(){
        JDBConnectionWrapper connectionWrapper = new JDBConnectionWrapper("test_library");
        bookRepository = new BookRepositoryMySQL(connectionWrapper.getConnection());

        book1 = new BookBuilder()
                .setAuthor("Cezar Petrescu")
                .setTitle("Fram Ursul Polar")
                .setPublishedDate(LocalDate.of(1931, 6, 2))
                .build();
        book2 = new BookBuilder()
                .setAuthor("Stephen King")
                .setTitle("IT")
                .setPublishedDate(LocalDate.of(1986, 9, 15))
                .build();
        book3 = new BookBuilder()
                .setAuthor("C. J. Tudor")
                .setTitle("Omul de creta")
                .setPublishedDate(LocalDate.of(2018, 1, 1))
                .build();
        bookList = new ArrayList<>();
        bookList.add(book1);
        bookList.add(book2);
        bookList.add(book3);
    }

    //inaite de fiecare test curatam tabela din baza de date pentru a putea testa pe rand fiecare functie
    @BeforeEach
    public void clearTable(){
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
        assertEquals(book1, bookRepository.findById(1L).get());
        assertEquals(Optional.empty(), bookRepository.findById(4L));
    }

    @Test
    public void removeAllTest(){
        bookRepository.save(book1);
        bookRepository.removeAll();
        assertTrue(bookRepository.findAll().isEmpty());
    }
}
