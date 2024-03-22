package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;

@SpringBootTest
public class PersonServiceTest {
    @Autowired
    private PersonService personService;

    @Test
    public void testSavePerson() throws IOException {
        Person person = new Person("John Doe", 30, null, null);
        MockMultipartFile image = new MockMultipartFile("image", "image.jpg", "image/jpeg", "image".getBytes());
        MockMultipartFile document = new MockMultipartFile("document", "document.pdf", "application/pdf", "document".getBytes());
        personService.savePerson(person, image, document);
        // Add assertions here to verify the save operation
    }
}