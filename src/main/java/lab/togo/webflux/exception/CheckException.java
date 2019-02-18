package lab.togo.webflux.exception;

import lab.togo.webflux.vo.ErrMsg;
import lombok.Data;

@Data
public class CheckException extends RuntimeException {

    //出错字段的消息
    private ErrMsg errMsg;

    public CheckException(ErrMsg errMsg) {
        this.errMsg = errMsg;
    }

    public CheckException() {
        super();
    }

    public CheckException(String message) {
        super(message);
    }

    public CheckException(String message, Throwable cause) {
        super(message, cause);
    }

    public CheckException(Throwable cause) {
        super(cause);
    }

    protected CheckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
