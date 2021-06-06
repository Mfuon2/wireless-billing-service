package com.softel.mpesa.aspect

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.stereotype.Component;

import com.softel.mpesa.aspect.annotation.IncrementIt
import com.softel.mpesa.repository.cache.GeneralDashboardRepository
import org.springframework.beans.factory.annotation.Autowired

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;

@Aspect
@Component
@Configurable
class IncrementItAspect {

  @Autowired
  lateinit var generalDashboardRepo: GeneralDashboardRepository

  @After(value = "@annotation(com.softel.mpesa.aspect.annotation.IncrementIt)")
  fun incrementIt(joinPoint: JoinPoint) {

      System.out.println("===========");
     
      val method = joinPoint.target.javaClass.getMethod(joinPoint.signature.name)
      val incrementIt = method.getAnnotation(IncrementIt::class.java)

      System.out.println("IncrementIt");
      System.out.println("\t\tannotation: " + incrementIt)
      System.out.println("\t\tfield: " + incrementIt.field)

      if(incrementIt.field.equals("countClients")){   //paybillBalance
          val oDash = generalDashboardRepo.findById("general")
          return if(oDash.isPresent()){
              var dash = oDash.get()
              dash.countClients = dash.countClients + 1
              val d = generalDashboardRepo.save(dash)
              }
          else{
              System.err.println("DASHBOARD not found ");
              }
          }
      else{
        System.err.println("UNKNOWN field " + incrementIt.field);
        }

      System.out.println("===========");

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