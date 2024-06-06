package com.alekseyz.testtask.springsecurityjwt.exceptionhandling;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiError {

        private HttpStatus status;
        private String message;
        private String logInfo;

        public ApiError(HttpStatus status, String message, String logInfo) {
            super();
            this.status = status;
            this.message = message;
            this.logInfo = logInfo;
        }

}
