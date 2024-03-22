package com.example.demo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void testSavePerson() {
        Person person = new Person("John Doe", 30, null, null);
        personRepository.save(person);
        // Add assertions here to verify the save operation
    }
}