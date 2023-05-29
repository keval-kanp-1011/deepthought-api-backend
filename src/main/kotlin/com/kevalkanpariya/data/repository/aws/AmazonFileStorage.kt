package com.kevalkanpariya.data.repository.aws

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File

class AmazonFileStorage: FileStorage {

    private val client: S3Client


    init {
        val region = System.getenv("region")
        val accessKey = System.getenv("access_s3")
        val secretKey = System.getenv("secret_s3")

        val credentials = AwsBasicCredentials.create(accessKey, secretKey)
        val awsRegion = Region.of(region)
        client = S3Client.builder()
            .credentialsProvider(StaticCredentialsProvider.create(credentials))
            .region(awsRegion)
            .build() as S3Client


    }

    override suspend fun saveToAWSBucket(file: File, bucketName: String): String =
        withContext(Dispatchers.IO) {
            client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(file.name).acl(ObjectCannedACL.PUBLIC_READ).build(),
                RequestBody.fromFile(file)
            )
            val request = GetUrlRequest.builder().bucket(bucketName).key(file.name).build()
            client.utilities().getUrl(request).toExternalForm()
        }
}