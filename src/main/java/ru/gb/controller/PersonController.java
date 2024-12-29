package ru.gb.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.model.Person;
import ru.gb.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    // Получить всех читателей
    @GetMapping
    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }

    // Получить читателя по ID
    @GetMapping("/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable Long id) {
        Optional<Person> person = personRepository.findById(id);
        return person.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Создать нового читателя
    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {
        Person savedPerson = personRepository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }

    // Обновить существующего читателя
    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable Long id, @RequestBody Person personDetails) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setFirstName(personDetails.getFirstName());
            person.setLastName(personDetails.getLastName());
            person.setAge(personDetails.getAge());
            Person updatedPerson = personRepository.save(person);
            return ResponseEntity.ok(updatedPerson);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Обновить читателя (метод для обработки формы)
    @PostMapping("/update-person")
    public ResponseEntity<Void> updatePersonFromForm(@RequestBody Person person) {
        personRepository.save(person);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); // Возвращаем статус 204 No Content
    }

    // Отображение формы для добавления нового читателя
    @GetMapping("/add-person")
    public ResponseEntity<Person> showAddForm() {
        return ResponseEntity.ok(new Person()); // Возвращаем пустой объект для создания нового читателя
    }

    // Сохранение нового читателя
    @PostMapping("/save-person")
    public ResponseEntity<Person> savePerson(@RequestBody Person person) {
        Person savedPerson = personRepository.save(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPerson);
    }

    // Удаление читателя
    @DeleteMapping("/delete-person/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return ResponseEntity.noContent().build(); // Возвращаем статус 204 No Content
        } else {
            return ResponseEntity.notFound().build(); // Если не найден, возвращаем 404 Not Found
        }
    }
}