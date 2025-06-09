package com.example.h2;

import com.example.h2.Person;
import com.example.h2.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    @Autowired
    private PersonRepository personRepository;

    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    public List<Person> getAllPersons() {
        return personRepository.findAll();
    }
}
