package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.io.File;
import java.io.Writer;
import java.io.IOException;

import org.nbp.common.FileMaker;
import org.nbp.common.AttachmentMaker;

import android.util.Log;

public class SendAndroidLog extends Action {
  private final static String LOG_TAG = SendAndroidLog.class.getName();

  private File makeLogFile () {
    FileMaker fileMaker = new AttachmentMaker() {
      @Override
      protected boolean writeContent (final Writer writer) {
        LogProcessor logProcessor = new LogProcessor() {
          @Override
          protected boolean handleLog (String log) {
            try {
              writer.write(log);
              writer.write('\n');
              return true;
            } catch (IOException exception) {
              Log.w(LOG_TAG, "log write error", exception);
            }

            return false;
          }
        };

        logProcessor.setFormat(LogProcessor.Format.TIME);
        logProcessor.addFilter(LogProcessor.Level.DEBUG);
        return logProcessor.processLogs();
      }
    };

    return fileMaker.makeFile("Android.log", this);
  }

  @Override
  public boolean performAction () {
    DeveloperMessage message = new DeveloperMessage() {
      @Override
      protected final boolean containsSensitiveData () {
        return true;
      }

      @Override
      protected final String getSubject () {
        return "Android log sent by user";
      }
    };

    if (message.isSendable()) {
      {
        File file = makeLogFile();
        if (file == null) return false;
        message.addAttachment(file, "a copy of your Android system log");
      }

      if (!message.send()) return false;
    }

    return true;
  }

  public SendAndroidLog (Endpoint endpoint) {
    super(endpoint, true);
  }
}
