package com.diekeditora.chapter.resources

import com.diekeditora.chapter.domain.Chapter
import com.diekeditora.chapter.domain.ChapterInput
import com.diekeditora.chapter.domain.ChapterService
import com.diekeditora.file.domain.ChapterCoverKind
import com.diekeditora.file.domain.ChapterPageKind
import com.diekeditora.file.domain.FileService
import com.diekeditora.file.domain.Upload
import com.diekeditora.graphql.infra.AuthGraphQLContext
import com.diekeditora.id.domain.UniqueId
import com.diekeditora.id.domain.UniqueIdService
import com.diekeditora.manga.domain.MangaService
import com.diekeditora.security.domain.Authenticated
import com.diekeditora.security.domain.Secured
import com.diekeditora.shared.infra.toByteArray
import com.expediagroup.graphql.generator.annotations.GraphQLDescription
import com.expediagroup.graphql.server.operations.Mutation
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Component

@Component
class ChapterMutation(
    val uidService: UniqueIdService,
    val mangaService: MangaService,
    val chapterService: ChapterService,
    val fileService: FileService,
) : Mutation {
    @Secured
    @PreAuthorize("hasAuthority('chapter.store')")
    @GraphQLDescription("Creates a new chapter with provided data")
    suspend fun createChapter(input: ChapterInput): Chapter? {
        val manga = mangaService.findMangaByUid(input.mangaUid)
            ?: return null

        val uid = uidService.generateUniqueId()
        val mangaId = manga.id ?: error("Manga id must be not null")

        return chapterService.createChapter(manga, input.toChapter(uid, mangaId))
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Updates a chapter by its uid")
    suspend fun updateChapter(
        ctx: AuthGraphQLContext,
        uid: UniqueId,
        input: ChapterInput,
    ): Chapter? {
        val chapter = chapterService.findChapterByUid(uid) ?: return null

        val manga = mangaService.findMangaByUid(chapter.ownerId)
            ?: error("Could not find the manga owner of target chapter")

        val mangaId = manga.id ?: error("Manga id must be not null")

        ctx {
            hasAuthority("chapter.update")

            required {
                user.own(chapter)
                manga.own(chapter)
            }
        }

        return chapterService.updateChapter(chapter, input.toChapter(chapter.uid, mangaId))
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Updates a chapter by its uid")
    suspend fun removeChapterPage(ctx: AuthGraphQLContext, uid: UniqueId, page: Int): Chapter? {
        val chapter = chapterService.findChapterByUid(uid) ?: return null

        val manga = mangaService.findMangaByUid(chapter.ownerId)
            ?: error("Could not find the manga owner of target chapter")

        ctx {
            hasAuthority("chapter.update")

            required {
                user.own(chapter)
                manga.own(chapter)
            }
        }

        fileService.deleteFile(ChapterPageKind(chapter, page))

        return chapter
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Updates a chapter by its uid")
    suspend fun updateChapterPage(
        ctx: AuthGraphQLContext,
        uid: UniqueId,
        page: Int,
        image: Upload,
    ): Chapter? {
        val chapter = chapterService.findChapterByUid(uid) ?: return null

        val manga = mangaService.findMangaByUid(chapter.ownerId)
            ?: error("Could not find the manga owner of target chapter")

        val kind = ChapterPageKind(chapter, page)

        ctx {
            hasAuthority("chapter.update")

            required {
                user.own(chapter)
                manga.own(chapter)
            }
        }

        fileService.uploadFile(image.toByteArray(), kind, image.contentType)

        return chapter
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Updates a chapter by its uid")
    suspend fun updateChapterCover(
        ctx: AuthGraphQLContext,
        uid: UniqueId,
        image: Upload,
    ): Chapter? {
        val chapter = chapterService.findChapterByUid(uid) ?: return null

        val manga = mangaService.findMangaByUid(chapter.ownerId)
            ?: error("Could not find the manga owner of target chapter")

        val kind = ChapterCoverKind(chapter)

        ctx {
            hasAuthority("chapter.update")

            required {
                user.own(chapter)
                manga.own(chapter)
            }
        }

        fileService.uploadFile(image.toByteArray(), kind, image.contentType)

        return chapter
    }

    @Secured
    @Authenticated
    @GraphQLDescription("Deletes a chapter by its uid")
    suspend fun deleteChapter(uid: UniqueId): Chapter {
        val chapter = chapterService.findChapterByUid(uid)
            ?: error("Can not find chapter with the provided uid")

        return chapterService.deleteChapter(chapter)
    }
}
