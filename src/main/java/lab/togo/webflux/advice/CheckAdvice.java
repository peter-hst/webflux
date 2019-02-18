package lab.togo.webflux.advice;

import lab.togo.webflux.vo.ErrMsg;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 异常处理切面
 */

@ControllerAdvice
public class CheckAdvice {

   /* @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<String> handleBindException(WebExchangeBindException e) {
        return new ResponseEntity<String>(toStr(e), HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(WebExchangeBindException.class)
    public ResponseEntity<List<ErrMsg>> handleBindException(WebExchangeBindException ex) {
        return new ResponseEntity<>(ex.getFieldErrors().stream()
                .map(e -> ErrMsg.builder().field(e.getField()).msg(e.getDefaultMessage()).build()).collect(Collectors.toList()),
                HttpStatus.BAD_REQUEST);
    }

    /**
     * 把异常转换为字符串
     *
     * @param ex
     * @return
     */
    private String toStr(WebExchangeBindException ex) {
        return ex.getFieldErrors().stream()
                .map(e -> e.getField() + ":" + e.getDefaultMessage())
                .reduce("", (s1, s2) -> s1 + "\n" + s2);
    }
}
