package utils;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.RollingPolicy;
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogbackHelper {
    private static String PROPERTY_HOME_PATH = "HOME_PATH";
    private static String PROPERTY_FILE_PREFIX = "FILE_PREFIX";
    private static String PROPERTY_FILE_SUFFIX = "FILE_SUFFIX";
    private static String PROPERTY_FILE_SUFFIX_WITH_PATTERN = "FILE_SUFFIX_WITH_PATTERN";
    private static String PROPERTY_FILE_CONTENT_FORMAT = "FILE_CONTENT_FORMAT";
    private static String PROPERTY_MAX_HISTORY_SIZE = "MAX_HISTORY_SIZE";
    private static String PROPERTY_MAX_FILE_SIZE = "MAX_FILE_SIZE";
    private static String DEFAULT_HOME_PATH = "/tmp/";
    private static String DEFAULT_FILE_PREFIX = "op-";
    private static String DEFAULT_FILE_SUFFIX = ".log";
    private static String DEFAULT_FILE_SUFFIX_WITH_PATTERN = ".log-%d{yyyy-MM-dd}";
    private static String DEFAULT_FILE_CONTENT_FORMAT = "%d{yyyy-MM-dd HH:mm:ss}->%file:%line->%level->%msg%n";
    private static int DEFAULT_MAX_HISTORY_SIZE = 30;
    private static String DEFAULT_MAX_FILE_SIZE = "2048 mb";
    public static final String USE_DEFAULT = null;
    private static final ConcurrentHashMap<String, Logger> loggerMap = new ConcurrentHashMap();

    private LogbackHelper() {
    }

    public static final Logger aaa = LogbackHelper.getDefaultLogger("a111");

    public static void main(String[] args) {
        aaa.info("aaa");
    }

    public static final Logger getDefaultLogger(String loggerName) {
        return getCustomLogger(loggerName, USE_DEFAULT, USE_DEFAULT, USE_DEFAULT, USE_DEFAULT, USE_DEFAULT, USE_DEFAULT, USE_DEFAULT);
    }

    public static final Logger getCustomLogger(String loggerName, String logDir, String filePrefix, String fileSuffix, String fileSuffixWithPattern, String maxHistory, String logContentPattern, String maxFileSize) {
        Logger logger = (Logger) loggerMap.get(loggerName);
        if (logger == null) {
            ConcurrentHashMap var9 = loggerMap;
            synchronized (loggerMap) {
                logger = (Logger) loggerMap.get(loggerName);
                if (logger == null) {
                    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
                    List<ch.qos.logback.classic.Logger> loggerList = context.getLoggerList();
                    Iterator var12 = loggerList.iterator();

                    while (var12.hasNext()) {
                        ch.qos.logback.classic.Logger log = (ch.qos.logback.classic.Logger) var12.next();
                        String loggerNameConfig = log.getName();
                        if (loggerNameConfig.contains(loggerName)) {
                            logger = log;
                            break;
                        }
                    }

                    if (logger == null) {
                        logger = createLogger(loggerName, logDir, filePrefix, fileSuffix, fileSuffixWithPattern, maxHistory, logContentPattern, maxFileSize);
                    }

                    loggerMap.put(loggerName, logger);
                }
            }
        }

        return (Logger) logger;
    }

    private static String getPropertyOrDefault(LoggerContext context, String customValue, String property, String defaultValue) {
        if (!customValue.isEmpty()) {
            return customValue;
        } else {
            String propertyValue = context.getProperty(property);
            return propertyValue.isEmpty() ? defaultValue : propertyValue;
        }
    }

    private static final Logger createLogger(String loggerName, String logDir, String filePrefix, String fileSuffix, String fileSuffixWithPattern, String maxHistory, String logContentPattern, String maxFileSize) {
        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        String filePath = buildFilePath(context, logDir, filePrefix, loggerName, fileSuffix);
        String rollingFilePath = buildRollingPolicyFilePath(context, logDir, filePrefix, loggerName, fileSuffixWithPattern);
        RollingFileAppender<ILoggingEvent> appender = new RollingFileAppender();
        RollingPolicy rollingPolicy = buildRollingPolicy(context, appender, rollingFilePath, maxHistory, maxFileSize);
        Encoder encoder = buildEncoder(context, logContentPattern);
        appender.setFile(filePath);
        appender.setName(loggerName);
        appender.setContext(context);
        appender.setAppend(true);
        appender.setRollingPolicy(rollingPolicy);
        appender.setEncoder(encoder);
        appender.setPrudent(false);
        appender.start();
        ch.qos.logback.classic.Logger logbackLogger = context.getLogger(loggerName);
        logbackLogger.setAdditive(false);
        logbackLogger.addAppender(appender);
        return logbackLogger;
    }

    private static final String buildFilePath(LoggerContext context, String logDir, String filePrefix, String loggerName, String fileSuffix) {
        return _buildPath(context, logDir, filePrefix, loggerName, fileSuffix, PROPERTY_FILE_SUFFIX, DEFAULT_FILE_SUFFIX);
    }

    private static final String buildRollingPolicyFilePath(LoggerContext context, String logDir, String filePrefix, String loggerName, String fileSuffixWithPattern) {
        return _buildPath(context, logDir, filePrefix, loggerName, fileSuffixWithPattern, PROPERTY_FILE_SUFFIX_WITH_PATTERN, DEFAULT_FILE_SUFFIX_WITH_PATTERN);
    }

    private static final String _buildPath(LoggerContext context, String logDir, String filePrefix, String loggerName, String suffix, String propertyKey, String defaultValue) {
        logDir = getPropertyOrDefault(context, logDir, PROPERTY_HOME_PATH, DEFAULT_HOME_PATH);
        Path path = Paths.get(logDir);
        if (Files.notExists(path, new LinkOption[]{LinkOption.NOFOLLOW_LINKS})) {
            try {
                Files.createDirectories(path);
            } catch (Exception var9) {
                var9.printStackTrace();
            }
        }

        filePrefix = getPropertyOrDefault(context, filePrefix, PROPERTY_FILE_PREFIX, DEFAULT_FILE_PREFIX);
        suffix = getPropertyOrDefault(context, suffix, propertyKey, defaultValue);
        Path fullFileName = Paths.get(logDir, filePrefix + loggerName + suffix);
        return fullFileName.toString();
    }

    private static RollingPolicy buildRollingPolicy(LoggerContext context, RollingFileAppender appender, String fileNamePattern, String maxHistoryFileCount, String maxFileSize) {
        int maxHistory = DEFAULT_MAX_HISTORY_SIZE;

        try {
            maxHistory = Integer.parseInt(maxHistoryFileCount);
        } catch (Exception var9) {
            maxHistoryFileCount = "";
        }

        if (maxHistoryFileCount.isEmpty()) {
            maxHistoryFileCount = context.getProperty(PROPERTY_MAX_HISTORY_SIZE);
        }

        TimeBasedRollingPolicy rollingPolicy = new TimeBasedRollingPolicy();
        rollingPolicy.setContext(context);
        rollingPolicy.setParent(appender);
        rollingPolicy.setFileNamePattern(fileNamePattern);
        rollingPolicy.setMaxHistory(maxHistory);
        boolean alsoRollingByFileSize = fileNamePattern.indexOf("%i") >= 0;
        if (alsoRollingByFileSize) {
            maxFileSize = getPropertyOrDefault(context, maxFileSize, PROPERTY_MAX_FILE_SIZE, DEFAULT_MAX_FILE_SIZE);
            SizeAndTimeBasedFNATP<ILoggingEvent> triggeringPolicy = new SizeAndTimeBasedFNATP();
            triggeringPolicy.setContext(context);
            triggeringPolicy.setMaxFileSize(maxFileSize);
            rollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(triggeringPolicy);
            rollingPolicy.start();
            triggeringPolicy.start();
            appender.setTriggeringPolicy(triggeringPolicy);
            triggeringPolicy.setTimeBasedRollingPolicy(rollingPolicy);
        } else {
            rollingPolicy.start();
        }

        return rollingPolicy;
    }

    private static Encoder buildEncoder(LoggerContext context, String fileContentEncoderPattern) {
        fileContentEncoderPattern = getPropertyOrDefault(context, fileContentEncoderPattern, PROPERTY_FILE_CONTENT_FORMAT, DEFAULT_FILE_CONTENT_FORMAT);
        PatternLayoutEncoder encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern(fileContentEncoderPattern);
        encoder.start();
        return encoder;
    }
}
