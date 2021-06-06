package com.softel.mpesa.aspect

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.softel.mpesa.aspect.annotation.SpyIt;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

@Aspect
@Component
class SpyItAspect {


  @Before(value = "@annotation(com.softel.mpesa.aspect.annotation.SpyIt)")
  fun logSignature(joinPoint: JoinPoint) {

      System.out.println("===========");

      // Method Information
      val signature = joinPoint.signature
      System.out.println("full method description: " + signature.toShortString());
      System.out.println("\t\tmethod name: " + signature.name);
      System.out.println("\t\tdeclaring type: " + signature.declaringType);

      // // Method args
      // System.out.println("Method args names:");
      // Arrays.stream(signature.getParameterNames())
      //   .forEach(s -> System.out.println("\t\targ name: " + s));

      // System.out.println("Method args types:");
      // Arrays.stream(signature.getParameterTypes())
      //   .forEach(s -> System.out.println("\t\targ type: " + s));

      // System.out.println("Method args values:");
      // Arrays.stream(joinPoint.getArgs())
      //   .forEach(o -> System.out.println("\t\targ value: " + o.toString()));

      // // Additional Information
      // System.out.println("Returning type: " + signature.getReturnType());
      // System.out.println("Method modifier: " + Modifier.toString(signature.getModifiers()));
      // Arrays.stream(signature.getExceptionTypes())
      //   .forEach(aClass -> System.out.println("\texception type: " + aClass));

      // Method annotation
      // val method: Method = signature.getMethod();
      // val spyIt: SpyIt = method.getAnnotation(SpyIt.class);
      val method = joinPoint.target.javaClass.getMethod(joinPoint.signature.name)
      val spyIt = method.getAnnotation(SpyIt::class.java)

      System.out.println("SpyIt");
      System.out.println("\t\tannotation: " + spyIt);
      System.out.println("\t\tvalue: " + spyIt.operation);
      System.out.println("===========");

      // val method = joinPoint.target.javaClass.getMethod(joinPoint.signature.name)
      // val x = method.getAnnotation(SpyIt::class.java)

      }

  // @Around("@annotation(net.softel.pacis.ussd.aspect.annotation.SpyIt)")
  // public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
  //     long startTime = System.currentTimeMillis();
  //     Object proceed = joinPoint.proceed();
  //     long duration = System.currentTimeMillis() - startTime;
  //     System.out.println("\tExecution took [" + duration + "ms]");
  //     return proceed;
  //     }

  
}