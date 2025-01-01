package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;

@RestController
@RequestMapping("/list-bucket-content")
class S3BucketController {

    private final AmazonS3 s3Client;
    private final String bucketName = "equip9project"; 

    public S3BucketController() {
        // Configure your AWS credentials
        String accessKey = "AKIA3FLDYDWCMYFJK5VU"; 
        String secretKey = "L+JPpcL5phsKcu6QqsyhpKySdedU/qIpM7RzSW8z"; 
        String region = "us-east-1";       

        BasicAWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                .build();
    }

    @GetMapping(value = {"", "/{path}"})
    public String listBucketContent(@PathVariable(required = false) String path) {
        List<String> content = new ArrayList<>();

        String prefix = (path != null) ? path + "/" : "";

        ListObjectsV2Request request = new ListObjectsV2Request()
                .withBucketName(bucketName)
                .withPrefix(prefix)
                .withDelimiter("/");

        ListObjectsV2Result result = s3Client.listObjectsV2(request);

        // Add directories (common prefixes)
        if (result.getCommonPrefixes() != null) {
            for (String commonPrefix : result.getCommonPrefixes()) {
                content.add(commonPrefix.replaceFirst(prefix, "").replaceAll("/$", ""));
            }
        }

        // Add files (object summaries)
        for (S3ObjectSummary objectSummary : result.getObjectSummaries()) {
            String key = objectSummary.getKey();

            // Only include objects directly under the prefix (exclude nested objects)
            if (!key.equals(prefix)) {
                content.add(key.substring(prefix.length()));
            }
        }

        return "{\"content\": " + content.toString() + "}";
    }
}
