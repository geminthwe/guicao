package cn.edu.fzu.sosd.guicao.common.exception;

import org.springframework.http.HttpStatus;

public class BadRequestException extends AbstractMyHttpException{
    public BadRequestException(String msgTpl, Object... args) {
        super(msgTpl, args);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
