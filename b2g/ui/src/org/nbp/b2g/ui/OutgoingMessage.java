package org.nbp.b2g.ui;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
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
  private String subject = EMPTY_STRING;
  private final List<String> body = new ArrayList<String>();
  private final Collection<Uri> attachments = new LinkedHashSet<Uri>();

  public void reset () {
    primaryRecipients.clear();
    secondaryRecipients.clear();
    hiddenRecipients.clear();
    subject = EMPTY_STRING;
    body.clear();
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
    return subject;
  }

  public void setSubject (String text) {
    if (text == null) text = EMPTY_STRING;
    subject = text;
  }

  public String formatBody () {
    StringBuilder sb = new StringBuilder();
    boolean wasParagraph = true;

    for (String line : body) {
      boolean isParagraph;

      if (line.isEmpty()) {
        isParagraph = false;
      } else if (line.charAt(0) == ' ') {
        isParagraph = false;
      } else {
        isParagraph = true;
      }

      if (sb.length() > 0) {
        sb.append((isParagraph && wasParagraph)? ' ': '\n');
      }

      sb.append(line);
      wasParagraph = isParagraph;
    }

    if (sb.length() > 0) sb.append('\n');
    return sb.toString();
  }

  public void addBodyLine (String line) {
    body.add(line);
  }

  public void addBodyLine (int line) {
    String string = ApplicationContext.getString(line);
    if (string != null) addBodyLine(string);
  }

  public void addBodyLine () {
    addBodyLine("");
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
    Intent sender = new Intent();

    sender.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    sender.addCategory(Intent.CATEGORY_DEFAULT);

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

    if (!subject.isEmpty()) {
      sender.putExtra(Intent.EXTRA_SUBJECT, getSubject());
    } else {
      Log.w(LOG_TAG, "no subject");
    }

    if (!body.isEmpty()) {
      sender.putExtra(Intent.EXTRA_TEXT, formatBody());
    } else {
      Log.w(LOG_TAG, "no body");
    }

    if (attachments.isEmpty()) {
      sender.setData(Uri.parse("mailto:"));
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

    boolean found = LaunchUtilities.launchActivity(
      sender, R.string.message_select_outgoing_email_app,
      "com.android.email"
    );

    if (found) return true;
    Log.w(LOG_TAG, "outgoing message sender not found");
    return false;
  }

  public OutgoingMessage () {
    reset();
  }
}
