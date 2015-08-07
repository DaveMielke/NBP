package org.nbp.b2g.ui;

import android.content.Context;
import android.widget.TextView;
import android.graphics.Typeface;

public class BrailleWindow extends SystemWindow {
  private TextView textView = null;

  public void setText (final String text) {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        textView.setText(text);
      }
    });
  }

  public BrailleWindow (final Context context) {
    super(context);

    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        windowView.setOrientation(windowView.VERTICAL);
        windowView.setAlpha(0f);

        textView = new TextView(context);
        textView.setFocusable(false);
        textView.setAlpha(0.5f);
        textView.setTypeface(Typeface.MONOSPACE);
        windowView.addView(textView);
      }
    });
  }
}
