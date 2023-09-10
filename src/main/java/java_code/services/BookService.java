package java_code.services;

import java_code.models.Book;
import java_code.models.Person;
import java_code.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;

    @Autowired
    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> showBooks(Boolean isSorting) {
        if (isSorting != null && isSorting) {
            return showBooksWithSorting("year");
        }
        return bookRepository.findAll();
    }

    public Book showBook(int id) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        return optionalBook.orElse(null);
    }

    @Transactional
    public void updateBook(int id, Book updatedBook) {
        updatedBook.setId(id);
        bookRepository.save(updatedBook);
    }

    @Transactional
    public void addBook(Book book) {
        bookRepository.save(book);
    }

    @Transactional
    public void deleteBook(int id) {
        bookRepository.deleteById(id);
    }

    public Person getOwnerOfBook(int id) {
        return bookRepository.findById(id).map(book -> book.getOwner()).orElse(null);
    }


    /////////////////////////
    ///Assign/Release Book
    /////////////////////////

    @Transactional
    public void releaseBook(int id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(null);
        });
    }

    @Transactional
    public void assignBook(Person person, int id) {
        bookRepository.findById(id).ifPresent(book -> {
            book.setOwner(person);
            book.setStart_date(new Date());
        });
    }

    public List<Book> findByNameStartingWith(String query) {
        return bookRepository.findByNameStartingWith(query);
    }

    /////////////////////////
    ///Pagination and Sorting
    /////////////////////////

    public List<Book> showBooksWithPagination(int page, int books_per_page, Boolean isSorting) {
        if (isSorting != null && isSorting) {
            showBooksWithPaginationAndSorting(page, books_per_page, "year");
        }
        return bookRepository.findAll(PageRequest.of(page, books_per_page)).getContent();
    }

    public List<Book> showBooksWithSorting(String fieldName) {
        return bookRepository.findAll(Sort.by(fieldName));
    }

    public List<Book> showBooksWithPaginationAndSorting(int page, int books_per_page, String fieldName) {
        return bookRepository.findAll(PageRequest.of(page, books_per_page, Sort.by(fieldName))).getContent();
    }
}
