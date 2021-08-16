package com.diekeditora.domain.image

import com.diekeditora.domain.file.FileKind
import org.springframework.http.MediaType

interface FileService {
    /**
     * Handles file upload
     *
     * @return file link
     */
    suspend fun uploadFile(content: ByteArray, kind: FileKind, type: MediaType): String

    suspend fun getLink(path: String): String

    suspend fun getFile(path: String): ByteArray
}
