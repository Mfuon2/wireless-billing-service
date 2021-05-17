package com.softel.mpesa.service.files.impl

//import lombok.extern.slf4j.Slf4j

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
import com.softel.mpesa.entity.VoucherUpload
import com.softel.mpesa.repository.VoucherUploadRepository
import org.springframework.beans.factory.annotation.Autowired


import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser

import com.softel.mpesa.service.files.IFileStorage

@Service
//@Slf4j
class FilesStorageService: IFileStorage {

    @Autowired
    lateinit var voucherRepository: VoucherUploadRepository

  val root: Path = Paths.get("uploads");

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




override fun readVouchers(fileName: String){

    println("reading vouchers")

    try {

        val file: Path = root.resolve(fileName);

        val bufferedReader =  Files.newBufferedReader(file);

        // CSVFormat.DEFAULT
        //     .withDelimiter(',')
        //     .withQuote('"')
        //     .withRecordSeparator("\r\n")

        val csvParser = CSVParser(bufferedReader, CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim());

        for (csvRecord in csvParser) {
            val voucherId = csvRecord.get("Voucher ID")
            val portal = csvRecord.get("Portal")
            val plan = csvRecord.get("Policy")
            val creationTime = csvRecord.get("Creation Time")
            val claimedTime = csvRecord.get("Claimed Time")
            val expiryTime = csvRecord.get("Expiry Time")

            //val studentName = csvRecord.get("FirstName);
            //val studentLastName = csvRecord.get("LastName);
            //var studentScore = csvRecord.get("Score);
            //println(Student(studentId, studentName, studentLastName, studentScore))
            
            println("Found VoucherID " + voucherId)

            val tempVoucher = VoucherUpload(
                voucherId = voucherId,
                portal = portal,
                plan = plan,
                creationTime = creationTime,
                claimedTime = claimedTime,
                expiryTime = expiryTime
                )

            voucherRepository.save(tempVoucher)


            }

        } 
    catch (e:IOException) {
        throw RuntimeException("Could not read vouchers " + e.message);
        }
  }
  
}