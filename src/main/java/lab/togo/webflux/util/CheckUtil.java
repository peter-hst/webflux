package lab.togo.webflux.util;

import lab.togo.webflux.exception.CheckException;
import lab.togo.webflux.vo.ErrMsg;

import java.util.stream.Stream;

public class CheckUtil {

    private final static String[] INVALID_NAMES = {"admin", "guanliyuan"};


    /**
     * 校验名字， 不成功时抛出校验异常
     *
     * @param value
     */
    public static void checkName(String value) {
        Stream.of(INVALID_NAMES)
                .filter(name -> name.equalsIgnoreCase(value))
                .findAny().ifPresent(name -> {
            throw new CheckException(ErrMsg.builder().field("name").msg(value).build());
        });
    }
}
