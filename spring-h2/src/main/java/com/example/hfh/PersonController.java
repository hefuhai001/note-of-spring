package com.example.hfh;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/add")
    public Person createPerson(@RequestBody Person person) {
        return personRepository.save(person);
    }


    @GetMapping("/list")
    public List<Person> getAllPersons() {
        return personService.getAllPersons();
    }
}
