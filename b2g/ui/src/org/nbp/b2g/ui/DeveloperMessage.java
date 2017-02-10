package org.nbp.b2g.ui;

import java.io.File;

import org.nbp.common.OutgoingMessage;

public abstract class DeveloperMessage {
  protected abstract boolean containsSensitiveData ();
  protected abstract String getSubject ();

  private OutgoingMessage message = null;

  public final boolean isSendable () {
    return message != null;
  }

  public final void addLine (int line) {
    if (isSendable()) message.addBodyLine(line);
  }

  public final void addAttachment (File file) {
    if (isSendable()) message.addAttachment(file);
  }

  public final boolean send () {
    if (isSendable()) {
      message.addBodyLine();
      message.addBodyLine(
        containsSensitiveData()?
        R.string.email_sensitive_data_warning:
        R.string.email_no_sensitive_data
      );

      if (!message.send()) return false;
    }

    return true;
  }

  protected DeveloperMessage () {
    String[] recipients = ApplicationContext.getStringArray(R.array.recipients_crash);

    if (recipients.length > 0) {
      message = new OutgoingMessage();

      for (String recipient : recipients) {
        message.addPrimaryRecipient(recipient);
      }

      message.setSubject(getSubject());
      message.addBodyLine(R.string.email_to_the_user);
      message.addBodyLine();
    } else {
      message = null;
    }
  }
}
