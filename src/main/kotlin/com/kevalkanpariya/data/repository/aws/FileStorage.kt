package com.kevalkanpariya.data.repository.aws

import java.io.File

interface FileStorage {

    suspend fun saveToAWSBucket(file: File, bucketName: String): String
}
