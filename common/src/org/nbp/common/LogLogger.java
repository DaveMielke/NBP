package org.nbp.common;

import android.util.Log;

public class LogLogger extends Logger {
  private final String logTag;

  public LogLogger (String tag, Iterator iterator) {
    super(iterator);
    logTag = tag;
  }

  @Override
  protected final boolean write (String line) {
    Log.d(logTag, line);
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
