package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具
 *
 * @author fyn
 * @version 1.0
 */
public class LoggerUtil {

    public static final Logger testLogger = LoggerFactory.getLogger("test");

    public static void main(String[] args) {
        int a = 0;
        testLogger.info("info 测试日志 {}",a);
        testLogger.error("error 测试日志");
    }

}
