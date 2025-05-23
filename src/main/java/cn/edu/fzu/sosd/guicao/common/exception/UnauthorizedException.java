package cn.edu.fzu.sosd.guicao.common.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends AbstractMyHttpException{
    public UnauthorizedException(String msgTpl, Object... args) {
        super(msgTpl, args);
        setHttpStatus(HttpStatus.UNAUTHORIZED);
    }
}
