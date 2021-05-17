package com.softel.mpesa.service.files.impl

import java.io.IOException
import java.net.MalformedURLException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.FileSystemUtils
import org.springframework.web.multipart.MultipartFile

import com.softel.mpesa.service.files.IFileStorage

@Service
class FilesStorageService: IFileStorage {

  val root: Path = Paths.get("./vuka/uploads");

  override fun initialize(){
    try {
        Files.createDirectory(root);
        } 
    catch (e:IOException) {
        throw RuntimeException("Could not initialize folder for upload!");
        }
  }

  override fun save(file: MultipartFile) {
    try {
        Files.copy(file.getInputStream(), this.root.resolve(file.getOriginalFilename()));
        }
    catch (e: Exception) {
        throw RuntimeException("Could not store the file. Error: " + e.message);
        }
  }

  override fun  load(filename: String): Resource {
    try {
       val file: Path = root.resolve(filename);
       val resource: Resource  =  UrlResource(file.toUri());

        if (resource.exists() || resource.isReadable()) {
            return resource;
            } 
        else {
            throw RuntimeException("Could not read the file!");
            }
        } 
    catch ( e: MalformedURLException) {
        throw RuntimeException("Error: " + e.message);
        }
  }

  override fun deleteAll() {
    FileSystemUtils.deleteRecursively(root.toFile());
    }

//   @Override
//   public Stream<Path> loadAll() {
//     try {
//       return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
//     } catch (IOException e) {
//       throw new RuntimeException("Could not load the files!");
//     }
//   }

//   override fun loadAll(): Stream<Path> {
//     try {
//       return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
//         } 
//     catch (IOException e) {
//       throw RuntimeException("Could not load the files!");
//         }
//   }

}