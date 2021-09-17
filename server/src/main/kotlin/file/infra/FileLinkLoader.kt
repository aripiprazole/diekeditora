package com.diekeditora.file.infra

import com.diekeditora.file.domain.FileKind
import com.diekeditora.file.domain.FileService
import com.diekeditora.utils.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
internal class FileLinkLoader(val fileService: FileService) :
    KotlinDataLoader<FileKind, String> by dataLoader("FileLinkLoader")
        .execute({
            fileService.getLink(it.generateKey())
        })
