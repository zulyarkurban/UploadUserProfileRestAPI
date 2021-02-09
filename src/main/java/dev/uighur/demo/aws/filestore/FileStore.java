package dev.uighur.demo.aws.filestore;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

@Service
public class FileStore {

    private final AmazonS3 s3;

    @Autowired
    public FileStore(AmazonS3 s3) {
        this.s3 = s3;
    }

    public void save(String path, String filename, Optional<Map<String, String>> optionalMedataData, InputStream inputStream) {
        ObjectMetadata metaData = new ObjectMetadata();
        optionalMedataData.ifPresent(map -> {
            if (!map.isEmpty()) {
                map.forEach(metaData::addUserMetadata);
            }
        });
        try {
            s3.putObject(path, filename, inputStream, metaData);
        } catch (AmazonServiceException e) {
            throw new IllegalStateException("Filed to store the data to s3", e);
        }
    }
    public byte[] download(String path,String key) {
        try{
            S3Object object= s3.getObject(path,key);
            S3ObjectInputStream inputStream=object.getObjectContent();
           return  IOUtils.toByteArray(inputStream);

        }catch (AmazonServiceException | IOException e){
            throw  new IllegalStateException("Filed to download the file");
        }
    }
}
