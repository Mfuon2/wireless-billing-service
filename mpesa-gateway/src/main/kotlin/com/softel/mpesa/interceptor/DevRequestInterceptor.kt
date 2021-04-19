package com.softel.mpesa.interceptor


import java.security.Principal
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter 
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import com.softel.mpesa.multitenancy.TenantContext;
import org.springframework.web.servlet.ModelAndView
import org.springframework.stereotype.Component
import org.springframework.context.annotation.Profile

// import org.keycloak.KeycloakPrincipal;
// import org.keycloak.representations.IDToken;
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import java.util.Map


@Component
@Profile("dev")
class DevRequestInterceptor: HandlerInterceptorAdapter() {

    val log: Logger = LoggerFactory.getLogger(DevRequestInterceptor::class.java)

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse,  obj: Any): Boolean {

        // Principal principal = request.getUserPrincipal();
        
        // if (principal != null && principal instanceof KeycloakPrincipal) {
        //     log.info("Principal: name = " + principal.getName());

        //     KeycloakPrincipal kPrincipal = (KeycloakPrincipal) principal;

        //     IDToken token = kPrincipal.getKeycloakSecurityContext().getIdToken();
        //     Map<String, Object> customClaims = token.getOtherClaims();

        //     //Map<String, Object> customClaims = kPrincipal.getKeycloakSecurityContext().getIdToken().getOtherClaims();

        //     if (customClaims.containsKey("X-TENANT-ID")) {
        //         String tenantID = String.valueOf(customClaims.get("X-TENANT-ID"));
        //         log.info("attr = " + tenantID);


        //         TenantContext.setTenant(tenantID);
        //         return true;

        //         }
        //     else{
        //         log.info("tenant id is null...");
        //         response.getWriter().write("Tenant information for the user was not found");
        //         response.setStatus(400);
        //         return false;
        //         }
            
        //     }
        // else{
        //     log.info("tenant id is null...");
        //     response.getWriter().write("Tenant information for the user was not found");
        //     response.setStatus(400);
        //     return false;
            
        //     }


        log.info("In preHandle we are Intercepting the Request")
        log.info("____________________________________________")

        val requestURI: String = request.getRequestURI()
        val tenantID: String = request.getHeader("X-TenantID")

        //log.info("RequestURI::{} || Search for X-TenantID  :: {}", requestURI, tenantID)
        log.info("____________________________________________")
        if (tenantID == null) {
            TenantContext.setTenant("public")
            return true
            }
        else{
            TenantContext.setTenant(tenantID)
            return true
            }
 
    }

    @Throws(Exception::class)
    override fun postHandle(request: HttpServletRequest,  response: HttpServletResponse,  handler: Any,  modelAndView: ModelAndView?) {
        TenantContext.reset()
    }

}

