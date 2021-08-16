package com.diekeditora.infra.file

import com.diekeditora.domain.file.FileKind
import com.diekeditora.domain.image.FileService
import com.diekeditora.shared.dataLoader
import com.expediagroup.graphql.server.execution.KotlinDataLoader
import org.springframework.stereotype.Component

@Component
internal class FileLinkLoader(val fileService: FileService) :
    KotlinDataLoader<FileKind, String> by dataLoader("FileLinkLoader")
        .execute({
            fileService.getLink(it.generateKey())
        })
