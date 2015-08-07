package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.graphics.Typeface;

public class BrailleMonitorWindow extends SystemWindow {
  private final static String LOG_TAG = BrailleMonitorWindow.class.getName();

  private final static float ALPHA = 0.5f;
  private final Typeface BRAILLE_FONT;

  private TextView brailleView = null;
  private TextView textView = null;

  public final void setContent (final String braille, final String text) {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        brailleView.setText(braille);
        textView.setText(text);
      }
    });
  }

  private final void addView (WindowLayout layout, View view) {
    view.setFocusable(false);
    view.setAlpha(ALPHA);
    layout.addView(view);
  }

  private final void initializeWindow (Context context, WindowLayout layout) {
    layout.setOrientation(layout.VERTICAL);
    layout.setAlpha(0f);

    brailleView = new TextView(context);
    brailleView.setTypeface(BRAILLE_FONT);
    addView(layout, brailleView);

    textView = new TextView(context);
    textView.setTypeface(Typeface.MONOSPACE);
    addView(layout, textView);
  }

  public BrailleMonitorWindow (Context context) {
    super(context);
    BRAILLE_FONT = Typeface.createFromAsset(context.getAssets(), "UBraille.ttf");
    initializeWindow(context, getLayout());
  }
}
