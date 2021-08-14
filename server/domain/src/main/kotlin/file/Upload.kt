package com.diekeditora.domain.file

import org.springframework.http.codec.multipart.FilePart

class Upload(val part: FilePart) : FilePart by part
