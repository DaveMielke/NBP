package org.nbp.b2g.ui;

import android.util.Log;
import android.content.Context;
import android.widget.TextView;

public class BrailleWindow extends SystemWindow {
  private final static String LOG_TAG = BrailleWindow.class.getName();

  public final TextView textView;

  public void log () {
    StringBuilder sb = new StringBuilder();
    sb.append("braille window:");
    sb.append(" Vis:");
    sb.append(textView.getVisibility());
    sb.append(" Height:");
    sb.append(textView.getHeight());
    sb.append(" Width:");
    sb.append(textView.getWidth());
    sb.append(" Alpha:");
    sb.append(textView.getAlpha());
    Log.d(LOG_TAG, sb.toString());
  }

  public BrailleWindow (Context context) {
    super(context);

    window.setOrientation(window.VERTICAL);
    window.setAlpha(0f);

    textView = new TextView(context);
    textView.setFocusable(false);
    textView.setAlpha(0.5f);
    window.addView(textView);
  }
}
