package com.diekeditora.app.file

import org.springframework.http.MediaType

interface FileService {
    /**
     * Handles file upload
     *
     * @return file link
     */
    suspend fun uploadFile(content: ByteArray, kind: FileKind, type: MediaType): String

    /**
     * Deletes a file by its kind
     *
     * @return file link
     */
    suspend fun deleteFile(kind: FileKind)

    suspend fun getLink(path: String): String

    suspend fun getFile(path: String): ByteArray
}
