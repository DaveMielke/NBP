package org.nbp.b2g.ui.actions;
import org.nbp.b2g.ui.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import android.util.Log;

import android.net.Uri;

public class SendLog extends Action {
  private final static String LOG_TAG = SendLog.class.getName();

  private File makeLogFile () {
    boolean success = false;

    File directory = getActionDirectory();
    if (directory == null) return null;

    File file = new File(directory, "Android.log");
    if (file == null) return null;

    try {
      final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));

      try {
        LogProcessor logProcessor = new LogProcessor() {
          @Override
          protected boolean handleLog (String log) {
            try {
              writer.write(log);
              writer.newLine();
            } catch (IOException exception) {
              Log.w(LOG_TAG, "log file write error", exception);
            }

            return true;
          }
        };

        logProcessor.setFormat(LogProcessor.Format.TIME);
        logProcessor.addFilter(LogProcessor.Level.DEBUG);
        if (logProcessor.processLogs()) success = true;
      } finally {
        writer.close();
      }
    } catch (IOException exception) {
      Log.w(LOG_TAG, "log file creation error", exception);
    }

    if (!success) return null;
    file.setReadable(true, false);
    return file;
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

    {
      Uri.Builder ub = new Uri.Builder();
      ub.scheme("file");
      ub.path(file.toString());
      message.addAttachment(ub.build());
    }

    message.setSubject("Android log from user.");
    message.addBodyLine(R.string.email_to_the_user);
    message.addBodyLine();
    message.addBodyLine(R.string.email_sending_log);
    message.addBodyLine(R.string.email_sensitive_data_warning);

    return message.send();
  }

  @Override
  public boolean performAction () {
    File file = makeLogFile();
    if (file == null) return false;
    return sendLogFile(file);
  }

  public SendLog (Endpoint endpoint) {
    super(endpoint, true);
  }
}
