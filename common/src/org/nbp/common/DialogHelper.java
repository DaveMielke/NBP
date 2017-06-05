package org.nbp.common;

import android.app.Dialog;
import android.view.View;
import android.widget.TextView;

public class DialogHelper {
  private final Dialog dialog;

  public DialogHelper (Dialog dialog) {
    this.dialog = dialog;
  }

  public final Dialog getDialog () {
    return dialog;
  }

  public final View findViewById (int view) {
    return getDialog().findViewById(view);
  }

  public final void setText (int view, CharSequence text) {
    if (text != null) ((TextView)findViewById(view)).setText(text);
  }

  public final void setText (int view, int text) {
    setText(view, dialog.getContext().getString(text));
  }

  public final void setTextFromAsset (int view, String name) {
    final StringBuilder text = new StringBuilder();

    new InputProcessor() {
      @Override
      protected boolean handleLine (CharSequence line, int number) {
        if (text.length() > 0) text.append('\n');
        text.append(line);
        return true;
      }
    }.processInput(name);

    setText(view, text.toString());
  }

  public final void setValue (int view, int value) {
    setText(view, Integer.toString(value));
  }
}
