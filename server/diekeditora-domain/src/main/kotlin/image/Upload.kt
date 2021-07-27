package com.diekeditora.domain.image

import org.springframework.http.codec.multipart.FilePart

class Upload(val part: FilePart) : FilePart by part
