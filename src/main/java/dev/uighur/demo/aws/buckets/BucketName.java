package dev.uighur.demo.aws.buckets;

public enum BucketName {

    //write bucket name which you've created in AWS S3
    PROFILE_IMAGE("elasticbeanstalk-us-east-2-331469377214");
    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }
    public String getBucketName() {
        return bucketName;
    }
}
