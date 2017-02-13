package org.nbp.common;

import java.io.IOException;
import java.io.File;
import java.io.Writer;

import android.util.Log;

public class FileLogger extends Logger {
  private final static String LOG_TAG = FileLogger.class.getName();

  private final String fileName;

  public FileLogger (String name, Iterator iterator) {
    super(iterator);
    fileName = name;
  }

  private final StringBuilder logContent = new StringBuilder();
  private File logFile = null;

  @Override
  protected final boolean write (String text) {
    logContent.append(text);
    logContent.append('\n');
    return true;
  }

  @Override
  protected final boolean end () {
    FileMaker fileMaker = new AttachmentMaker() {
      @Override
      protected boolean writeContent (Writer writer) {
        try {
          writer.write(logContent.toString());
          return true;
        } catch (IOException exception) {
          Log.w(LOG_TAG, ("log write error: " + exception.getMessage()));
        }

        return false;
      }
    };

    return (logFile = fileMaker.makeFile(fileName, this)) != null;
  }

  public final File getFile () {
    return logFile;
  }
}
