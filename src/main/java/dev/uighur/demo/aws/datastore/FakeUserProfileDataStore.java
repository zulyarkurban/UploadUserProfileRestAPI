package dev.uighur.demo.aws.datastore;


import dev.uighur.demo.aws.profile.UserProfile;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class FakeUserProfileDataStore {

    private static List<UserProfile> USER_PROFILES =new ArrayList<>();

    static {
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(),"Ahmet",null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(),"Sali",null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(),"Ali",null));
        USER_PROFILES.add(new UserProfile(UUID.randomUUID(),"Mehmet",null));

    }

    public List<UserProfile> getUserProfiles(){
       return USER_PROFILES;
    }
}
