package me.mad4a.logging;

import java.util.HashMap;
import java.util.Map;

/**
 * User: chsc4698@gmail.com
 * Date: 14-1-13
 * all rights reserved
 */
public abstract class Logger {
    /**
     *  0 ?
     * 10 DEBUG
     * 20 INFO
     * 30 WARNING
     * 40 ERROR
     * 50 CRITICAL
     * */
    protected Format format;
    protected String name;
    protected int level;

    public static int getLevel(String level) {
        if (level.equals("DEBUG")) {
            return 10;
        } else if (level.equals("INFO")) {
            return 20;
        } else if (level.equals("WARNING")) {
            return 30;
        } else if (level.equals("ERROR")) {
            return 40;
        } else if (level.equals("CRITICAL")) {
            return 50;
        }

        return 0;
    }

    public void setFormat(Format format) {
        this.format = format;
    }

    public Format getFormat() {
        return format;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract void i(String fmt, Object... args);
    public abstract void d(String fmt, Object... args);
    public abstract void w(String fmt, Object... args);
    public abstract void c(String fmt, Object... args);
    public abstract void e(String fmt, Object... args);
    protected abstract void log(String level, String msg);

    protected Map<String, Object> genLogRecord(String level,
                                               long time,
                                               String name,
                                               String msg) {
        Map<String, Object> ret = new HashMap<String, Object>();
        ret.put("LEVEL", level);
        ret.put("TIME", time);
        ret.put("NAME", name);
        ret.put("MSG", msg);

        return ret;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
