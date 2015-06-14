package org.nbp.b2g.ui;

import java.util.List;
import java.util.ArrayList;
import java.io.File;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OutgoingMessage {
  private final static String LOG_TAG = OutgoingMessage.class.getName();

  private List<String> primaryRecipients = new ArrayList<String>();
  private List<String> secondaryRecipients = new ArrayList<String>();
  private List<String> hiddenRecipients = new ArrayList<String>();
  private String subjectText = null;
  private StringBuilder bodyText = new StringBuilder();
  private ArrayList<Uri> attachments = new ArrayList<Uri>();

  public void addPrimaryRecipient (String recipient) {
    primaryRecipients.add(recipient);
  }

  public void addSecondaryRecipient (String recipient) {
    secondaryRecipients.add(recipient);
  }

  public void addHiddenRecipient (String recipient) {
    hiddenRecipients.add(recipient);
  }

  public void setSubject (String subject) {
    subjectText = subject;
  }

  public void addBodyLine (String line) {
    if (bodyText.length() > 0) bodyText.append('\n');
    bodyText.append(line);
  }

  public void addAttachment (File file) {
    attachments.add(Uri.fromFile(file));
  }

  private String[] toArray (List<String> list) {
    String[] array = new String[list.size()];
    return list.toArray(array);
  }

  public boolean sendMessage () {
    Context context = ApplicationContext.getContext();
    boolean multipleAttachments = attachments.size() > 1;

    Intent sender = new Intent(
      multipleAttachments? Intent.ACTION_SEND_MULTIPLE: Intent.ACTION_SENDTO
    );

    sender.setData(Uri.parse("mailto:"));
  //sender.setType("message/rfc822");
    sender.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    if (primaryRecipients.size() > 0) {
      sender.putExtra(Intent.EXTRA_EMAIL, toArray(primaryRecipients));
    } else {
      Log.w(LOG_TAG, "no primary recipient");
    }

    if (secondaryRecipients.size() > 0) {
      sender.putExtra(Intent.EXTRA_CC, toArray(secondaryRecipients));
    }

    if (hiddenRecipients.size() > 0) {
      sender.putExtra(Intent.EXTRA_BCC, toArray(hiddenRecipients));
    }

    if (subjectText != null) {
      sender.putExtra(Intent.EXTRA_SUBJECT, subjectText);
    } else {
      Log.w(LOG_TAG, "no subject");
    }

    if (bodyText.length() > 0) {
      sender.putExtra(Intent.EXTRA_TEXT, bodyText.toString());
    } else {
      Log.w(LOG_TAG, "no body");
    }

    if (attachments.size() > 0) {
      if (multipleAttachments) {
        sender.putParcelableArrayListExtra(Intent.EXTRA_STREAM, attachments);
      } else {
        sender.putExtra(Intent.EXTRA_STREAM, attachments.get(0));
      }
    }

    if (context != null) {
      String title = ApplicationContext.getString(R.string.message_select_outgoing_email_app);
      Intent chooser = Intent.createChooser(sender, title);
      chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

      try {
        context.startActivity(chooser);
        return true;
      } catch (android.content.ActivityNotFoundException ex) {
        Log.w(LOG_TAG, "outgoing message sender not found");
      }
    }

    return false;
  }

  public OutgoingMessage () {
  }
}
