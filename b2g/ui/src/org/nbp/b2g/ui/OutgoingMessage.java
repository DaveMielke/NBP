package org.nbp.b2g.ui;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.ArrayList;
import java.io.File;

import android.util.Log;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class OutgoingMessage {
  private final static String LOG_TAG = OutgoingMessage.class.getName();

  private final static String EMPTY_STRING = "";

  private final Collection<String> primaryRecipients = new LinkedHashSet<String>();
  private final Collection<String> secondaryRecipients = new LinkedHashSet<String>();
  private final Collection<String> hiddenRecipients = new LinkedHashSet<String>();
  private String subjectText = EMPTY_STRING;
  private final StringBuilder bodyText = new StringBuilder();
  private final Collection<Uri> attachments = new LinkedHashSet<Uri>();

  public void reset () {
    primaryRecipients.clear();
    secondaryRecipients.clear();
    hiddenRecipients.clear();
    subjectText = EMPTY_STRING;
    bodyText.setLength(0);
    attachments.clear();
  }

  private String[] toStringArray (Collection<String> collection) {
    String[] array = new String[collection.size()];
    return collection.toArray(array);
  }

  private Uri[] toUriArray (Collection<Uri> collection) {
    Uri[] array = new Uri[collection.size()];
    return collection.toArray(array);
  }

  public String[] getPrimaryRecipients () {
    return toStringArray(primaryRecipients);
  }

  public boolean addPrimaryRecipient (String recipient) {
    secondaryRecipients.remove(recipient);
    hiddenRecipients.remove(recipient);
    return primaryRecipients.add(recipient);
  }

  public boolean removePrimaryRecipient (String recipient) {
    return primaryRecipients.remove(recipient);
  }

  public String[] getSecondaryRecipients () {
    return toStringArray(secondaryRecipients);
  }

  public boolean addSecondaryRecipient (String recipient) {
    if (primaryRecipients.contains(recipient)) return false;
    hiddenRecipients.remove(recipient);
    return secondaryRecipients.add(recipient);
  }

  public boolean removeSecondaryRecipient (String recipient) {
    return secondaryRecipients.remove(recipient);
  }

  public String[] getHiddenRecipients () {
    return toStringArray(hiddenRecipients);
  }

  public boolean addHiddenRecipient (String recipient) {
    if (primaryRecipients.contains(recipient)) return false;
    if (secondaryRecipients.contains(recipient)) return false;
    return hiddenRecipients.add(recipient);
  }

  public boolean removeHiddenRecipient (String recipient) {
    return hiddenRecipients.remove(recipient);
  }

  public String getSubject () {
    return subjectText;
  }

  public void setSubject (String subject) {
    if (subject == null) subject = EMPTY_STRING;
    subjectText = subject;
  }

  public String getBody () {
    return bodyText.toString();
  }

  public void addBodyLine (String line) {
    if (bodyText.length() > 0) bodyText.append('\n');
    bodyText.append(line);
  }

  public Uri[] getAttachments () {
    return toUriArray(attachments);
  }

  public boolean addAttachment (Uri attachment) {
    return attachments.add(attachment);
  }

  public boolean removeAttachment (Uri attachment) {
    return attachments.remove(attachment);
  }

  public boolean addAttachment (File file) {
    String problem = null;

    if (!file.exists()) {
      problem = "file not found";
    } else if (!file.isFile()) {
      problem = "not a file";
    } else if (!file.canRead()) {
      problem = "file not readable";
    } else {
      return addAttachment(Uri.fromFile(file));
    }

    Log.w(LOG_TAG, problem + ": " + file.toString());
    return false;
  }

  public boolean removeAttachment (File file) {
    return removeAttachment(Uri.fromFile(file));
  }

  public boolean send () {
    Context context = ApplicationContext.getContext();
    Intent sender = new Intent();

    sender.setData(Uri.parse("mailto:"));
    sender.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    if (!primaryRecipients.isEmpty()) {
      sender.putExtra(Intent.EXTRA_EMAIL, getPrimaryRecipients());
    } else {
      Log.w(LOG_TAG, "no primary recipient");
    }

    if (!secondaryRecipients.isEmpty()) {
      sender.putExtra(Intent.EXTRA_CC, getSecondaryRecipients());
    }

    if (!hiddenRecipients.isEmpty()) {
      sender.putExtra(Intent.EXTRA_BCC, getHiddenRecipients());
    }

    if (!subjectText.isEmpty()) {
      sender.putExtra(Intent.EXTRA_SUBJECT, getSubject());
    } else {
      Log.w(LOG_TAG, "no subject");
    }

    if (bodyText.length() > 0) {
      sender.putExtra(Intent.EXTRA_TEXT, getBody());
    } else {
      Log.w(LOG_TAG, "no body");
    }

    if (attachments.isEmpty()) {
    //sender.setType("message/rfc822");
      sender.setAction(Intent.ACTION_SENDTO);
    } else {
      sender.setType("*/*");
      ArrayList<Uri> array = new ArrayList<Uri>(attachments);

      if (array.size() == 1) {
        sender.putExtra(Intent.EXTRA_STREAM, array.get(0));
        sender.setAction(Intent.ACTION_SEND);
      } else {
        sender.putParcelableArrayListExtra(Intent.EXTRA_STREAM, array);
        sender.setAction(Intent.ACTION_SEND_MULTIPLE);
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
    reset();
  }
}
