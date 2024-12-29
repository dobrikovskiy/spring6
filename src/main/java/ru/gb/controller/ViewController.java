package ru.gb.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.gb.model.Person;
import ru.gb.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@Controller
public class ViewController {

    private final PersonRepository personRepository;

    public ViewController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // Отображение главной страницы со списком читателей
    @GetMapping("/")
    public String index(Model model) {
        List<Person> persons = personRepository.findAll(); // Получаем всех читателей
        model.addAttribute("persons", persons); // Добавляем их в модель
        return "index"; // Возвращаем имя шаблона для главной страницы
    }

    // Отображение страницы добавления читателя
    @GetMapping("/add-person")
    public String showAddForm(Model model) {
        model.addAttribute("person", new Person()); // Передаем пустой объект для создания нового читателя
        return "add-person"; // Возвращаем имя шаблона для добавления читателя
    }

    // Отображение страницы редактирования читателя
    @GetMapping("/edit-person/{id}")
    public String editPerson(@PathVariable Long id, Model model) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            model.addAttribute("person", person.get());
            return "edit-person"; // Возвращаем имя шаблона для редактирования
        } else {
            return "redirect:/"; // Если не найден, перенаправляем на главную страницу
        }
    }
}