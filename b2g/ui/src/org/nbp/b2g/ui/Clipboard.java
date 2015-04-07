package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;

public class Clipboard {
  private final static String LOG_TAG = Clipboard.class.getName();

  private final static Object LOCK = new Object();
  private static ClipboardManager clipboard = null;

  public static void setClipboard (Context context) {
    synchronized (LOCK) {
      if (clipboard == null) {
        clipboard = (ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE);
      }
    }
  }

  public static ClipboardManager getClipboard () {
    synchronized (LOCK) {
      if (clipboard == null) Log.w(LOG_TAG, "no clipboard");
      return clipboard;
    }
  }

  public static ClipData newTextClip (String text) {
    return ClipData.newPlainText("B2G User Interface", text);
  }

  public static String getClipText (ClipData clip) {
    int count = clip.getItemCount();

    for (int index=0; index<count; index+=1) {
      ClipData.Item item = clip.getItemAt(index);

      if (item != null) {
        CharSequence text = item.getText();
        if (text != null) return text.toString();
      }
    }

    return null;
  }

  public static boolean putText (String text) {
    synchronized (LOCK) {
      if (clipboard != null) {
        clipboard.setPrimaryClip(newTextClip(text));
        return true;
      }
    }

    return false;
  }

  public static String getText () {
    synchronized (LOCK) {
      if (clipboard != null) {
        ClipData clip = clipboard.getPrimaryClip();

        if (clip != null) {
          return getClipText(clip);
        }
      }
    }

    return null;
  }

  private Clipboard () {
  }
}
