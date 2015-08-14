package org.nbp.b2g.ui;

import android.util.Log;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import android.graphics.Typeface;

public class BrailleMonitorWindow extends SystemOverlayWindow {
  private final static String LOG_TAG = BrailleMonitorWindow.class.getName();

                                  /* RRGGBB */
  private final static int COLOR = 0XC00000;
  private final static float ALPHA = 0.8f;
  private final Typeface BRAILLE_FONT;

  private final ThreadLocal<TextView> brailleView = new ThreadLocal<TextView>();
  private final ThreadLocal<TextView> textView = new ThreadLocal<TextView>();

  public final void setContent (final String braille, final CharSequence text) {
    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        brailleView.get().setText(braille);
        textView.get().setText(text);
      }
    });
  }

  private static void addView (WindowLayout layout, View view) {
    view.setFocusable(false);
    view.setAlpha(ALPHA);
    layout.addView(view);
  }

  private static void addTextView (WindowLayout layout, TextView view) {
    view.setTextColor(COLOR | 0XFF000000);
    addView(layout, view);
  }

  public BrailleMonitorWindow (final Context context) {
    super(context);
    BRAILLE_FONT = Typeface.createFromAsset(context.getAssets(), "UBraille.ttf");

    runOnWindowThread(new Runnable() {
      @Override
      public void run () {
        WindowLayout layout = getLayout();

        layout.setOrientation(layout.VERTICAL);
        layout.setAlpha(0f);

        {
          TextView view = new TextView(context);
          view.setTypeface(BRAILLE_FONT);

          brailleView.set(view);
          addTextView(layout, view);
        }

        {
          TextView view = new TextView(context);
          view.setTypeface(Typeface.MONOSPACE);

          textView.set(view);
          addTextView(layout, view);
        }
      }
    });
  }
}
