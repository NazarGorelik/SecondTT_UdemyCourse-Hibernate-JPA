package java_code.services;

import java_code.models.Book;
import java_code.models.Person;
import java_code.repositories.BookRepository;
import java_code.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;
    private final BookRepository bookRepository;

    @Autowired
    public PersonService(PersonRepository personRepository, BookRepository bookRepository) {
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
    }

    public List<Person> showPeople() {
        return personRepository.findAll();
    }

    public Person showPerson(int id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null);
    }

    @Transactional
    public void updatePerson(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        personRepository.save(updatedPerson);
    }

    @Transactional
    public void addPerson(Person person) {
        personRepository.save(person);
    }

    @Transactional
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }


    /////////////////////////
    ///Get Books and check if they are expired
    /////////////////////////

    public List<Book> getBooks(Person person) {
        List<Book> books = bookRepository.findByOwner(person);
        isExpired(books);
        return books;
    }

    public void isExpired(List<Book> bookList) {
        for (Book book : bookList) {
            LocalDate end = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate start = book.getStart_date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            long diff = Math.abs(ChronoUnit.DAYS.between(end, start));
            if (diff > 10) {
                book.setExpired(true);
            }
        }
    }
}
