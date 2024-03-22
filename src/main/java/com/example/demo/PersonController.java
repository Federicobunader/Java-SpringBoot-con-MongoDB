package com.example.demo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class PersonController {
    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/person")
    public ResponseEntity<Map<String, String>> createPerson(@RequestPart("person") String personJson, @RequestPart("image") MultipartFile image, @RequestPart("document") MultipartFile document) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Person person = mapper.readValue(personJson.toString(), Person.class);
            personService.savePerson(person, image, document);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Person created successfully");
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error occurred while creating person: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/updatePerson/{id}")
    public ResponseEntity<Map<String, String>> updatePerson(@PathVariable String id, @RequestPart("person") String personJson, @RequestPart("image") MultipartFile image, @RequestPart("document") MultipartFile document) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Person person = mapper.readValue(personJson, Person.class);
            person.setId(id);
            personService.updatePerson(person, image, document);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Person updated successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error occurred while updating person: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        try {
            List<Person> persons = personService.getAllPersons();
            return new ResponseEntity<>(persons, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/deletePerson/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        try {
            personService.deletePerson(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}