package com.cadastroit.authapi.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;

import java.util.Map;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AccountStatusException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleSecurityException(Exception exception) {
        exception.printStackTrace();

        ExceptionResponse response = EXCEPTION_MAPPINGS.getOrDefault(
            exception.getClass(), 
            new ExceptionResponse(500, "Unknown internal server error.")
        );
        
        ProblemDetail errorDetail = ProblemDetail.forStatusAndDetail(
            HttpStatusCode.valueOf(response.getStatus()), 
            exception.getMessage()
        );
        
        errorDetail.setProperty("description", response.getDescription());
        return errorDetail;
        
    }
	
	private static final Map<Class<? extends Exception>, ExceptionResponse> EXCEPTION_MAPPINGS = 
			
			Map.of(BadCredentialsException.class,
			new ExceptionResponse(401, "The username or password is incorrect 1-A"), AccountStatusException.class,
			new ExceptionResponse(403, "The account is locked 2-A"), AccessDeniedException.class,
			new ExceptionResponse(401, "You are not authorized to access this resource 2-A"), SignatureException.class,
			new ExceptionResponse(403, "The JWT signature is invalid 3-A"), ExpiredJwtException.class,
			new ExceptionResponse(401, "The JWT token has expired 4-A"));
	 
			private static class ExceptionResponse {

				private final int status;
				private final String description;

				public ExceptionResponse(int status, String description) {

					this.status = status;
					this.description = description;
				}

				public int getStatus() {

					return status;
				}

				public String getDescription() {

					return description;
			}
	 }	
}
