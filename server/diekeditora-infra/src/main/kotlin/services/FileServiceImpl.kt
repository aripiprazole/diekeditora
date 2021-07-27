package com.diekeditora.infra.services

import com.diekeditora.domain.image.FileKind
import com.diekeditora.domain.image.FileService
import com.diekeditora.infra.props.S3Props
import kotlinx.coroutines.future.await
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.async.AsyncRequestBody
import software.amazon.awssdk.core.async.AsyncResponseTransformer
import software.amazon.awssdk.services.s3.S3AsyncClient
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ObjectCannedACL
import software.amazon.awssdk.services.s3.model.PutObjectRequest

@Service
internal class FileServiceImpl(
    val s3AsyncClient: S3AsyncClient,
    val s3Client: S3Client,
    val props: S3Props,
) : FileService {
    override suspend fun uploadFile(content: ByteArray, kind: FileKind, type: MediaType): String {
        val request = PutObjectRequest.builder()
            .key(kind.generateKey())
            .bucket(props.bucketName)
            .acl(ObjectCannedACL.PUBLIC_READ)
            .contentType(type.toString())
            .build()

        s3AsyncClient
            .putObject(request, AsyncRequestBody.fromBytes(content))
            .await()

        return getLink(kind.generateKey())
    }

    override suspend fun getLink(path: String): String {
        return s3Client
            .utilities()
            .getUrl { it.bucket(props.bucketName).key(path) }
            .toExternalForm()
    }

    override suspend fun getFile(path: String): ByteArray {
        val request = GetObjectRequest.builder()
            .bucket(props.bucketName)
            .build()

        return s3AsyncClient.getObject(request, AsyncResponseTransformer.toBytes())
            .await()
            .asByteArray()
    }
}
