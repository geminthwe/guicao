package cn.edu.fzu.sosd.guicao.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        log.error("Internal Server Error: ", ex);
        return new ResponseEntity<>("UNKNOWN Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = AbstractMyHttpException.class)
    @ResponseBody
    public ResponseEntity<?> handleMyHttpException(AbstractMyHttpException e, WebRequest request) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus());
    }
}
