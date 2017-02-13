package org.nbp.common;

import android.util.Log;

public class LogLogger extends Logger {
  private final String logTag;
  private final int logLevel;

  public LogLogger (String tag, int level, Iterator iterator) {
    super(iterator);
    logTag = tag;
    logLevel = level;
  }

  public LogLogger (String tag, Iterator iterator) {
    this(tag, Log.VERBOSE, iterator);
  }

  @Override
  protected final boolean write (String text) {
    Log.println(logLevel, logTag, text);
    return true;
  }

  @Override
  protected final boolean begin () {
    return write("begin");
  }

  @Override
  protected final boolean end () {
    return write("end");
  }
}
