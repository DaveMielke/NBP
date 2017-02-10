package org.nbp.b2g.ui.host;
import org.nbp.b2g.ui.host.actions.*;
import org.nbp.b2g.ui.*;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

import org.nbp.common.FileMaker;
import org.nbp.common.AttachmentMaker;

import android.util.Log;

public class FileScreenLogger extends ScreenLogger {
  private final String LOG_TAG = FileScreenLogger.class.getName();

  private File logFile = null;
  private Writer logWriter = null;

  @Override
  protected final boolean write (String text) {
    try {
      logWriter.write(text);
      logWriter.write('\n');
      return true;
    } catch (IOException exception) {
      Log.w(LOG_TAG, "log write error", exception);
    }

    return false;
  }

  @Override
  public final boolean logScreen () {
    FileMaker fileMaker = new AttachmentMaker() {
      @Override
      protected boolean writeContent (Writer writer) {
        logWriter = writer;

        try {
          return writeScreen();
        } finally {
          logWriter = null;
        }
      }
    };

    return (logFile = fileMaker.makeFile("screen.log", this)) != null;
  }

  public final File getFile () {
    return logFile;
  }

  public FileScreenLogger () {
    super();
  }
}
