# equip9demo
This creates HTTP service to check contents in AWS S3 bucket

Code Explanation:
Key Components
S3BucketController:
A Spring Boot REST controller that exposes the /list-bucket-content endpoint.
Uses AWS SDK to interact with the S3 bucket.

AWS SDK Integration:
Configures AmazonS3 client using BasicAWSCredentials.
Retrieves bucket content using ListObjectsV2Request.

Response Building:
Combines S3 objects (ObjectSummaries) and simulated directories (CommonPrefixes) into a JSON response.
