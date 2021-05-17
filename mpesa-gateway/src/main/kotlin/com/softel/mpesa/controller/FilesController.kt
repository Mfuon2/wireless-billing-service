package com.softel.mpesa.controller

//import java.util.List
import kotlin.collections.List
import java.util.stream.Collectors

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Parameter

import com.softel.mpesa.dto.FileInfo
import com.softel.mpesa.dto.FileResponseMessage
import com.softel.mpesa.service.files.IFileStorage

@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1", "http://68.183.217.137","http://68.183.217.137:4200" ])
@RequestMapping("/files")
@Tag(name = "Files", description = "File upload and download api")
public class FilesController {

  @Autowired
  lateinit var storageService: IFileStorage

  @PostMapping(value = ["/upload"])
  fun uploadFile(@RequestParam("file") file: MultipartFile): ResponseEntity<FileResponseMessage> {
    var message: String

    try {
      storageService.save(file)

      message = "Uploaded the file successfully: " + file.getOriginalFilename()
      return ResponseEntity.status(HttpStatus.OK).body(FileResponseMessage(message))
      } 
    catch (e: Exception) {
      message = "Could not upload the file: " + file.getOriginalFilename() + "!"
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(FileResponseMessage(message))
      }
      
    }



  @GetMapping("/{filename:.+}")
  @ResponseBody
  fun getFile(@PathVariable  filename: String): ResponseEntity<Resource>  {
    val file: Resource = storageService.load(filename)

    return ResponseEntity
        .ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment filename=\"" + file.getFilename() + "\"")
        .body(file)
    }

  @GetMapping("/initialize")
  fun initialize():String {
    storageService.initialize()
    return "OK"
    }

  // @GetMapping("/list")
  // fun getListFiles(): ResponseEntity<List<FileInfo>> {
  //     val fileInfos: List<FileInfo> = storageService.loadAll().map(path -> {
  //      val filename: String = path.getFileName().toString()
  //      val url: String = MvcUriComponentsBuilder
  //         .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString()
  //     return FileInfo(filename, url)
  //   }).collect(Collectors.toList())

  //   return ResponseEntity.status(HttpStatus.OK).body(fileInfos)
  // }

}