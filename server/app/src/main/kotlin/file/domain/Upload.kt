package file.domain

import org.springframework.http.MediaType
import org.springframework.http.codec.multipart.FilePart

class Upload(val part: FilePart) : FilePart by part {
    val contentType: MediaType
        get() = headers().contentType ?: error("Missing content type in image headers")
}
