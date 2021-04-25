package com.softel.mpesa.config

// import org.springframework.context.annotation.Configuration
// import org.springframework.data.web.PageableHandlerMethodArgumentResolver
// import org.springframework.web.method.support.HandlerMethodArgumentResolver
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapte
 
import java.util.List

import org.springframework.context.annotation.Configuration
import org.springframework.data.web.config.EnableSpringDataWebSupport

@Configuration
@EnableSpringDataWebSupport
class PaginationConfig {

    //private static final int PMP_MAX_PAGE_SIZE = 10000;
    // val PMP_MAX_PAGE_SIZE: Integer =  10000
 
    // override fun addArgumentResolvers( argumentResolvers: List<HandlerMethodArgumentResolver>) {
    //     resolver: PageableHandlerMethodArgumentResolver =  PageableHandlerMethodArgumentResolver();
 
    //     resolver.setMaxPageSize(PMP_MAX_PAGE_SIZE);
    //     argumentResolvers.add(resolver);
    //     super.addArgumentResolvers(argumentResolvers);

}