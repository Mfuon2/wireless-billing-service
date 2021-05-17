package com.softel.mpesa.service.files

import java.nio.file.Path
import java.util.stream.Stream

import org.springframework.core.io.Resource
import org.springframework.web.multipart.MultipartFile

interface IFileStorage {

  fun initialize()
  fun save(file: MultipartFile)
  fun load( filename: String): Resource
  fun deleteAll()
  // fun loadAll(): Stream<Path>
}