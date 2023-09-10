package java_code.controllers;

import java_code.models.Book;
import java_code.models.Person;
import java_code.services.BookService;
import java_code.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String showBooks(Model model, @RequestParam(value = "page", required = false) Integer page,
                            @RequestParam(value = "books_per_page", required = false) Integer books_per_page,
                            @RequestParam(value = "sort_by_year", required = false) Boolean sort_by_year) {
        if (page == null || books_per_page == null) {
            model.addAttribute("books", bookService.showBooks(sort_by_year));
        } else {
            model.addAttribute("books", bookService.showBooksWithPagination(page, books_per_page, sort_by_year));
        }
        return "books/showBooks";
    }

    @GetMapping("/{id}")
    public String showBook(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person) {
        model.addAttribute("book", bookService.showBook(id));

        Person owner = bookService.getOwnerOfBook(id);

        if (owner != null) {
            model.addAttribute("owner", owner);
        } else {
            model.addAttribute("people", personService.showPeople());
        }
        return "books/showBook";
    }

    @PatchMapping("/{id}/release")
    public String releaseBook(@PathVariable("id") int id) {
        bookService.releaseBook(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assignBook(@PathVariable("id") int id, @ModelAttribute("person") Person person) {
        bookService.assignBook(person, id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/add")
    public String addBookForm(@ModelAttribute("book") Book book) {
        return "books/addBook";
    }

    @PostMapping
    public String addBook(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/addBook";
        }
        bookService.addBook(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/update")
    public String updateBookForm(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.showBook(id));
        return "books/updateBook";
    }

    @PostMapping("/{id}")
    public String updateBook(@PathVariable("id") int id, @ModelAttribute("book") @Valid Book updatedBook, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/updateBook";
        }
        bookService.updateBook(id, updatedBook);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String deleteBook(@PathVariable("id") int id) {
        bookService.deleteBook(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String searchBookForm() {
        return "books/searchBook";
    }

    @PostMapping("/search")
    public String searchBook(Model model, @RequestParam("query") String query) {
        model.addAttribute("books", bookService.findByNameStartingWith(query));
        return "books/searchBook";
    }
}
