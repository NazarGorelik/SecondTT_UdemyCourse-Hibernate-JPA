package java_code.repositories;

import java_code.models.Book;
import java_code.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findByOwner(Person owner);
    List<Book> findByNameStartingWith(String name);
}
