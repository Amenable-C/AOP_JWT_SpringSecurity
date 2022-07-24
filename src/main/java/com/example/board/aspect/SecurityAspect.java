package com.example.board.aspect;

import com.example.board.security.SecurityService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
public class SecurityAspect {
    @Autowired
    SecurityService securityService;

    @Before("@annotation(tokenRequired)")
    public void authenticateWithToken(TokenRequired tokenRequired){
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();

        String token = request.getHeader("token");
        if(StringUtils.isEmpty(token)){
            throw new IllegalArgumentException("token is empty");
        }

        if(securityService.getClaims(token) == null || securityService.getSubject(token) == null){
            throw new IllegalArgumentException("token error!! claims or subject is null!!");
        }


    }
}
