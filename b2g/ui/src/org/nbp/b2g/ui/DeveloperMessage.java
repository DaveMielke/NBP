package org.nbp.b2g.ui;

import java.io.File;
import java.util.List;
import java.util.ArrayList;

import org.nbp.common.OutgoingMessage;

public abstract class DeveloperMessage {
  protected abstract boolean containsSensitiveData ();
  protected abstract String getSubject ();

  private OutgoingMessage message = null;
  private final List<String> attachmentDescriptions = new ArrayList<String>();

  public final boolean isSendable () {
    return message != null;
  }

  public final void addAttachment (File file, String description) {
    if (isSendable()) {
      message.addAttachment(file);
      attachmentDescriptions.add(description);
    }
  }

  private final void describeAttachments () {
    String prefix = "This message contains";

    if (attachmentDescriptions.size() == 1) {
      message.addBodyLine(prefix);
      message.addBodyLine((attachmentDescriptions.get(0) + '.'));
    } else {
      message.addBodyLine((prefix + ':'));

      for (String description : attachmentDescriptions) {
        message.addBodyLine(("  *  " + description));
      }

      message.addBodyLine();
    }
  }

  public final boolean send () {
    if (isSendable()) {
      describeAttachments();
      message.addBodyLine("It's being sent to the B2G developers for their analysis.");

      if (containsSensitiveData()) {
        message.addBodyLine("If you suspect that it may contain sensitive information that they shouldn't see then DO NOT send it.");
      } else {
        message.addBodyLine("It's safe to send it because it doesn't contain any sensitive data.");
      }

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
      message.addBodyLine("To the user:");
      message.addBodyLine();
    } else {
      message = null;
    }
  }
}
