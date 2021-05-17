package com.softel.mpesa.advice

import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.softel.mpesa.dto.FileResponseMessage


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice

class FileUploadExceptionAdvice: ResponseEntityExceptionHandler() {

  @ExceptionHandler(value = [(MaxUploadSizeExceededException::class)])
  fun handleMaxSizeException(exc: MaxUploadSizeExceededException): ResponseEntity<FileResponseMessage> {
    return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(FileResponseMessage("File too large!"))
    }
}