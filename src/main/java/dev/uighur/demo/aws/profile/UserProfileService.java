package dev.uighur.demo.aws.profile;

import dev.uighur.demo.aws.buckets.BucketName;
import dev.uighur.demo.aws.filestore.FileStore;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.ion.IonException;

import java.io.IOException;
import java.util.*;

@Service
public class UserProfileService {


    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfile() {
        return userProfileDataAccessService.getUserProfile();
    }

    public void uploadUserProfileImage(UUID userProfileID, MultipartFile file) {
        isFile(file);
        isImage(file);
        UserProfile user = getUserProfileOrThrow(userProfileID);


        Map<String, String> metaData = extractMetaData(file);

        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String fileName = String.format("%s-%s" + file.getOriginalFilename(), UUID.randomUUID());
        try {
            fileStore.save(path, fileName, Optional.of(metaData), file.getInputStream());
            user.setUserProfileImageLink(fileName);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private Map<String, String> extractMetaData(MultipartFile file) {
        Map<String, String> metaData = new HashMap<>();
        metaData.put("Content-Type", file.getContentType());
        metaData.put("Content-Length", String.valueOf(file.getSize()));
        return metaData;
    }

    private UserProfile getUserProfileOrThrow(UUID userProfileID) {
        return userProfileDataAccessService.getUserProfile().stream().filter(userProfile -> userProfileID.equals(userProfileID))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("user profile %s not found", userProfileID)));
    }

    private void isImage(MultipartFile file) {
        if (Arrays.asList(ContentType.IMAGE_JPEG.getMimeType(), ContentType.IMAGE_PNG.getMimeType(), ContentType.IMAGE_GIF.getMimeType()).contains(file.getContentType())) {
            throw new IllegalStateException("file must be an image [ "+ file.getContentType()+" ]");
        }
    }

    private void isFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalStateException("cannot upload empty file [" + file.getSize() + " ]");
        }
    }


    public byte[] downloadUserProfileImage(UUID userProfileID) {
       UserProfile user= getUserProfileOrThrow(userProfileID);
        String path = String.format("%s/%s ", BucketName.PROFILE_IMAGE.getBucketName()
        ,user.getUserProfileImageLink());

       return user.getUserProfileImageLink()
               .map(key ->fileStore.download(path,key))
               .orElse(new byte[0]);
    }
}
