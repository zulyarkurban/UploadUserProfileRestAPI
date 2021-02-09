package dev.uighur.demo.aws.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/userprofile")
@CrossOrigin("*")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Autowired
    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @GetMapping
    public List<UserProfile> getUserProfile(){
        return userProfileService.getUserProfile();
    };

    @PostMapping(
           path = "{userProfileID}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadUserProfileImage(@PathVariable("userProfileID")UUID userProfileID, @RequestParam("file") MultipartFile file){
        userProfileService.uploadUserProfileImage(userProfileID,file);
    }

    @GetMapping(path = "{userProfileID}/image/download")
    public byte[] downloadUserProfileImage(@PathVariable("userProfileID")UUID userProfileID){
       return userProfileService.downloadUserProfileImage(userProfileID);
    }

}
