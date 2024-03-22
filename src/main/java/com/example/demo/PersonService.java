package com.example.demo;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
    private final FileService fileService;
    private final MongoDBConnection mongoDBConnection;
    private final MongoCollection<Document> personCollection;

    public PersonService(FileService fileService, MongoDBConnection mongoDBConnection) {
        this.fileService = fileService;
        this.mongoDBConnection = mongoDBConnection;
        MongoDatabase database = mongoDBConnection.getDatabase();
        this.personCollection = database.getCollection("person");
    }

    public void savePerson(Person person, MultipartFile image, MultipartFile document) throws IOException {
        String imageId = fileService.addFile(image);
        String documentId = fileService.addFile(document);

        person.setImageId(imageId);
        person.setDocumentId(documentId);

        Document doc = new Document("name", person.getName())
                .append("age", person.getAge())
                .append("imageId", person.getImageId())
                .append("documentId", person.getDocumentId());

        personCollection.insertOne(doc);

    }

    public void updatePerson(Person person, MultipartFile image, MultipartFile document) throws IOException {

        Person existingPerson = getPerson(person.getId());

        String imageId = existingPerson.getImageId();
        String documentId = existingPerson.getDocumentId();

        fileService.updateFile(imageId, image);
        fileService.updateFile(documentId, document);

        Document doc = new Document("name", person.getName())
                .append("age", person.getAge())
                .append("imageId", person.getImageId())
                .append("documentId", person.getDocumentId());

        ObjectId objectId = new ObjectId(person.getId());
        Document query = new Document("_id", objectId);
        personCollection.replaceOne(query, doc);
    }

    public Person getPerson(String id) {
        ObjectId objectId = new ObjectId(id);
        Document query = new Document("_id", objectId);
        Document doc = personCollection.find(query).first();
        if (doc != null) {
            String name = doc.getString("name");
            int age = doc.getInteger("age");
            String imageId = doc.getString("imageId");
            String documentId = doc.getString("documentId");
            return new Person(id, name, age, imageId, documentId);
        }
        return null;
    }

    public List<Person> getAllPersons() {
        FindIterable<Document> docs = personCollection.find();
        List<Person> persons = new ArrayList<>();
        for (Document doc : docs) {
            String name = doc.getString("name");
            int age = doc.getInteger("age");
            String imageId = doc.getString("imageId");
            String documentId = doc.getString("documentId");
            String id = doc.getObjectId("_id").toString();
            persons.add(new Person(id,name, age, imageId, documentId));
        }
        return persons;
    }

    public void deletePerson(String id) {
        ObjectId objectId = new ObjectId(id);
        Document doc = new Document("_id", objectId);
        personCollection.deleteOne(doc);
    }

}