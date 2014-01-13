package me.mad4a.logging;

import java.util.Map;

/**
 * User: chsc4698@gmail.com
 * Date: 14-1-13
 * all rights reserved
 */
public class CommandLineLogger extends Logger {

    public CommandLineLogger(String name) {
        format = new Format();
        this.name = name;
        level = 20;
    }

    @Override
    protected void log(String level, String msg) {
        Map<String, Object> record = genLogRecord(level,
                                                  System.currentTimeMillis(),
                                                  name,
                                                  msg);
        if (this.level <= getLevel(level)) {
            System.out.println(format.format(record));
        }
    }

    @Override
    public void i(String fmt, Object... args) {
        log("INFO", String.format(fmt, args));
    }

    @Override
    public void d(String fmt, Object... args) {
        log("DEBUG", String.format(fmt, args));
    }

    @Override
    public void w(String fmt, Object... args) {
        log("WARNING", String.format(fmt, args));
    }

    @Override
    public void c(String fmt, Object... args) {
        log("CRITICAL", String.format(fmt, args));
    }

    @Override
    public void e(String fmt, Object... args) {
        log("ERROR", String.format(fmt, args));
    }

}
