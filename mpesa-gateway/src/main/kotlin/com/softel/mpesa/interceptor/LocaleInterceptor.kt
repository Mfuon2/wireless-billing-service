// package com.softel.mpesa.interceptor


// import java.security.Principal
// import org.springframework.web.servlet.handler.HandlerInterceptorAdapter 
// import javax.servlet.http.HttpServletRequest
// import javax.servlet.http.HttpServletResponse

// import com.softel.mpesa.multitenancy.TenantContext;
// import org.springframework.web.servlet.ModelAndView
// import org.springframework.stereotype.Component
// import org.springframework.context.annotation.Profile

// // import org.keycloak.KeycloakPrincipal;
// // import org.keycloak.representations.IDToken;
// import org.slf4j.Logger
// import org.slf4j.LoggerFactory

// import java.util.Map
// import org.springframework.web.servlet.i18n.LocaleChangeInterceptor
// import org.springframework.context.annotation.Bean;


// object LocaleInterceptor {

//     @Bean
//     fun  localeChangeInterceptor(): LocaleChangeInterceptor {
//         var lci: LocaleChangeInterceptor = LocaleChangeInterceptor();
//         lci.setParamName("lang");
//         return lci;
//      }
// }


