package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.io.File;
import java.io.Writer;
import java.io.IOException;

import android.util.Log;

public class SendAndroidLog extends Action {
  private final static String LOG_TAG = SendAndroidLog.class.getName();

  private File makeLogFile () {
    FileMaker fileMaker = new AttachmentMaker() {
      @Override
      protected boolean writeContent (final Writer writer) throws IOException {
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

  private boolean sendLogFile (File file) {
    OutgoingMessage message = new OutgoingMessage();

    {
      String[] recipients = ApplicationContext.getStringArray(R.array.recipients_log);

      if (recipients != null) {
        for (String recipient : recipients) {
          message.addPrimaryRecipient(recipient);
        }
      }
    }

    message.addAttachment(file);
    message.setSubject("Android log sent by user");
    message.addBodyLine(R.string.email_to_the_user);
    message.addBodyLine();
    message.addBodyLine(R.string.email_sending_android_log);
    message.addBodyLine(R.string.email_sensitive_data_warning);

    return message.send();
  }

  @Override
  public boolean performAction () {
    File file = makeLogFile();
    if (file == null) return false;
    return sendLogFile(file);
  }

  public SendAndroidLog (Endpoint endpoint) {
    super(endpoint, true);
  }
}
