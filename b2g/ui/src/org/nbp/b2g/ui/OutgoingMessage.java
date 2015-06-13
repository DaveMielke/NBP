package org.nbp.b2g.ui;

import java.util.List;
import java.util.ArrayList;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OutgoingMessage {
  private final static String LOG_TAG = OutgoingMessage.class.getName();

  private List<String> directRecipients = new ArrayList<String>();
  private List<String> indirectRecipients = new ArrayList<String>();
  private List<String> hiddenRecipients = new ArrayList<String>();
  private String subjectText = null;
  private StringBuilder bodyText = new StringBuilder();

  public void addDirectRecipient (String recipient) {
    directRecipients.add(recipient);
  }

  public void addIndirectRecipient (String recipient) {
    indirectRecipients.add(recipient);
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

  private String[] toArray (List<String> list) {
    String[] array = new String[list.size()];
    return list.toArray(array);
  }

  public boolean sendMessage () {
    Context context = ApplicationContext.getContext();

    Intent sender = new Intent(Intent.ACTION_SENDTO);
    sender.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    sender.setType("message/rfc822");

    if (directRecipients.size() > 0) {
      sender.putExtra(Intent.EXTRA_EMAIL, toArray(directRecipients));
    } else {
      Log.w(LOG_TAG, "no direct recipient");
    }

    if (indirectRecipients.size() > 0) {
      sender.putExtra(Intent.EXTRA_CC, toArray(indirectRecipients));
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
