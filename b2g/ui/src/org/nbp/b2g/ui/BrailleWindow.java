package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.graphics.Typeface;

public class BrailleWindow extends SystemWindow {
  private final static String LOG_TAG = BrailleWindow.class.getName();

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

  private final void initializeWindow (Context context, WindowLayout window) {
    window.setOrientation(window.VERTICAL);
    window.setAlpha(0f);

    brailleView = new TextView(context);
    brailleView.setTypeface(BRAILLE_FONT);
    addView(window, brailleView);

    textView = new TextView(context);
    textView.setTypeface(Typeface.MONOSPACE);
    addView(window, textView);
  }

  public BrailleWindow (Context context) {
    super(context);
    BRAILLE_FONT = Typeface.createFromAsset(context.getAssets(), "UBraille.ttf");
    initializeWindow(context, getWindowLayout());
  }
}
