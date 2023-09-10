package java_code.controllers;

import java_code.models.Book;
import java_code.models.Person;
import java_code.services.PersonService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping
    private String showPeople(Model model) {
        model.addAttribute("people", personService.showPeople());
        return "people/showPeople";
    }

    @GetMapping("/{id}")
    public String showPerson(@PathVariable("id") int id, Model model) {
        Person person = personService.showPerson(id);
        List<Book> books = personService.getBooks(person);
        model.addAttribute("person", person);
        model.addAttribute("books", books);
        return "people/showPerson";
    }

    @GetMapping("/{id}/update")
    public String updatePersonForm(@PathVariable("id") int id, Model model) {
        model.addAttribute("person", personService.showPerson(id));
        return "people/updatePerson";
    }

    @PostMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "people/updatePerson";
        }
        personService.updatePerson(id, person);
        return "redirect:/people";
    }

    @GetMapping("/add")
    public String addPersonForm(@ModelAttribute("person") Person person) {
        return "people/addPerson";
    }

    @PostMapping
    public String addPerson(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "people/addPerson";
        }
        personService.addPerson(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String deletePerson(@PathVariable("id") int id) {
        personService.deletePerson(id);
        return "redirect:/people";
    }
}
