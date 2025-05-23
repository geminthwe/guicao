package cn.edu.fzu.sosd.guicao.common.exception;

import com.google.common.base.Strings;
import org.springframework.http.HttpStatus;

public abstract class AbstractMyHttpException extends RuntimeException{
    private static final long serialVersionUID = 1873434555278689L;

    protected HttpStatus httpStatus;

    public AbstractMyHttpException(String msgTpl, Object... args){
        super(args == null || args.length == 0 ? msgTpl : Strings.lenientFormat(msgTpl, args));
    }

    protected void setHttpStatus(HttpStatus httpStatus){
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus(){
        return httpStatus;
    }
}
