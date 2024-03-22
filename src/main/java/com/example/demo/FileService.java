package com.example.demo;

import com.mongodb.client.gridfs.GridFSBuckets;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public String addFile(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            ObjectId fileId = GridFSBuckets.create(mongoTemplate.getDb()).uploadFromStream(
                    file.getOriginalFilename(), inputStream);
            return fileId.toHexString();
        }
    }

    public String updateFile(String id, MultipartFile file) throws IOException {
        // Check if the file exists before trying to delete it
        if (GridFSBuckets.create(mongoTemplate.getDb()).find(new Document("_id", new ObjectId(id))).iterator().hasNext()) {
            GridFSBuckets.create(mongoTemplate.getDb()).delete(new ObjectId(id));
        }

        // Upload the new file
        try (InputStream inputStream = file.getInputStream()) {
            ObjectId fileId = GridFSBuckets.create(mongoTemplate.getDb()).uploadFromStream(
                    file.getOriginalFilename(), inputStream);
            return fileId.toHexString();
        }
    }

    public byte[] getFile(String id) throws IOException {
        try (InputStream inputStream = GridFSBuckets.create(mongoTemplate.getDb()).openDownloadStream(new ObjectId(id))) {
            return inputStream.readAllBytes();
        }
    }
}
