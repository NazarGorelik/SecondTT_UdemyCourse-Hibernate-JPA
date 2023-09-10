package java_code.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.List;

@Entity
@Table(name="person")
public class Person {

    @Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="name")
    @NotEmpty(message = "Name cant be empty")
    //@Pattern(regexp = "[A-Z]\\w+ [A-Z]\\w+ [A-Z]\\w+",message="Your name should have this format: Name Second Name Surname")
    private String name;

    @Column(name="birthdate")
    @Min(value=1900, message="Birthdate should be in range from 1900-2023")
    @Max(value=2023, message="Birthdate should be in range from 1900-2023")
    private int birthdate;

    @OneToMany
    private List<Book> bookList;

    public Person() {
    }

    public Person(String name, int birthdate) {
        this.name = name;
        this.birthdate = birthdate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(int birthdate) {
        this.birthdate = birthdate;
    }

    public List<Book> getBookList() {
        return bookList;
    }

    public void setBookList(List<Book> bookList) {
        this.bookList = bookList;
    }
}
