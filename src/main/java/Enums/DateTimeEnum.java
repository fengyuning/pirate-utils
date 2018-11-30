package Enums;


import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public enum DateTimeEnum {
    DATE("yyyy-MM-dd", "[\\d]{4}-[\\d]{2}-[\\d]{2}", DateTimeFormat.forPattern("yyyy-MM-dd")),
    DATE_TIME("yyyy-MM-dd HH:mm:dd", "[\\d]{4}-[\\d]{2}-[\\d]{2} [\\d]{2}:[\\d]{2}:[\\d]{2}",
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:dd")),
    MONTH("yyyy-MM", "[\\d]{4}-[\\d]{2}", DateTimeFormat.forPattern("yyyy-MM"));

    private String style;
    private String regex;
    private DateTimeFormatter format;

    DateTimeEnum(String style, String regex, DateTimeFormatter format) {
        this.style = style;
        this.regex = regex;
        this.format = format;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public DateTimeFormatter getFormat() {
        return format;
    }

    public void setFormat(DateTimeFormatter format) {
        this.format = format;
    }
}
