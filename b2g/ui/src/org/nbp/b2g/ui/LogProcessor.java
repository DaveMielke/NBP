package org.nbp.b2g.ui;

import java.util.List;
import java.util.ArrayList;

import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

import android.util.Log;

public abstract class LogProcessor {
  private final static String LOG_TAG = LogProcessor.class.getName();

  protected abstract boolean handleLog (String log);

  public enum Format {
    BRIEF,
    TAG,
    PROCESS,
    THREAD,
    TIME,
    THREAD_TIME,
    LONG,
    RAW
  }

  private Format logFormat = Format.BRIEF;

  public void setFormat (Format format) {
    logFormat = format;
  }

  public enum Buffer {
    MAIN,
    SYSTEM,
    RADIO,
    EVENTS
  }

  private List<Buffer> logBuffers = new ArrayList<Buffer>();

  public void addBuffer (Buffer buffer) {
    logBuffers.add(buffer);
  }

  public enum Level {
    VERBOSE,
    DEBUG,
    INFO,
    WARNING,
    ERROR,
    FATAL,
    SILENT
  }

  private static class Filter {
    public final String tag;
    private final Level level;

    public Filter (String tag, Level level) {
      this.tag = (tag != null)? tag: "*";
      this.level = (level != null)? level: Level.INFO;
    }
  }

  private final List<Filter> logFilters = new ArrayList<Filter>();

  public void addFilter (String tag, Level level) {
    logFilters.add(new Filter(tag, level));
  }

  public void addFilter (String tag) {
    addFilter(tag, null);
  }

  public void addFilter (Level level) {
    addFilter(null, level);
  }

  private List<String> makeCommand () {
    List<String> command = new ArrayList<String>();

    command.add("logcat");
    command.add("-d");

    command.add("-v");
    command.add(logFormat.name().toLowerCase().replace("_", ""));

    {
      List<Buffer> buffers = logBuffers;

      if (buffers.isEmpty()) {
        buffers = new ArrayList<Buffer>();
        buffers.add(Buffer.MAIN);
        buffers.add(Buffer.SYSTEM);
      }

      for (Buffer buffer : buffers) {
        command.add("-b");
        command.add(buffer.name().toLowerCase());
      }
    }

    {
      List<Filter> filters = logFilters;

      if (filters.isEmpty()) {
        filters.add(new Filter(null, null));
      }

      for (Filter filter : filters) {
        command.add((filter.tag + ":" + filter.level.name().charAt(0)));
      }
    }

    return command;
  }

  public boolean processLogs () {
    ProcessBuilder pb = new ProcessBuilder(makeCommand());

    try {
      Process process = pb.start();
      BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

      while (true) {
        String log = reader.readLine();
        if (log == null) return true;
        if (!handleLog(log)) break;
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, "logs read error", exception);
    }

    return false;
  }

  public LogProcessor () {
  }
}
